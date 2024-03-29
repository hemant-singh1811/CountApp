package com.counting.CountApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import com.counting.CountApp.R;
import com.counting.CountApp.databinding.ActivityMainBinding;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Button btn1=(Button) findViewById(R.id.mainbtn);
        Button btn2=(Button) findViewById(R.id.learnbtn);
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

    }

    public void emitIntent(String group){
        Intent i = new Intent(getApplicationContext(), Display.class);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString(Helper.SelectedGroup,group);
        myEdit.commit();
        startActivity(i);
    }

}

