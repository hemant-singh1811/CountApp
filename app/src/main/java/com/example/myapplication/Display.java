package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.VibrationEffect;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.myapplication.databinding.ActivityMainBinding;

import android.view.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.function.LongFunction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.os.Vibrator;
import java.util.Calendar;
import java.util.Date;


public class Display extends AppCompatActivity {

    int i=0;
    String sPref="";
    String totalmaingroupmember="48";
    String totalmaingrouppresentmember="0";
    String totalmaingroupabsentmember="0";
    String totallearninggroupmember="17";
    String totalmainlearningpresentmember="0";
    String totallearninggroupabsentmember="0";
    HashSet<String> absentlist=new HashSet<>();
    String todaydate="";
    boolean Switchbuttonstate=false;
    String tag="DisplayActivityTag";

    class updatecontact extends Thread{

        String sPref;
        View v;

        updatecontact(View v,String sPref){
            this.v=v;
            this.sPref=sPref;
        }

        public void run() {
            try {

                SharedPreferences sharedPreferences = getSharedPreferences(sPref,MODE_PRIVATE);

                long s=System.currentTimeMillis();

                displaysnakbar(v,"contact updating....");

                vibrate(100);

                Log.d(tag,"contact updating started");

                List[] list=getlistofbothgroup();

                if(sPref.equals("MainGroupSharedPref")){
                     print("main group button tapped");
                  List<ContactModel> mainbhagtcontacts=list[0];

                  putintoSharedPre(mainbhagtcontacts,sharedPreferences);
                }
                else {
                    List<ContactModel> learningbhagat=list[1];
                    putintoSharedPre(learningbhagat, sharedPreferences);
                }
                long e=System.currentTimeMillis();

                e=e-s;
                e=(int) e/1000;

               displaysnakbar(v,"Contact Updated in "+e+"s");

                vibrate(1000);
                i--;

            }catch (Exception e){
                Log.d(tag,"error"+e.toString());
                i--;
            }

        }

    }

