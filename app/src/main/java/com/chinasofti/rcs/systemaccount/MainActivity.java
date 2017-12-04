package com.chinasofti.rcs.systemaccount;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ContentResolver mContentReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentReceiver = this.getContentResolver();

//        Uri uri=Uri.parse("content://com.android.contacts/raw_contacts");
//        Uri dateUri=Uri.parse("content://com.android.contacts/data");
//
//        Cursor cursor = mContentReceiver.query(uri, new String[] { "contact_id" },
//                null, null, null);
//
//        while (cursor.moveToNext()) {
//            String id = cursor.getString(0);
//            // 2.根据联系人的id，查询data表，把这个id的数据取出来
//            // 系统api查询data表的时候不是真正的查询的data表，而是查询data表的视图
//            if (id==null)continue;
//            Log.d(TAG,"联系人id: " + id);
//            Cursor dataCursor = mContentReceiver.query(dateUri, new String[] { "data1", "mimetype" },
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


//        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
//        ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();
//        String username = "AA";
//        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
//        builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 3470);
//        builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.com.chinasofti.rcs.systemaccount.profile");
//        builder.withValue(ContactsContract.Data.DATA1, username);
//        builder.withValue(ContactsContract.Data.DATA2, "Last.fm Profile");
//        builder.withValue(ContactsContract.Data.DATA3, "View profile");
//        operationList.add(builder.build());
//        try {
//            mContentReceiver.applyBatch(ContactsContract.AUTHORITY, operationList);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        } catch (OperationApplicationException e) {
//            e.printStackTrace();
//        }
        String oldName = "bb";
        String name = "cc";
        String phone = "12345678912";
        String email = "123@123.com";
        String website = "www.123.com";
        String organization = "haha";
        String note = "note";

//        updateContact(this,oldName,name,phone,email,website,organization,note);
        updateContactStatus(3470);
    }


    private void updateContactStatus( long rawContactId) {
        ArrayList<ContentProviderOperation> operationList=new ArrayList<>();
        Uri rawContactUri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, rawContactId);
        Cursor c = mContentReceiver.query(rawContactUri, new String[] { "contact_id" },
                null, null, null);

        if (c.moveToNext()) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.StatusUpdates.CONTENT_URI);
            builder.withValue(ContactsContract.StatusUpdates.DATA_ID, c.getLong(0));
            builder.withValue(ContactsContract.StatusUpdates.STATUS, "ddd");
            builder.withValue(ContactsContract.StatusUpdates.STATUS_RES_PACKAGE, "com.chinasofti.rcs.systemaccount");
            builder.withValue(ContactsContract.StatusUpdates.STATUS_LABEL, R.string.app_name);
            builder.withValue(ContactsContract.StatusUpdates.STATUS_ICON, R.mipmap.ic_launcher_home);
            operationList.add(builder.build());

            if (Integer.decode(Build.VERSION.SDK) < 11) {
                builder = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI);
                builder.withSelection(BaseColumns._ID + " = '" + c.getLong(0) + "'", null);
                builder.withValue(ContactsContract.Data.DATA3, "ddd");
                operationList.add(builder.build());
            }
            try {
                mContentReceiver.applyBatch(ContactsContract.AUTHORITY, operationList);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
        }
    }



//  更新联系人信息
    private void updateContact(Context context, String oldname, String name, String phone, String email, String website, String organization, String note) {
        Cursor cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{ContactsContract.Data.RAW_CONTACT_ID},
                ContactsContract.Contacts.DISPLAY_NAME + "=?", new String[]{oldname}, null);
        cursor.moveToFirst();
        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
        cursor.close();
        ArrayList ops = new ArrayList();
//        更新电话号码
        ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=?" + " AND "+
                        ContactsContract.Data.MIMETYPE + " = ?",new String[] { String.valueOf(id), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE }).
                        withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name).build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {

        }
    }


}
