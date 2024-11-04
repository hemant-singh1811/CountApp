package com.counting.CountApp;


import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Helper {

    public static final String MainAppSharedPreferences="#CountApp#";
    public static final String SelectedGroup="sPref";
    public static final String SelectedGroupName="SelectedGroupName";
    public static final String InputHashText="inputHashText";
    public static final String LastUpdatedTime="lastUpdatedTime";
    public static final String Identifier="identifier";
    public static final String Separator="separator";
    public static final String Area="area";
    public static final String TotalMember="totalMember";
    public static final String ReportFormat="reportFormat";
    public static final String isVibrate="isVibrate";
    public static final String GroupsDetails="GroupsArrayAsString";


    public static String getDate() {
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        return currentDate;
    }

    public static String getTime() {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        return currentTime;
    }


}
