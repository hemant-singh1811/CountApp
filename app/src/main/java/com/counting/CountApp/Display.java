package com.counting.CountApp;

import static android.Manifest.permission.READ_CONTACTS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.os.VibrationEffect;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.os.Vibrator;

import java.util.Random;

public class Display extends AppCompatActivity {

    String sPref = "";
    String totalMember = "48";
    String presentMember = "0";
    String absentMember = "0";
    String identifier = "";
    String separator = "";
    String[] absentMemberList = new String[0];
    boolean Switchbuttonstate = false;
    String prevActivity = "";
    String tag = "DisplayActivityTag";
    SharedPreferences sharedPreferences = null;
    SharedPreferences sharedPreferencesContactsGroup;
    int REQ_CODE = 200;
    View view = null;
    String area = "";
    UpdateContacts updatecontactobj;
    String[] absentReportFormat = {"इन सेवादारों ने आज सेवा नही की हैं", "date"};

    //        String[] reportFormat = {"🙏बन्दीछोड सतगुरु रामपाल जी महाराज जी की जय हो🙏", "🌹 अंबेडकर नगर सोशल मीडिया सेवा🌹", "{date}", "की सेवा का विवरण", "जिन भगतात्माओ ने सेवा की है।", "Total members        ➡", "{totalmember}", "PRESENT.                 ➡", "{presentmember}", "ABSENT.                   ➡", "{absentmember}", "Note:- सभी भगतात्माओ से प्रार्थना है सेवा में बढ़-चढ़कर सहयोग करें।", "🙏 सत साहेब जी 🙏"};
    String[] reportFormat = {"🙏बन्दीछोड सतगुरु रामपाल जी महाराज जी की जय हो🙏", "{area}", " सोशल मीडिया सेवा", "{date}", "की सेवा का विवरण", "जिन भगतात्माओ ने सेवा की है।", "Total members        ➡", "{totalMember}", "PRESENT.                 ➡", "{presentMember}", "ABSENT.                   ➡", "{absentMember}", "Note:- सभी भगतात्माओ से प्रार्थना है सेवा में बढ़-चढ़कर सहयोग करें।", "🙏 सत साहेब जी 🙏"};
    String[] reportFormat1Test = {"🙏बन्दीछोड सतगुरु रामपाल जी महाराज जी की जय हो🙏", "🌹 देवली+ संगम विहार सोशल मीडिया सेवा🌹", "{date}", "की सेवा का विवरण", "जिन भगतात्माओ ने सेवा की है।", "Total members        ➡", "{totalMember}", "PRESENT.                 ➡", "{presentMember}", "ABSENT.                   ➡", "{absentMember}", "Note:- सभी भगतात्माओ से प्रार्थना है सेवा में बढ़-चढ़कर सहयोग करें।", "🙏 सत साहेब जी 🙏"};


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_display);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sPref = extras.getString("sPref");
            prevActivity = extras.getString("prev");
        }

        setSharedPreferences();

        view = getWindow().getDecorView().getRootView();

        ImageView convertSnoToNameButton = (ImageView) findViewById(R.id.convertSnoToName);
        EditText count = (EditText) findViewById(R.id.count);
        EditText countstrings = (EditText) findViewById(R.id.inputHashText);
        ImageView cross = (ImageView) findViewById(R.id.cross);
        TextView countabsent = (TextView) findViewById(R.id.countabsent);
        TextView result = (TextView) findViewById(R.id.Scrollresult);
        ImageView getReportButton = (ImageView) findViewById(R.id.getreport);
        ImageView getAbsentButton = (ImageView) findViewById(R.id.getabsent);
        TextView lastupdatetime = (TextView) findViewById(R.id.lastupdatetime);
        ImageView resizeButton = (ImageView) findViewById(R.id.resizeText);
        ImageView pasteButton = (ImageView) findViewById(R.id.pasteButton);
        ImageView updateContactsButton = (ImageView) findViewById(R.id.refreshbutton);
        ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.contactsUpdateBar);

        if (prevActivity != null) {
            updateConfig(view);
        }


        resizeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputStr = countstrings.getText().toString();
                resizedText(inputStr);
                vibrate(100);
            }
        });

        getAbsentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int[] gap = {0};
                    int i = 0;
                    int absentCount = 1;
                    StringBuilder resultantReport = new StringBuilder();
                    String alert = "🚨";

                    for (String reportLine : absentReportFormat) {
                        if (reportLine.equals("date")) {
                            resultantReport.append("*(").append(Helper.getDate()).append(")").append(alert).append("*");
                        } else {
                            resultantReport.append(" *").append(reportLine).append("*");

                            if (i < gap.length) {
                                int gapLine = gap[i];
                                resultantReport.append("\n");
                                while (gapLine-- > 0) {
                                    resultantReport.append("\n");
                                }
                            }
                            i++;
                        }
                    }
                    resultantReport.append("\n \n \n");

                    for (int j = 1; j < absentMemberList.length; j++) {
                        String absentMemberName = absentMemberList[j];

                        if (absentMemberName.equals("null")) continue;

                        String trimmedName = trimstr(absentMemberName);

                        resultantReport.append("").append(absentCount++).append(".").append(trimmedName).append("\n \n");

                    }

                    copyData(resultantReport.toString());
                    vibrate(100);
                    displayToast("copied");
                } catch (Exception e) {
                    Log.d(tag, "onClick: " + e.toString());
                    displayToast("Something went wrong");
                }

            }
        });

        getReportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String reportFormatString = sharedPreferences.getString(Helper.ReportFormat, "null");

                if (!reportFormatString.equals("null"))
                    reportFormat1Test = reportFormatString.split("\n");

                int[] gap = {0, 1, 2, 1, 1, 2, 1, 1};

                StringBuilder resultantReport = new StringBuilder();
                int i = 0;
                int j = 0;

                for (int line = 0; line < reportFormat.length; line++) {
                    String reportLine = reportFormat[line];

                    switch (reportLine) {
                        case "{date}":
                            resultantReport.append("*").append(Helper.getDate()).append("*");
                            break;
                        case "{totalMember}": {
                            String emoji = getNumericEmoji(totalMember);
                            resultantReport.append("*").append(emoji).append("*").append("\n").append("\n");
                            break;
                        }
                        case "{presentMember}": {
                            String emoji = getNumericEmoji(presentMember);
                            resultantReport.append("*").append(emoji).append("*").append("\n").append("\n");
                            break;
                        }
                        case "{absentMember}": {
                            String emoji = getNumericEmoji(absentMember);
                            resultantReport.append("*").append(emoji).append("*").append("\n").append("\n").append("\n");
                            break;
                        }
                        case "{area}": {
                            resultantReport.append("*🌹").append(area).append(reportFormat[line + 1].toString()).append("🌹*").append("\n").append("\n");
                            line++;
                            break;
                        }
                        default:
                            if (i == 1 && Switchbuttonstate)
                                reportLine = "🌹 देवली + संगम विहार सोशल मीडिया लर्निंग ग्रुप सेवा🌹";

                            resultantReport.append(" *").append(reportLine).append("*");

                            if (j == 5 || j == 7 || j == 9) {
                                resultantReport.append(" ");
                            } else if (i < gap.length) {
                                int gapline = gap[i];
                                resultantReport.append("\n");
                                while (gapline > 0) {
                                    resultantReport.append("\n");
                                    gapline--;
                                }
                                i++;
                            }
                            break;
                    }
                    j++;
                }

                copyData(resultantReport.toString());
                vibrate(100);
                displayToast("copied");

            }
        });

        count.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                totalMember = count.getText().toString();

                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString(Helper.TotalMember, totalMember);
                myEdit.apply();

                vibrate(100);

            }
        });

        pasteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String currentInputString = countstrings.getText().toString();

                    ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                    ClipData clipData = myClipboard.getPrimaryClip();

                    String copiedText = clipData.getItemAt(0).coerceToText(getApplicationContext()).toString();

                    currentInputString += copiedText;

                    countstrings.setText(currentInputString);

                    countstrings.setSelection(countstrings.getText().length());

                    vibrate(100);
                } catch (Exception e) {
                    Log.d(tag, "pasteButton onClick: " + e.toString());
                }

            }
        });

        convertSnoToNameButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    vibrate(100);

                    int total = Integer.parseInt(count.getText().toString());
                    String inputHashText = countstrings.getText().toString();
                    String[] lines = new String[0];
                    int[] arr = new int[total];
                    boolean greaterSerialNumberFound = false;
                    String lastUpdated = Helper.getDate() + " " + Helper.getTime();

                    if (inputHashText.length() > 0) lines = inputHashText.split(separator);

                    absentMemberList = new String[total + 1];
                    Arrays.fill(absentMemberList, "null");

                    for (String serialNumberAsString : lines) {
                        int serialNumber = Integer.parseInt(serialNumberAsString);

                        if (serialNumber <= total) arr[serialNumber - 1] = 1;
                        else {
                            greaterSerialNumberFound = true;
                        }
                    }

                    StringBuilder ans = new StringBuilder();
                    int absentMembers = 0;

                    SharedPreferences.Editor myEdit = sharedPreferences.edit();

                    myEdit.putString(Helper.LastUpdatedTime, lastUpdated);
                    myEdit.putString(Helper.InputHashText, inputHashText);
                    myEdit.apply();

                    for (int i = 0; i < total; i++) {
                        if (arr[i] == 0) {
                            String sno = (i + 1) + "";
                            String s1 = sharedPreferencesContactsGroup.getString(sno, "Default Value");
                            absentMemberList[i + 1] = s1;
                            absentMembers++;
                            ans.append(s1).append("\n");
                        }
                    }

                    presentMember = "" + (total - absentMembers);
                    absentMember = "" + absentMembers;

                    presentMember = formatstr(totalMember, presentMember);
                    absentMember = formatstr(totalMember, absentMember);

                    result.setText(ans.toString());
                    countabsent.setText(absentMembers + "");
                    lastupdatetime.setText(lastUpdated);


                    if (greaterSerialNumberFound) displayToast("Number greater than total");

                    hideKeyboard();


                } catch (Exception e) {
                    Log.d(tag, e.toString());

                    displayToast("Please enter correct");

                    hideKeyboard();
                }
            }
        });

        result.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String absentListAsString = result.getText().toString();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", absentListAsString);
                clipboard.setPrimaryClip(clip);
                displayToast("copied");
                vibrate(100);

            }
        });

        cross.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                countstrings.setText("");
                result.setText("");
                countabsent.setText("");
                vibrate(100);
            }
        });

        updateContactsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                updateContact();
            }
        });

    }

    public void onResume() {
        super.onResume();
        view = getWindow().getDecorView().getRootView();

        setSharedPreferences();
    }

    public void onStart() {
        super.onStart();
        view = getWindow().getDecorView().getRootView();

        setSharedPreferences();
    }

    public void onPause() {
        super.onPause();

        Log.d(tag, "onPause: ");
    }

    public void onStop() {
        super.onStop();

        Log.d(tag, "onStop: ");
    }

    public void setSharedPreferences() {
        try {
            SharedPreferences sharedPreferencesGroup = getSharedPreferences("group", MODE_PRIVATE);
            sPref = sharedPreferencesGroup.getString(Helper.SelectedGroup, "MainGroupSharedPref");
            sharedPreferences = getSharedPreferences(sPref, MODE_PRIVATE);
            sharedPreferencesContactsGroup = getSharedPreferences(sPref + "ContactsGroup", MODE_PRIVATE);
            setText(sharedPreferences);
        } catch (Exception ignored) {
            Log.d(tag, "setSharedPreferences: " + ignored.toString());
        }
    }

    class UpdateContacts extends Thread {

        String contactIdentifier;
        View view;

        UpdateContacts(View view, String contactIdentifier) {
            this.view = view;
            this.contactIdentifier = contactIdentifier;
        }

        public void run() {
            ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.contactsUpdateBar);
            ImageView updateContactsButton = (ImageView) findViewById(R.id.refreshbutton);

            try {
                setVisibility(simpleProgressBar, updateContactsButton, true);
                displaySnackbar(view, contactIdentifier + " Tag Contact Updating....");
                vibrate(100);

                List<ContactModel>[] contactsList = getListsOfBothGroup(contactIdentifier);

                if (!sPref.equals("MainGroupSharedPref")) {
                    contactsList[0] = contactsList[1];
                }

                List<ContactModel> groupMemberContacts = contactsList[0];
                int foundContacts = groupMemberContacts.size();
                putContactsIntoStorage(groupMemberContacts);


                String printMessage = foundContacts + " Contacts Found with " + contactIdentifier;
                displaySnackbar(view, printMessage);
                vibrate(250);

                setVisibility(simpleProgressBar, updateContactsButton, false);

            } catch (Exception e) {
                Log.d(tag, "error here : " + e.toString());
                displaySnackbar(view, "Something Went Wrong");
                setVisibility(simpleProgressBar, updateContactsButton, false);
            }

        }
    }

    public void setVisibility(ProgressBar simpleProgressBar, ImageView updateContactsButton, boolean isVisible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isVisible) {
                    simpleProgressBar.setVisibility(View.VISIBLE);
                    updateContactsButton.setVisibility(View.INVISIBLE);
                } else {
                    simpleProgressBar.setVisibility(View.INVISIBLE);
                    updateContactsButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQ_CODE) {
            if (grantResults.length > 0) {
                Log.d(tag, "len : " + grantResults.length);
                int res = grantResults[0];
                boolean checkloc = res == PackageManager.PERMISSION_GRANTED;
                if (checkloc) {
                    startContactUpdating();
                } else {
                }
            }
        } else {
            Log.d(tag, "p no");
        }
    }

    public boolean checkPermissionGranted() {

        int result = ActivityCompat.checkSelfPermission(this, READ_CONTACTS);

        if (result == PackageManager.PERMISSION_GRANTED) {
            Log.d(tag, "permission granted ");
            return true;
        } else {
            Log.d(tag, "permission not granted ");
            return false;
        }
    }

    public void updateContact() {

        try {
            if (checkPermissionGranted()) {
                startContactUpdating();
            } else {
                displayPermissionSnackbar(view, "Permission not granted");
                String[] permissions = {READ_CONTACTS};
                Random rand = new Random();
                REQ_CODE = rand.nextInt(1000);
                ActivityCompat.requestPermissions(Display.this, permissions, REQ_CODE);

            }
        } catch (Exception e) {
            Log.d(tag, "error is here" + e.toString());

        }
    }

    public void startContactUpdating() {
        updatecontactobj = new UpdateContacts(view, identifier);
        updatecontactobj.start();
    }

    public void updateConfig(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(sPref, MODE_PRIVATE);

        separator = sharedPreferences.getString(Helper.Separator, "%");
        String identifier1 = sharedPreferences.getString(Helper.Identifier, "%");

        if (!identifier.equals(identifier1)) {
            updateContact();
        }

    }

    public void displaySnackbar(View view, String data) {
        Snackbar snk = Snackbar.make(view, data, Snackbar.LENGTH_SHORT).setAction("Action", null);

        View sbView = snk.getView();
        snk.show();
    }

    public void displayPermissionSnackbar(View view, String data) {

        try {
            Snackbar snk = Snackbar.make(view, data, Snackbar.LENGTH_SHORT).setAction("Action", null);

            View sbView = snk.getView();

            snk.setAction("Set Permission", new OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null)));
                }
            });
            snk.show();
        } catch (Exception e) {
            Log.d(tag, "displayPermissionSnackbar: " + e.toString());
        }
    }

    public String formatstr(String total, String modify) {

        int len1 = total.length();
        int len2 = modify.length();

        if (len1 == len2) return modify;

        StringBuilder zero = new StringBuilder();
        int missing = len1 - len2;

        while (missing-- > 0) {
            zero.append("0");
        }

        modify = zero + modify;
        return modify;

    }

    public void vibrate(int duration) {

        SharedPreferences sharedPreferences1 = getSharedPreferences("group", MODE_PRIVATE);
        String isVibrate = sharedPreferences1.getString(Helper.isVibrate, "false");

        Log.d(tag, "vibrate: " + isVibrate);

        if (isVibrate.equals("false")) return;

        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 2000 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vib.vibrate(duration);
        }
    }

    public void putContactsIntoStorage(List<ContactModel> groupMember) {

        sharedPreferencesContactsGroup.edit().clear().commit();

        SharedPreferences.Editor myEdit = sharedPreferencesContactsGroup.edit();

        for (ContactModel contact : groupMember) {
            myEdit.putString("" + contact.serialNumber, contact.name.toString());
        }
        myEdit.apply();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            Log.d(tag, "Software Keyboard was shown");
        } else {
            Log.d(tag, "Software Keyboard was not shown");
        }
    }

    public String getNumericEmoji(String member) {

        String[] str = {"0️⃣", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣"};

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < member.length(); i++) {
            int c = Integer.parseInt(member.charAt(i) + "");
            result.append("").append(str[c]);
            if (member.length() == 1 && member.charAt(0) == '0') {
                result.append("").append(str[c]);
            }
        }

        return result.toString();
    }

    public List<ContactModel>[] getListsOfBothGroup(String contactIdentifier) {

        return getContacts(this, contactIdentifier);

    }

    public static int getSerialNumber(String str) {

        StringBuilder ans = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            if (c == '.') return Integer.parseInt(ans.toString());

            ans.append(c);

        }
        return Integer.parseInt(ans.toString());
    }

    public String trimstr(String str) {

        try {
            StringBuilder resultstr = new StringBuilder();
            boolean canstart = false;

            for (int i = 0; i < str.length() - 1; i++) {
                char c = str.charAt(i);
                if (canstart) {
                    String ct = "" + str.charAt(i) + str.charAt(i + 1);
                    if (ct.equals(identifier)) {
                        return resultstr.toString();
                    }
                    resultstr.append(c);
                }
                if (!Switchbuttonstate) {
                    if (c == '.') canstart = true;
                } else {
                    String ct = "" + str.charAt(i) + str.charAt(i + 1);
                    if (ct.equals("S2")) {
                        canstart = true;
                        i++;
                    }
                }
            }
            resultstr.append(" ");

            return resultstr.toString();
        } catch (Exception e) {
            Log.d(tag, "trimstr error : " + e.toString());
            return "";
        }
    }

    public boolean isBelongToLearningGroup(String name) {

        int len = name.length();

        for (int i = 0; i < len - 1; i++) {

            String learningGroupTag = name.charAt(i) + "" + name.charAt(i + 1);

            if (learningGroupTag.equals("S2")) {
                return true;
            }
        }
        return false;
    }

    public List<ContactModel>[] getContacts(Context ctx, String contactIdentifier) {
        try {

            List<ContactModel> contactList = new ArrayList<>();
            HashMap<String, Integer> mainmap = new HashMap<>();
            List<ContactModel> contactListLearn = new ArrayList<>();

            List<ContactModel>[] list = new List[2];

            ContentResolver contentResolver = ctx.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                        while (cursorInfo.moveToNext()) {
                            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            String mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            int identifierLength = contactIdentifier.length();
                            if (name.length() <= identifierLength) continue;
                            String nameEndString = name.substring(name.length() - identifierLength);

                            if (Character.isDigit(name.charAt(0)) && nameEndString.equals(contactIdentifier)) {

                                ContactModel newContact = new ContactModel();

                                newContact.id = id;
                                newContact.serialNumber = "";
                                newContact.mobileNumber = mobileNumber;
                                newContact.name = name.substring(0, name.length() - identifierLength);

                                int serialNumber=getSerialNumberFromNameString(name);

                                if(serialNumber==-1) continue;

                                newContact.serialNumber=""+serialNumber;

                                if (isBelongToLearningGroup(name)) {
                                    contactListLearn.add(newContact);
                                } else {
                                    if (!mainmap.containsKey(newContact.mobileNumber)) {
                                        contactList.add(newContact);
                                        mainmap.put(newContact.mobileNumber, 1);
                                    }
                                }
                            }

                        }
                        cursorInfo.close();
                    }
                }
                cursor.close();
            }

            list[0] = new ArrayList<>(contactList);
            list[1] = new ArrayList<>(contactListLearn);

            return list;

        } catch (Exception e) {
            Log.d(tag, "getContacts: erroe : " + e.toString());
            return null;
        }
    }

    public static int getSerialNumberFromNameString(String name) {

        boolean dotFound = false;
        String serialNumberAsString = "";

        for (int i = 0; i < name.length(); i++) {
            Character ch = name.charAt(i);
            if (Character.compare(ch, '.') == 0) {
                dotFound = true;
                break;
            } else {

                serialNumberAsString += ch;
            }
        }

        serialNumberAsString = serialNumberAsString.replaceAll("\\s", "");

        if (dotFound) {
            return getSerialNumber(serialNumberAsString);
        } else {
            return -1;
        }

    }

    public void copyData(String displayString) {

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", displayString);
        clipboard.setPrimaryClip(clip);

    }

    public void displayToast(String displayText) {
        Toast t1 = Toast.makeText(getApplicationContext(), displayText, Toast.LENGTH_SHORT);
        t1.show();
    }

    public void setText(SharedPreferences shPref) {

        try {
            EditText count = (EditText) findViewById(R.id.count);
            EditText inputHashText = (EditText) findViewById(R.id.inputHashText);
            TextView lastUpdatedTime = (TextView) findViewById(R.id.lastupdatetime);

            String inputString = shPref.getString(Helper.InputHashText, "");
            String lastUpdateTime = shPref.getString(Helper.LastUpdatedTime, "last-updated");
            identifier = shPref.getString(Helper.Identifier, "SM");
            separator = shPref.getString(Helper.Separator, "%");
            area = shPref.getString(Helper.Area, "देवली+ संगम विहार");
            totalMember = shPref.getString(Helper.TotalMember, "0");

            count.setText(totalMember);
            inputHashText.setText(inputString);
            lastUpdatedTime.setText(lastUpdateTime);

            inputHashText.setSelection(inputHashText.getText().length());
        } catch (Exception e) {
            displayToast("Some things Went Wrong please Close the App");
        }
    }

    public void doConfiguration(View v) {
        Intent i = new Intent(this, groupConfig.class);
        i.putExtra("sPref", sPref);
        String reportFormatarr = "";
        for (String reportLine : reportFormat) {
            reportFormatarr += reportLine;
            reportFormatarr += "#";
        }

        i.putExtra("reportFormat", reportFormatarr);
        startActivity(i);
    }

    public void resizedText(String inputStr) {
        Intent i = new Intent(this, resizedText.class);
        i.putExtra("sPref", sPref);
        i.putExtra("value", inputStr);
        startActivity(i);
    }
}

