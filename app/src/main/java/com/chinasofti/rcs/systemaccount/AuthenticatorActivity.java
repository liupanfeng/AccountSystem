package com.chinasofti.rcs.systemaccount;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/11/30.
 */

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public static final String ACCOUNT_TYPE = "com.chinasofti.rcs.systemaccount.account"; //必须与xml中的type保持一致

    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);

        setContentView(R.layout.activity_main);

        mAccountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Account[] accounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length>0){
            Toast.makeText(this,"已添加当前登录的账户",Toast.LENGTH_SHORT).show();
            finish();
        }

        Button btn_login=findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account=new Account(getString(R.string.app_name),ACCOUNT_TYPE);
                mAccountManager.addAccountExplicitly(account,"123456",null);

//                // 自动同步
                Bundle bundle= new Bundle();
                ContentResolver.setIsSyncable(account, AccountProvider.AUTHORITY, 1);
                ContentResolver.setSyncAutomatically(account, AccountProvider.AUTHORITY,true);
                ContentResolver.addPeriodicSync(account, AccountProvider.AUTHORITY,bundle, 30);    // 间隔时间为30秒

                // 手动同步
                //ContentResolver.requestSync(account, AccountProvider.AUTHORITY, bundle);

                finish();
            }
        });

    }


}
