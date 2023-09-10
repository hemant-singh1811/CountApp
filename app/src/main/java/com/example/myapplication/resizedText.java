package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class resizedText extends AppCompatActivity {

    String tag="resizedTextTag";
    String sPref="";
    SharedPreferences sharedPreferences =null;
    String inputString="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resized_text);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("sPref");
            inputString = extras.getString("value");

            sPref=value;

            sharedPreferences=getSharedPreferences(sPref,MODE_PRIVATE);
        }

        EditText inputStringView=(EditText) findViewById(R.id.inputString);

        inputStringView.setText(inputString);

        sharedPreferences = getSharedPreferences(sPref,MODE_PRIVATE);

        inputStringView.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String updatedcount=inputStringView.getText().toString();

                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("inputstring", updatedcount);
                myEdit.commit();

                Toast t1=Toast.makeText(getApplicationContext(),"Updated", Toast.LENGTH_SHORT);
                t1.show();

            }
        });

    }
}