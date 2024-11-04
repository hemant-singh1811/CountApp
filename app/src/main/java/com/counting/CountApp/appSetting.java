package com.counting.CountApp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class appSetting extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    String TAG = "appSettingTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_settings);

        getSupportActionBar().setTitle("App Settings");

        sharedPreferences = getSharedPreferences("group", MODE_PRIVATE);
        String isVibrate = sharedPreferences.getString(Helper.isVibrate, "false");
        Switch onOffSwitch = (Switch) findViewById(R.id.vibrationToogleButton);

        if (isVibrate.equals("true"))
            onOffSwitch.setChecked(true);
        else
            onOffSwitch.setChecked(false);

        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.v(TAG, "is ON : " + isChecked);

                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                myEdit.putString(Helper.isVibrate, isChecked + "");
                myEdit.commit();
            }

        });
    }
}
