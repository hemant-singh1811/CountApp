package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.example.myapplication.databinding.ActivityMainBinding;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    String tag="MainActivityTag";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        Button btn1=(Button) findViewById(R.id.mainbtn);
        Button btn2=(Button) findViewById(R.id.learnbtn);

        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Display.class);
                i.putExtra("Value1", "This value one for ActivityTwo ");
                i.putExtra("Value2", "This value two ActivityTwo");
                startActivity(i);
            }
        });

        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Display.class);
                i.putExtra("Value1", "This value one for ActivityTwo ");
                i.putExtra("Value2", "This value two ActivityTwo");
                startActivity(i);
            }
        });

    }

}

