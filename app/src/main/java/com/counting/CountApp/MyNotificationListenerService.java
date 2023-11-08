package com.counting.CountApp;

import android.app.Notification;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class MyNotificationListenerService extends NotificationListenerService {

    String TAG = "MyNotificationListenerServiceTAG";
    final String TargetApplicationPackage = "com.whatsapp";

        final String group1 = "SANGAM VIHAR SOCIAL M DL";
//    final String group1 = "🙏Sample testing group🙏" ;
    final String group2 = "South Delhi Learning G DL";

    final String group1SPref = "MainGroupSharedPref";
    final String group2SPref = "LearningGroupSharedPref";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        String packageName = sbn.getPackageName();
        String Ticker = "";
        if (sbn.getNotification().tickerText != null) {
            Ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();

        String groupBelongsTo = "";
        String personName = "";

        if (packageName.equals(TargetApplicationPackage)) {
            String[] TickerArray = Ticker.split("@");


                Log.i(TAG, "onNotificationPosted Package : " + packageName);
                Log.i(TAG, "onNotificationPosted Ticker : " + Ticker);
                Log.i(TAG, "onNotificationPosted Title : " + title);
                Log.i(TAG, "onNotificationPosted Text : " + text);
                Log.i(TAG, "onNotificationPosted nTickerArray Size : " + TickerArray.length);


            if (TickerArray.length > 1) {

                groupBelongsTo = TickerArray[1];
                groupBelongsTo = groupBelongsTo.substring(1, groupBelongsTo.length());

                personName = TickerArray[0];
                personName = personName.substring(13, personName.length());

                Log.d(TAG, "onNotificationPosted: groupBelongsTo :" + groupBelongsTo);
                Log.d(TAG, "onNotificationPosted: personName :" + personName);


                if (groupBelongsTo.equals(group1)) {
                    Log.d(TAG, "onNotificationPosted: groupBelongsTo :" + group1);

                    puttingData(personName, group1SPref);
                } else if (groupBelongsTo.equals(group2)) {
                    Log.d(TAG, "onNotificationPosted: groupBelongsTo :" + group2);
                    puttingData(personName, group2SPref);
                }

                // 1. need selected groups
                // 2. Identifier
                // 3. seperator for updating value
            }
        }
    }

    public void puttingData(String personName, String groupSPref) {

        try {
            SharedPreferences sharedPreferences = getSharedPreferences(groupSPref, MODE_PRIVATE);
            String inputHashText = sharedPreferences.getString(Helper.InputHashText, "");
            String separator = sharedPreferences.getString(Helper.Separator, "%");
            String lastUpdateTime = sharedPreferences.getString(Helper.LastUpdatedTime, "last-updated");

            String[] lastUpdateDateArray = lastUpdateTime.split(" ");
            String lastUpdatedDate = lastUpdateDateArray[0];

            Log.d(TAG, "lastUpdateTime: " + lastUpdateTime);
            Log.d(TAG, "lastUpdatedDate: " + lastUpdatedDate);

            int serialNumber = getSerialNumber(personName);
            String lastUpdated = Helper.getDate() + " " + Helper.getTime();

            if (lastUpdatedDate.equals(Helper.getDate())) {
                Log.d(TAG, "lastUpdatedDate equals to today's date: ");

                inputHashText += serialNumber + separator;
            } else {
                inputHashText=""+ serialNumber + separator;
            }

            SharedPreferences.Editor myEdit = sharedPreferences.edit();

            myEdit.putString(Helper.LastUpdatedTime, lastUpdated);
            myEdit.putString(Helper.InputHashText, inputHashText);
            myEdit.apply();

            Log.d(TAG, "onNotificationPosted: serialNumber :" + serialNumber);

        } catch (Exception e) {
            Log.d(TAG, "puttingData: error  : " + e.toString());
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.d(TAG, "onNotificationRemoved Removed  ");
//        Log.d(TAG, "onNotificationPosted: "+sbn.toString());
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

    public String getGroup(String name, String contactIdentifier) {
        if (name.length() <= 2) return "";

        int identifierLength = contactIdentifier.length();

        String nameEndString = name.substring(name.length() - identifierLength);

        if (Character.isDigit(name.charAt(0)) && nameEndString.equals(contactIdentifier)) {
            if (isBelongToLearningGroup(name)) return group2SPref;
            else return group1SPref;
        }

        return "";
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

}

