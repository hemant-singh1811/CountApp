package com.example.myapplication;

import static java.lang.Character.isDigit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public  class  lists extends AppCompatActivity {

    String tag="listActivityTag";

    public  class ContactModel {
        public String id;
        public String sno;
        public String name;
        public String mobileNumber;
        public Bitmap photo;
        public Uri photoURI;
    }

    public List<ContactModel> getContacts(Context ctx) {
        List<ContactModel> list = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(ctx.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    while (cursorInfo.moveToNext()) {
                        ContactModel info = new ContactModel();
//                        Log.d("contact",info)
                        info.id = id;
                        info.sno="";
                        info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        info.photo = photo;
                        info.photoURI= pURI;
                        list.add(info);
                    }

                    cursorInfo.close();
                }
            }
            cursor.close();
        }
        return list;
    }

    public static int getsno(String str) {

        String ans="";

        for(int i=0;i<str.length();i++) {

            char c=str.charAt(i);

            if(c=='.') return Integer.parseInt(ans);

            ans+=c;

        }
        return  Integer.parseInt(ans);
    }

    public boolean isbelongtolearninggroup(String name){

        int len=name.length();

        for(int i=0;i<len-1;i++){

            String learninggrouptag= name.charAt(i)+""+name.charAt(i+1);

//            Log.d(tag+"11",learninggrouptag);

            if(learninggrouptag.equals("S2")){
//                Log.d(tag+"11","learning group member found");
                return true;
            }
        }
        return false;
    }


    public  List<ContactModel> getmainbhagtcontactslist(){
        List<ContactModel> contacts= getContacts(lists.this);

//        Collections.sort(contacts, (a, b) -> {
//            String a1=a.name;
//            String b1=b.name;
//
//            if(a1.compareTo(b1)>=0) return 1;
//            else return -1;
//        });

            List<ContactModel> bhagatcontactslist=new ArrayList<>();


        for(ContactModel x:contacts) {
                String name=x.name;

                if(name.length()<=2) continue;
                String end= name.substring(name.length()-2);

//                Log.d(tag,end);
//                Log.d(tag,str);
//                Log.d(tag,"digit : "+Character.isDigit(str.charAt(0)));
//  if(Character.isDigit(name.charAt(0)) && end.equals("SV") && isbelongtolearninggroup(name)){
                if(Character.isDigit(name.charAt(0)) && end.equals("SV")){
                    try {
                        if(!isbelongtolearninggroup(name)){
                            String[] arr = name.split(" .");

                            int sno=getsno(arr[0]);

//                        Log.d(tag,"sno - "+sno);
                            x.sno=""+sno;
                            bhagatcontactslist.add(x);
                        }
                        }
                    catch (Exception e){
                        Log.d(tag,"string split error ocoor");
                    }

//                    Log.d(tag, x.mobileNumber.toString());
                }
            }


        return bhagatcontactslist;
    }


    public  List<ContactModel> getlearningbhagtcontactslist(){

        List<ContactModel> contacts= getContacts(lists.this);


//        Collections.sort(contacts, (a, b) -> {
//            String a1=a.name;
//            String b1=b.name;
//
//            if(a1.compareTo(b1)>=0) return 1;
//            else return -1;
//        });

        List<ContactModel> bhagatcontactslist=new ArrayList<>();



        for(ContactModel x:contacts) {
            String name=x.name;

            if(name.length()<=2) continue;
            String end= name.substring(name.length()-2);

//                Log.d(tag,end);
//                Log.d(tag,str);
//                Log.d(tag,"digit : "+Character.isDigit(str.charAt(0)));
//
            if(Character.isDigit(name.charAt(0)) && end.equals("SV") && isbelongtolearninggroup(name)){
                try {
                    String[] arr = name.split(" .");

                    int sno=getsno(arr[0]);

//                    Log.d(tag,"sno - "+sno);
                    x.sno=""+sno;

                    bhagatcontactslist.add(x);
                }catch (Exception e){
                    Log.d(tag,"string split error ocoor");
                }
//                    Log.d(tag, x.mobileNumber.toString());
            }
        }
        return bhagatcontactslist;
    }

    public  List[] getlistofbothgroup(){

        List<ContactModel> contacts= getContacts(lists.this);

        List<ContactModel> bhagatcontactslist=new ArrayList<>();
        List<ContactModel> learningcontactslist=new ArrayList<>();

        List[] list=new List[2];

        for(ContactModel x:contacts) {
            String name=x.name;
            try {
            if(name.length()<=2) continue;
            String end= name.substring(name.length()-2);

        if(Character.isDigit(name.charAt(0)) && end.equals("SV")){


                    if(isbelongtolearninggroup(name)){

                        String[] arr = name.split(" .");

                        int sno=getsno(arr[0]);

                        x.sno=""+sno;

                        learningcontactslist.add(x);


                    }else{
                        String[] arr = name.split(" .");

                        int sno=getsno(arr[0]);

//                        Log.d(tag,"sno - "+sno);
                        x.sno=""+sno;
                        bhagatcontactslist.add(x);
                    }
//                    Log.d(tag, x.mobileNumber.toString());
            }

            }
            catch (Exception e){

                Toast.makeText(lists.this, "Error occur Clicked", Toast.LENGTH_SHORT).show();


                Log.d(tag,"string split error ocoor");
            }
        }

        list[0]=bhagatcontactslist;
        list[1]=learningcontactslist;

        return list;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        Button backbtn=(Button) findViewById(R.id.backbtn);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button btn=(Button) findViewById(R.id.contact);

        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int duration = Toast.LENGTH_SHORT;

                Log.d(tag,"press butn to get contack");

                Toast toast = Toast.makeText(getApplicationContext(), "clicked", duration);
                toast.show();

                btn.setEnabled(false);

//                List<ContactModel> allcontacts=getContacts(lists.this);

                List[] list=getlistofbothgroup();

                List<ContactModel> mainbhagtcontacts=list[0];

                List<ContactModel> learningbhagat=list[1];

                SharedPreferences mainGroupsharedPreferences = getSharedPreferences("MainGroupSharedPref",MODE_PRIVATE);

                SharedPreferences leaninggroupbhaktshreadPreferences= getSharedPreferences("LearningGroupSharedPref",MODE_PRIVATE);

                putintoSharedPre(mainbhagtcontacts,mainGroupsharedPreferences);

                putintoSharedPre(learningbhagat,leaninggroupbhaktshreadPreferences);

                Snackbar.make(view, "Contact Updated", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                btn.setEnabled(true);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(tag,"pause");

        Toast t1=Toast.makeText(this,"pause", Toast.LENGTH_SHORT);

        t1.show();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(tag,"stop");

        Toast t1=Toast.makeText(this,"stop", Toast.LENGTH_SHORT);

        t1.show();
    }

    public void putintoSharedPre(List<ContactModel> bhagtcontact, SharedPreferences Obj){

        SharedPreferences.Editor myEdit = Obj.edit();

        for(ContactModel contact:bhagtcontact){

            myEdit.putString(""+contact.sno, contact.name.toString());

        }
        myEdit.commit();
    }
}

