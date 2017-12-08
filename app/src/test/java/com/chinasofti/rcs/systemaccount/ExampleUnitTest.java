package com.chinasofti.rcs.systemaccount;

import android.content.ContentProviderOperation;
import android.net.Uri;
import android.provider.ContactsContract;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    //[content://com.android.contacts/raw_contacts]
    private static final Uri RAW_CONTACTS_URI = ContactsContract.RawContacts.CONTENT_URI;
    //[content://com.android.contacts/data]
    private static final Uri DATA_URI = ContactsContract.Data.CONTENT_URI;

    private static final String ACCOUNT_TYPE = ContactsContract.RawContacts.ACCOUNT_TYPE;
    private static final String ACCOUNT_NAME = ContactsContract.RawContacts.ACCOUNT_NAME;

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

    @Test
    public void addition_isCorrect() throws Exception {

    }

    @Test
    public void testContact()throws Exception{
        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        ContentProviderOperation operation = ContentProviderOperation.newInsert(RAW_CONTACTS_URI)
                .withValue(ACCOUNT_TYPE, null)
                .withValue(ACCOUNT_NAME, null)
                .build();
        operations.add(operation);

        //添加联系人名称操作
        operation = ContentProviderOperation.newInsert(DATA_URI)
                .withValueBackReference(RAW_CONTACT_ID, 0)
                .withValue(MIMETYPE, NAME_ITEM_TYPE)
                .withValue(DISPLAY_NAME, "Scott Liu")
                .build();
        operations.add(operation);

    }
}