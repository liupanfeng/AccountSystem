package com.chinasofti.rcs.systemaccount;

import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ContactTools {

//    //根据电话号码查询姓名（在一个电话打过来时，如果此电话在通讯录中，则显示姓名）
//    public void testReadNameByPhone(){
//        String phone = "12345678";
//        //uri=  content://com.android.contacts/data/phones/filter/#
//        Uri uri = Uri.parse("content://com.android.contacts/data/phones/filter/"+phone);
//        ContentResolver resolver = this.getContext().getContentResolver();
//        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data.DISPLAY_NAME}, null, null, null); //从raw_contact表中返回display_name
//        if(cursor.moveToFirst()){
//            Log.i("Contacts", "name="+cursor.getString(0));
//        }
//    }


//    //读取通讯录的全部的联系人
////需要先在raw_contact表中遍历id，并根据id到data表中获取数据
//    public void testReadAll(){
//        //uri = content://com.android.contacts/contacts
//        Uri uri = Uri.parse("content://com.android.contacts/contacts"); //访问raw_contacts表
//        ContentResolver resolver = this.getContext().getContentResolver();
//        Cursor cursor = resolver.query(uri, new String[]{Data._ID}, null, null, null);  //获得_id属性
//        while(cursor.moveToNext()){
//            StringBuilder buf = new StringBuilder();
//            int id = cursor.getInt(0);//获得id并且在data中寻找数据
//            buf.append("id="+id);
//            uri = Uri.parse("content://com.android.contacts/contacts/"+id+"/data"); //如果要获得data表中某个id对应的数据，则URI为content://com.android.contacts/contacts/#/data
//            Cursor cursor2 = resolver.query(uri, new String[]{Data.DATA1,Data.MIMETYPE}, null,null, null);  //data1存储各个记录的总数据，mimetype存放记录的类型，如电话、email等
//            while(cursor2.moveToNext()){
//                String data = cursor2.getString(cursor2.getColumnIndex("data1"));
//                if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/name")){       //如果是名字
//                    buf.append(",name="+data);
//                }
//                else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/phone_v2")){  //如果是电话
//                    buf.append(",phone="+data);
//                }
//                else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/email_v2")){  //如果是email
//                    buf.append(",email="+data);
//                }
//                else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/postal-address_v2")){ //如果是地址
//                    buf.append(",address="+data);
//                }
//                else if(cursor2.getString(cursor2.getColumnIndex("mimetype")).equals("vnd.android.cursor.item/organization")){  //如果是组织
//                    buf.append(",organization="+data);
//                }
//            }
//            String str = buf.toString();
//            Log.i("Contacts", str);
//        }
//    }
}
