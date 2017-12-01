package com.chinasofti.rcs.systemaccount;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.chinasofti.rcs.systemaccount.displaycontact.PhoneNumber;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Uri intentData = getIntent().getData();
        if (isNotEmpty(intentData)) {
            Cursor cursor = managedQuery(intentData, null, null, null, null);
            if (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex("DATA1"));
                String number = cursor.getString(cursor.getColumnIndex("DATA2"));
                TextView view = (TextView) findViewById(R.id.profiletext);
                view.setText("和飞信");
                doSomething(getPhoneNumber(number));
            }
        } else {
//            handleIntentWithoutData();
        }

    }

    private boolean isNotEmpty(Object o){
        if (o!=null){
            return true;
        }
        return false;
    }

    private PhoneNumber getPhoneNumber(String number){
        PhoneNumber phoneNumber=new PhoneNumber();
        phoneNumber.setNumber(number);
        return  phoneNumber;
    }
    private void doSomething(PhoneNumber phoneNumber) {
        Uri uri = Uri.parse("tel:" + phoneNumber.getNumber());
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivity(intent);
    }
}