    public  class ContactModel {
        public String id;
        public String sno;
        public String name;
        public String mobileNumber;
        public Bitmap photo;
        public Uri photoURI;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_display);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("sPref");
            Log.d("DisplayActiviy",value+" this is ");
            sPref=value;
        }

        View view = View.inflate(this, R.layout.activity_display, null);

        updatecontact(view);

        SharedPreferences sharedPreferences = getSharedPreferences(sPref,MODE_PRIVATE);

        totalmaingroupmember = sharedPreferences.getString("totalmaingroupmember", "0");

        setText(sharedPreferences);

        Button button = (Button) findViewById(R.id.button_first);
        EditText count = (EditText) findViewById(R.id.count);
        EditText countstrings = (EditText) findViewById(R.id.countstring);
        TextView cross=(TextView) findViewById(R.id.cross);
        TextView countabsent=(TextView) findViewById(R.id.countabsent);
        TextView result=(TextView) findViewById(R.id.Scrollresult);
        Button getreport=(Button) findViewById(R.id.getreport);
        Button getabsent=(Button) findViewById(R.id.absent);
        TextView lastupdatetime=(TextView) findViewById(R.id.lastupdatetime);

        displaydate();

        String s="println";

        getabsent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] str = {"इन सेवादारों ने आज सेवा नही की हैं", "date"};
                int[] gap = {0};
                String resultstr="";
                int i=0;
                String alert="🚨";
                for (String x : str) {
                    if(x=="date"){
                        resultstr+="*("+todaydate+")"+alert+"*";
                    }else{
                        resultstr += " *" + x + "*";

                        if (i < gap.length) {
                            int gapline = gap[i];
                            resultstr += "\n";
                            while (gapline-- > 0) {
                                resultstr += "\n";
                            }
                        }
                        i++;
                    }
                }

                resultstr+="\n \n \n";

                int len=absentlist.size();
                Log.d(tag,"len : "+len+"");

                int count=0;
                if(len!=0) {
                    for (String x : absentlist) {
                        count++;
                        print("name",x);
                        resultstr+=""+count+".";
                        String trimmedstr=trimstr(x);
                        print("name",trimmedstr);
                        Log.d(tag,"trim "+trimmedstr);

                        resultstr+=trimmedstr;
                        resultstr+=" \n"+"\n";
                    }
                }

                copyingdata(resultstr);
                vibrate(300);
            }
        });

        getreport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] arr={"बन्दीछोड सतगुरु रामपाल जी महाराज जी की जय हो🙏","🌹 देवली+ संगम विहार सोशल मीडिया सेवा🌹","date","की सेवा का विवरण","जिन भगतात्माओ ने सेवा की है।","Total members        ➡","totalmember","PRESENT.                 ➡","presentmember","ABSENT.                   ➡","absentmember","Note:- सभी भगतात्माओ से प्रार्थना है सेवा में बढ़-चढ़कर सहयोग करें।","🙏 सत साहेब जी 🙏"};

                int[] gap={0,1,2,1,1,2,1,1};

                String resultstr="";
                int i=0;
                int j=0;
                String[] groupcounting=new String[3];

                if (!Switchbuttonstate) {
                    groupcounting[0]= totalmaingroupmember;
                    groupcounting[1]= totalmaingrouppresentmember;
                    groupcounting[2]= totalmaingroupabsentmember;
                }else{
                    groupcounting[0]= totallearninggroupmember;
                    groupcounting[1]= totalmainlearningpresentmember;
                    groupcounting[2]= totallearninggroupabsentmember;
                }

                for(String x:arr){
                    if(x=="date"){
                        resultstr+="*"+todaydate+"*";
                    }
                    else if(x.equals("totalmember")){
                        String emoji=getemoji(groupcounting[0]);
                        resultstr+="*"+emoji+"*"+"\n"+"\n";
                    }else if(x.equals("presentmember")){
                        String emoji=getemoji(groupcounting[1]);
                        resultstr+="*"+emoji+"*"+"\n"+"\n";
                    }else if(x=="absentmember"){
                        String emoji=getemoji(groupcounting[2]);
                        resultstr+="*"+emoji+"*"+"\n"+"\n"+"\n";
                    }
                    else {
                        if(i==1 && Switchbuttonstate)
                            x="🌹 देवली + संगम विहार सोशल मीडिया लर्निंग ग्रुप सेवा🌹";
                        resultstr += " *" + x + "*";


                        if(j==5 || j==7 || j==9){
                            resultstr+= " ";
                        }
                        else if (i < gap.length) {
                            int gapline = gap[i];
                            resultstr+= "\n";
                            while (gapline > 0) {
                                resultstr += "\n";
                                gapline--;
                            }
                            i++;
                        }
                    }
                    j++;
                }

                copyingdata(resultstr);
                vibrate(300);

            }
        });

        count.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String updatedcount=count.getText().toString();

                Log.d(tag+"23","textchanged"+updatedcount);

                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    totalmaingroupmember=updatedcount;
                    myEdit.putString("totalmaingroupmember", updatedcount);
                    myEdit.commit();

                Toast t1=Toast.makeText(getApplicationContext(),"Updated", Toast.LENGTH_SHORT);
                t1.show();

            }
        });

        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                try {
                    String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                    String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                    String lastupdatetimestr=currentDate+" "+currentTime;

                    lastupdatetime.setText(lastupdatetimestr.toString());

                    String a = count.getText().toString();
                    int total = Integer.parseInt(a);

                    String enteredstring = countstrings.getText().toString();
                    String[] lines = enteredstring.split("%");

                    absentlist=new HashSet<>();

                    int[] arr = new int[total];

                    for (String x : lines) {
                        int c = Integer.parseInt(x);

                        if (c <= total)
                            arr[c - 1] = 1;
                        else
                        {
                            Toast.makeText(Display.this, "Number greater than total", Toast.LENGTH_SHORT).show();
                            vibrate(500);
                        }
                    }

                    String ans = "";
                    int absentbhagt = 0;

                        SharedPreferences.Editor myEdit = sharedPreferences.edit();

                        myEdit.putString("mainlastupdate",lastupdatetimestr.toString());
                        myEdit.putString("inputstring", enteredstring);

                        myEdit.commit();

                        for (int i = 0; i < total; i++) {
                            if (arr[i] == 0) {
                                String sno = (i + 1) + "";
                                String s1 = sharedPreferences.getString(sno, "defvalue");
                                absentlist.add(s1);
                                Log.d(tag,"button : "+s1);
                                absentbhagt++;
                                ans += s1 + "\n";
                            }
                        }
                        totalmaingrouppresentmember=""+(total-absentbhagt);
                        totalmaingroupabsentmember=""+absentbhagt;

                        totalmaingrouppresentmember= formatstr(totalmaingroupmember,totalmaingrouppresentmember);
                        totalmaingroupabsentmember= formatstr(totalmaingroupmember,totalmaingroupabsentmember);

                    result.setText(ans.toString());
                    countabsent.setText(absentbhagt + "");

                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

                    if (imm.isAcceptingText()) {
                        hidekeyboard();
                        Log.d(tag,"Software Keyboard was shown");
                    } else {
                        Log.d(tag,"Software Keyboard was not shown");
                    }
                }
                catch (Exception e) {
                    Toast.makeText(Display.this, "Please enter correct", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

                    if (imm.isAcceptingText()) {
                        hidekeyboard();
                        Log.d(tag,"Software Keyboard was shown");
                    } else {
                        Log.d(tag,"Software Keyboard was not shown");
                    }

                }
            }
        });

        result.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();

                int duration = Toast.LENGTH_SHORT;

                displaytoast("copied");

                Toast toast1 = Toast.makeText(context,"copied", duration);
                toast1.show();

                String getstring = result.getText().toString();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", getstring);
                clipboard.setPrimaryClip(clip);
                vibrate(300);

            }
        });

        cross.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                countstrings.setText("");
                result.setText("");
                countabsent.setText("");
            }
        });
    }

    public void displaysnakbar(View view,String data){
        Snackbar snk1= Snackbar.make(view, data, Snackbar.LENGTH_SHORT)
                .setAction("Action", null);
        snk1.show();

    }

    public String formatstr(String total, String modify) {

        int len1=total.length();
        int len2=modify.length();

        if(len1==len2) return modify;

        String zero="";
        int missing=len1-len2;

        while(missing-->0){
            zero+="0";
        }
        modify=zero+modify;
        return modify;

    }

    private void displaydate() {
//        TextView date = (TextView) findViewById(R.id.date);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Date currentTime = Calendar.getInstance().getTime();
        todaydate=""+currentDate.toString();
//        date.setText(currentDate.toString());
    }

    public void vibrate(int duration){
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 2000 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vib.vibrate(duration);
        }
    }

    public void putintoSharedPre(List<ContactModel> bhagtcontact, SharedPreferences Obj){

        SharedPreferences.Editor myEdit = Obj.edit();

        for(ContactModel contact:bhagtcontact){

            myEdit.putString(""+contact.sno, contact.name.toString());

        }
        myEdit.commit();
    }

    public void hidekeyboard(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public String getemoji(String totalmember) {

        Log.d(tag,totalmember);

        String[] str={"0️⃣","1️⃣", "2️⃣", "3️⃣","4️⃣", "5️⃣", "6️⃣", "7️⃣" ,"8️⃣","9️⃣"};

        String result="";

        for(int i=0;i<totalmember.length();i++) {
            int c=Integer.parseInt(totalmember.charAt(i)+"");
            result+=""+str[c];
            if(totalmember.length()==1 && totalmember.charAt(0)=='0') {
                result+=""+str[c];
            }
        }

        Log.d(tag,result);
        return  result;
    }

    public void print(String data){
        Log.d(tag,data);
    }

    public void print(String extratag,String data) {
        Log.d(tag+extratag,data);
    }

    public  List[] getlistofbothgroup(){

        try {
        List<ContactModel> contacts= getContacts(this);

        List<ContactModel> bhagatcontactslist=new ArrayList<>();
        HashMap<String,Integer> mainmap=new HashMap<>();
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
                        x.sno=""+getsno(arr[0]);
                        learningcontactslist.add(x);
                    }else{
                        if(!mainmap.containsKey(x.mobileNumber)){
                            String[] arr = name.split(" .");
                            x.sno=""+getsno(arr[0]);
                            bhagatcontactslist.add(x);
                            mainmap.put(x.mobileNumber,1);
                        }

                    }
                }

            }
            catch (Exception e){
                Toast.makeText(this, "Error occur Clicked", Toast.LENGTH_SHORT).show();
                Log.d(tag,"string split error ocoor");
            }
        }
        list[0]=new ArrayList<>(bhagatcontactslist);
        list[1]=new ArrayList<>(learningcontactslist);

        return list;
        }catch (Exception e)
        {
            throw  e;
        }
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

    public String trimstr(String str){

        String resultstr="";
        boolean canstart=false;

        for(int i=0;i<str.length()-1;i++){
            char c=str.charAt(i);
            if(canstart){
                String ct=""+str.charAt(i)+str.charAt(i+1);
                if(ct.equals("SV")) {
                    Log.d(tag+"trim","ct : "+ct);

                    return resultstr;
                }
                resultstr+=c;
            }
            if(!Switchbuttonstate) {
                if (c == '.') canstart = true;
            }else {
                String ct=""+str.charAt(i)+str.charAt(i+1);
                if(ct.equals("S2")) {
                    canstart = true;
                    i++;
                }
            }
        }
        return resultstr;
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

    public List<ContactModel> getContacts(Context ctx) {
        try {

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
                            info.id = id;
                            info.sno = "";
                            info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            info.photo = photo;
                            info.photoURI = pURI;
                            list.add(info);
                        }

                        cursorInfo.close();
                    }
                }
                cursor.close();
            }

        return list;

        }catch (SecurityException e){
            print("e : ",e.toString());
            return null;
//            throw e;
        }
    }

    public void copyingdata(String displaystring){

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", displaystring);
        clipboard.setPrimaryClip(clip);

    }

    public void displaytoast(String displaytext){
        Toast t1=Toast.makeText(getApplicationContext(),displaytext, Toast.LENGTH_SHORT);
        t1.show();
    }

    public void updatecontact(View v){

        if(i==0) {
            i++;
            updatecontact updatecontactobj = new updatecontact(v,sPref);

            updatecontactobj.start();
        }

    }

    public void setText(SharedPreferences sPref){

        EditText count=(EditText) findViewById(R.id.count);
        EditText countstrings = (EditText) findViewById(R.id.countstring);
        TextView lastupdatetime=(TextView) findViewById(R.id.lastupdatetime);

            String inputstring= sPref.getString("inputstring", "deaf");

            count.setText(totalmaingroupmember);
            countstrings.setText(inputstring);

            String res=sPref.getString("mainlastupdate", "last-update");

            lastupdatetime.setText(res.toString());

    }

}

