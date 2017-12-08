package com.chinasofti.rcs.systemaccount;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
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
        try {
//            writeContact();
//            addContact();
//            updateContact();
            addContact("ccc","d");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private  void addContact( String name, String username) {
        Log.i(TAG, "Adding contact: " + name);
        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();

        //Create our RawContact
        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "和飞信");
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "com.chinasofti.rcs.systemaccount.account");
        builder.withValue(ContactsContract.RawContacts.SYNC1, username);
        operationList.add(builder.build());

        //Create a Data record of common type 'StructuredName' for our RawContact
        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        builder.withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
        operationList.add(builder.build());

        //Create a Data record of custom type "vnd.android.cursor.item/vnd.profile" to display a link to the Last.fm profile
        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.com.chinasofti.rcs.systemaccount.profile");
        builder.withValue(ContactsContract.Data.DATA1, username);
        builder.withValue(ContactsContract.Data.DATA2, "Last.fm Profile");
        builder.withValue(ContactsContract.Data.DATA3, "View profile");
        operationList.add(builder.build());

        try {
            mContentReceiver.applyBatch(ContactsContract.AUTHORITY, operationList);
        } catch (Exception e) {
            Log.e(TAG, "Something went wrong during creation! " + e);
            e.printStackTrace();
        }
    }


    private void updateContact() throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
        String name = "飞飞";
        //根据姓名求id
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cursor = mContentReceiver.query(uri, new String[]{ContactsContract.Data._ID},"display_name=?", new String[]{name}, null);
        if(cursor.moveToFirst()){
            int id = cursor.getInt(0);
            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(ContactsContract.StatusUpdates.CONTENT_URI);
            builder.withValue(ContactsContract.StatusUpdates.DATA_ID,id);
            builder.withValue(ContactsContract.StatusUpdates.STATUS, "lpf");
            builder.withValue(ContactsContract.StatusUpdates.STATUS_RES_PACKAGE, "com.chinasofti.rcs.systemaccount");
            builder.withValue(ContactsContract.StatusUpdates.STATUS_LABEL, R.string.app_name);
            builder.withValue(ContactsContract.StatusUpdates.STATUS_ICON, R.mipmap.ic_launcher);
            operationList.add(builder.build());
            mContentReceiver.applyBatch(AUTHORITY,operationList);
        }

    }


    private static String UsernameColumn = ContactsContract.RawContacts.SYNC1;

    private void addContact() throws RemoteException, OperationApplicationException {

        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();

        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI);
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "lpf");
        builder.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "com.lpf.main");
        builder.withValue(UsernameColumn, "阿梦");
        operationList.add(builder.build());

        builder = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI);
        builder.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0);
        builder.withValue(ContactsContract.Data.MIMETYPE, "vnd.android.cursor.item/vnd.com.chinasofti.rcs.systemaccount.profile");
        builder.withValue(ContactsContract.Data.DATA1, "阿梦");
        builder.withValue(ContactsContract.Data.DATA2, "Last.fm Profile");
        builder.withValue(ContactsContract.Data.DATA3, "View profile");
        operationList.add(builder.build());

        //添加名字
        builder = ContentProviderOperation.newInsert(DATA_URI)
                .withValueBackReference(RAW_CONTACT_ID, 0)
                .withValue(MIMETYPE, NAME_ITEM_TYPE)
                .withValue(DISPLAY_NAME, "阿峰")
                ;
        operationList.add(builder.build());


        //添加移动电话
        builder = ContentProviderOperation.newInsert(DATA_URI)
                .withValueBackReference(RAW_CONTACT_ID, 0)
                .withValue(MIMETYPE, PHONE_ITEM_TYPE)
                .withValue(PHONE_NUMBER, "15313112160")
                ;
        operationList.add(builder.build());


        mContentReceiver.applyBatch(ContactsContract.AUTHORITY, operationList);
    }


    private void updateContactStatus(){

    }

    //[content://com.android.contacts/raw_contacts]
    private static final Uri RAW_CONTACTS_URI = ContactsContract.RawContacts.CONTENT_URI;
    private static final String ACCOUNT_TYPE = ContactsContract.RawContacts.ACCOUNT_TYPE;
    private static final String ACCOUNT_NAME = ContactsContract.RawContacts.ACCOUNT_NAME;

    //[content://com.android.contacts/data]
    private static final Uri DATA_URI = ContactsContract.Data.CONTENT_URI;
    private static final String RAW_CONTACT_ID = ContactsContract.Data.RAW_CONTACT_ID;
    private static final String MIMETYPE = ContactsContract.Data.MIMETYPE;
    private static final String NAME_ITEM_TYPE = ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE;
    private static final String DISPLAY_NAME = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME;

    private static final String PHONE_ITEM_TYPE = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE;
    private static final String PHONE_NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    private static final String PHONE_TYPE = ContactsContract.CommonDataKinds.Phone.TYPE;
    private static final int PHONE_TYPE_HOME = ContactsContract.CommonDataKinds.Phone.TYPE_HOME;
    private static final int PHONE_TYPE_MOBILE = ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE;

    private static final String EMAIL_ITEM_TYPE = ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE;
    private static final String EMAIL_DATA = ContactsContract.CommonDataKinds.Email.DATA;
    private static final String EMAIL_TYPE = ContactsContract.CommonDataKinds.Email.TYPE;
    private static final int EMAIL_TYPE_HOME = ContactsContract.CommonDataKinds.Email.TYPE_HOME;
    private static final int EMAIL_TYPE_WORK = ContactsContract.CommonDataKinds.Email.TYPE_WORK;


    private static final String AUTHORITY = ContactsContract.AUTHORITY;

    /**
     * 写联系人
     */
    public void writeContact() throws Exception {
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation operation = ContentProviderOperation.newInsert(RAW_CONTACTS_URI)
                .withValue(ACCOUNT_TYPE, null)
                .withValue(ACCOUNT_NAME, null)
                .build();
        operations.add(operation);

        //添加名字
        operation = ContentProviderOperation.newInsert(DATA_URI)
                .withValueBackReference(RAW_CONTACT_ID, 0)
                .withValue(MIMETYPE, NAME_ITEM_TYPE)
                .withValue(DISPLAY_NAME, "阿峰")
                .build();
        operations.add(operation);

        //添加座机电话
        operation = ContentProviderOperation.newInsert(DATA_URI)
                .withValueBackReference(RAW_CONTACT_ID, 0)
                .withValue(MIMETYPE, PHONE_TYPE_HOME)
                .withValue(PHONE_NUMBER, "031249417656")
                .build();
        operations.add(operation);

        //添加移动电话
        operation = ContentProviderOperation.newInsert(DATA_URI)
                .withValueBackReference(RAW_CONTACT_ID, 0)
                .withValue(MIMETYPE, PHONE_ITEM_TYPE)
                .withValue(PHONE_NUMBER, "15313112160")
                .build();
        operations.add(operation);

        //添加家庭邮箱
        operation = ContentProviderOperation.newInsert(DATA_URI)
                .withValueBackReference(RAW_CONTACT_ID, 0)
                .withValue(MIMETYPE, EMAIL_ITEM_TYPE)
                .withValue(EMAIL_TYPE, EMAIL_TYPE_HOME)
                .withValue(EMAIL_DATA, "scott@android.com")
                .build();
        operations.add(operation);

        //添加工作邮箱
        operation = ContentProviderOperation.newInsert(DATA_URI)
                .withValueBackReference(RAW_CONTACT_ID, 0)
                .withValue(MIMETYPE, EMAIL_ITEM_TYPE)
                .withValue(EMAIL_TYPE, EMAIL_TYPE_WORK)
                .withValue(EMAIL_DATA, "scott@msapple.com")
                .build();
        operations.add(operation);


        //批量执行,返回执行结果集
        ContentProviderResult[] results = mContentReceiver.applyBatch(AUTHORITY, operations);
        for (ContentProviderResult result : results) {
            Log.i(TAG, result.uri.toString());
        }

    }


    private void updateContactStatus(long rawContactId) {
        ArrayList<ContentProviderOperation> operationList = new ArrayList<>();
        Uri rawContactUri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, rawContactId);
        Cursor c = mContentReceiver.query(rawContactUri, new String[]{"contact_id"},
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
                .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=?" + " AND " +
                        ContactsContract.Data.MIMETYPE + " = ?", new String[]{String.valueOf(id), ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE}).
                        withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name).build());

        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {

        }
    }


}
