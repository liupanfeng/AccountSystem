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
//            try {
//                performSync(mContext,account,extras,authority,provider,syncResult);
//            } catch (OperationCanceledException e) {
//                e.printStackTrace();
//            }

        }
    }


    private static void performSync(Context context, Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult)
            throws OperationCanceledException {
        HashMap<String, Long> localContacts = new HashMap<String, Long>();
        mContentResolver = context.getContentResolver();
        Log.d(TAG, "performSync: " + account.toString());

//        Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
//        Uri dateUri=Uri.parse("content://com.android.contacts/data");
//        Cursor cursor = mContentResolver.query(uri, new String[] { "contact_id" },
//                null, null, null);
//
//        while (cursor.moveToNext()) {
//            String id = cursor.getString(0);
//            // 2.根据联系人的id，查询data表，把这个id的数据取出来
//            // 系统api查询data表的时候不是真正的查询的data表，而是查询data表的视图
//            if (id==null)continue;
//            Log.d(TAG,"联系人id: " + id);
//            Cursor dataCursor = mContentResolver.query(dateUri, new String[] { "data1", "mimetype" },
//                    "raw_contact_id=?", new String[] { id }, null);
//            if (dataCursor==null){
//                Log.d(TAG,"dataCursor==null ");
//                continue;
//            }else{
//                int count=dataCursor.getColumnCount();
//                Log.d(TAG,"count "+count);
//            }
//            while (dataCursor.moveToNext()) {
//                String data1 = dataCursor.getString(0);
//                Log.d(TAG,"data1=" + data1);
//                String mimetype = dataCursor.getString(1);
//                Log.d(TAG,"mimetype=" + mimetype);
//            }
//            dataCursor.close();
//        }
//        cursor.close();


//        for (Map.Entry<String,Long> entry:localContacts.entrySet()){
//           String key= entry.getKey();
//            Long value=entry.getValue();
//            Log.i(TAG,key+"----"+value);
//        }

//        ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
//        LastFmServer server = AndroidLastFmServerFactory.getServer();
//        try {
//            Friends friends = server.getFriends(account.name, "", "50");
//            for (User user : friends.getFriends()) {
//                if (!localContacts.containsKey(user.getName())) {
//                    if (user.getRealName().length() > 0)
//                        addContact(account, user.getRealName(), user.getName());
//                    else
//                        addContact(account, user.getName(), user.getName());
//                } else {
//                    Track[] tracks = server.getUserRecentTracks(user.getName(), "true", 1);
//                    if (tracks.length > 0) {
//                        updateContactStatus(operationList, localContacts.get(user.getName()), tracks[0]);
//                    }
//                }
//            }
//            if(operationList.size() > 0)
//                mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
//        } catch (Exception e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
    }

    private static void addContact(Account account, String name, String username) {
        Log.i(TAG, "Adding contact: " + name);
        ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

        //Create our RawContact
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, account.name);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, account.type);
        builder.withValue(ContactsContract.RawContacts.SYNC1, username);
        operationList.add(builder.build());

        //Create a Data record of common type 'StructuredName' for our RawContact
        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
        operationList.add(builder.build());

        //Create a Data record of custom type "vnd.android.cursor.item/vnd.fm.last.android.profile" to display a link to the Last.fm profile
        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.com.chinasofti.rcs.systemaccount.profile");
        builder.withValue(ContactsContract.Data.DATA1, username);
        builder.withValue(ContactsContract.Data.DATA2, "Last.fm Profile");
        builder.withValue(ContactsContract.Data.DATA3, "View profile");
        operationList.add(builder.build());

        try {
//            mContentResolver.applyBatch(ContactsContract.AUTHORITY, operationList);
        } catch (Exception e) {
            Log.e(TAG, "Something went wrong during creation! " + e);
            e.printStackTrace();
        }
    }




}
