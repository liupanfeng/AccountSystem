package com.chinasofti.rcs.systemaccount;

import android.accounts.Account;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chinasofti.rcs.systemaccount.displaycontact.ContactInfo;
import com.chinasofti.rcs.systemaccount.displaycontact.ContactsSyncAdapterService;
import com.chinasofti.rcs.systemaccount.displaycontact.PhoneNumber;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * 12.1
     * @param contactInfo
     */
    private void synchronizeContact(ContactInfo contactInfo) {
        ContactsSyncAdapterService syncAdapter = new ContactsSyncAdapterService();
        Account account = new Account(contactInfo.getDisplayName(), getString(R.string.account_type)); //account_type = <yourpackage>.account
        PhoneNumber phoneNumber = contactInfo.getPhoneNumbers().get(0);
        syncAdapter.performSync(MainActivity.this, account, phoneNumber);
    }
}
