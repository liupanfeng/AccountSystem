package com.chinasofti.rcs.systemaccount;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static final String TAG = "SyncService";
    private static SyncAdapter sSyncAdapter = null;
    private static ContentResolver mContentResolver = null;

    public SyncService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sSyncAdapterLock) {
            if(sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }


    static  class SyncAdapter extends AbstractThreadedSyncAdapter {

        private Context mContext;
        public SyncAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
            this.mContext=context;
        }



        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
             Log.d("SyncAdapter", "实现同步数据 ...");
             //实现同步数据
            mContentResolver = mContext.getContentResolver();
            try {
                performSync(mContext,account,extras,authority,provider,syncResult);
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            }

        }
    }

    private static String UsernameColumn = ContactsContract.RawContacts.SYNC1;
    private static String PhotoTimestampColumn = ContactsContract.RawContacts.SYNC2;

    private static class SyncEntry {
        public Long raw_id = 0L;
        public Long photo_timestamp = null;
    }


    private static void performSync(Context context, Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
            throws OperationCanceledException {
        HashMap<String, SyncEntry> localContacts = new HashMap<>();
        mContentResolver = context.getContentResolver();
        Log.i(TAG, "performSync: " + account.toString());

        // Load the local contacts
        Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.RawContacts.ACCOUNT_NAME, account.name).appendQueryParameter(
                ContactsContract.RawContacts.ACCOUNT_TYPE, account.type).build();
        Cursor c1 = mContentResolver.query(rawContactUri, new String[] { BaseColumns._ID, UsernameColumn, PhotoTimestampColumn }, null, null, null);
        while (c1.moveToNext()) {
            SyncEntry entry = new SyncEntry();
            entry.raw_id = c1.getLong(c1.getColumnIndex(BaseColumns._ID));
            entry.photo_timestamp = c1.getLong(c1.getColumnIndex(PhotoTimestampColumn));
            localContacts.put(c1.getString(1), entry);
        }

        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
        try {
            // If we don't have any contacts, create one. Otherwise, set a
            // status message
            if (localContacts.get("小雅") == null) {
                addContact(account, "小雅", "小雅雅");
            } else {
//				if (localContacts.get("小兮兮").photo_timestamp == null || System.currentTimeMillis() > (localContacts.get("小兮兮").photo_timestamp + 604800000L)) {
//					//You would probably download an image file and just pass the bytes, but this sample doesn't use network so we'll decode and re-compress the icon resource to get the bytes
//					ByteArrayOutputStream stream = new ByteArrayOutputStream();
//					Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon);
//					icon.compress(CompressFormat.PNG, 0, stream);
//					updateContactPhoto(operationList, localContacts.get("小兮兮").raw_id, stream.toByteArray());
//				}
//				updateContactStatus(operationList, localContacts.get("小兮兮").raw_id, "hunting wabbits");
            }
            if (operationList.size() > 0)
                mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private static void addContact(Account account, String name, String username) {
        Log.i(TAG, "Adding contact: " + name);
        ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, account.name);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, account.type);
        builder.withValue(ContactsContract.RawContacts.SYNC1, username);
        operationList.add(builder.build());

        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
        operationList.add(builder.build());

        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.com.chinasofti.rcs.systemaccount.account.profile");
        builder.withValue(ContactsContract.Data.DATA1, username);
        builder.withValue(ContactsContract.Data.DATA2, "和飞信 Profile");
        builder.withValue(ContactsContract.Data.DATA3, "发起视频");
        operationList.add(builder.build());

        try {
            mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
