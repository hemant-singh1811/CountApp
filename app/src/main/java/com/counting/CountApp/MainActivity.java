package com.counting.CountApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.counting.CountApp.R;
import com.counting.CountApp.databinding.ActivityMainBinding;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    String tag="MainActivityTag";

    private ActivityMainBinding binding;

    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Button btn1=(Button) findViewById(R.id.mainbtn);
        Button btn2=(Button) findViewById(R.id.learnbtn);

        // name of sharedPreferneces
        // 1- maingroup ("MainGroupSharedPref")
        // 2- learning group ("LearningGroupSharedPref")
        // 3- count -> have 2 count of both group (main and learning)
        //      ->key: ("totalmaingroupmember") , ("totallearninggroupmember")
        // 4- lastupdate time -> ("lastupdate")
        //      ->key :  ("mainlastupdate")  ("learninglastupdate")

        sharedPreferences = getSharedPreferences("group", MODE_PRIVATE);

        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Display.class);

                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                myEdit.putString("sPref", "MainGroupSharedPref");
                myEdit.commit();

                startActivity(i);
            }
        });

        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Display.class);

                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                myEdit.putString("sPref", "LearningGroupSharedPref");
                myEdit.commit();
                startActivity(i);
            }
        });

    }

}

