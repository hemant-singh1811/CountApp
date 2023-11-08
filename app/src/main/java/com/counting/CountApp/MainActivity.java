package com.counting.CountApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//        startActivity(intent);

        Button btn1 = (Button) findViewById(R.id.mainbtn);
        Button btn2 = (Button) findViewById(R.id.learnbtn);
        ImageView appSettingButton=(ImageView) findViewById(R.id.appSetting);

        sharedPreferences = getSharedPreferences("group", MODE_PRIVATE);

        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                emitIntent("MainGroupSharedPref");
            }
        });

        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                emitIntent("LearningGroupSharedPref");
            }
        });

        appSettingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), appSetting.class);
                startActivity(i);
            }
        });

    }

    public void emitIntent(String group) {
        Intent i = new Intent(getApplicationContext(), Display.class);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString(Helper.SelectedGroup, group);
        myEdit.commit();
        startActivity(i);
    }

}

