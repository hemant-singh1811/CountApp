package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class groupConfig extends AppCompatActivity {

    String sPref="";
    SharedPreferences sharedPreferences =null;
    String[] reportFormat = {"🙏बन्दीछोड सतगुरु रामपाल जी महाराज जी की जय हो🙏", "🌹 देवली+ संगम विहार सोशल मीडिया सेवा🌹", "date", "की सेवा का विवरण", "जिन भगतात्माओ ने सेवा की है।", "Total members        ➡", "totalmember", "PRESENT.                 ➡", "presentmember", "ABSENT.                   ➡", "absentmember", "Note:- सभी भगतात्माओ से प्रार्थना है सेवा में बढ़-चढ़कर सहयोग करें।", "🙏 सत साहेब जी 🙏"};
    String tag="groupConfigTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_config);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sPref = extras.getString("sPref");
            sharedPreferences=getSharedPreferences(sPref,MODE_PRIVATE);
            setText(sharedPreferences);
        }

        setReportFormat(reportFormat);

        EditText reportFormatTag = (EditText) findViewById(R.id.reportFormat);

        reportFormatTag.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String updatedReportFormat=reportFormatTag.getText().toString();

                String updatedReportFormatarr[]=reportFormatTag.getText().toString().split("\n");

                for (String line:updatedReportFormatarr){
                    if(line.isEmpty()){
                        Log.d(tag,"this is empty");
                    }
                    Log.d(tag,line+"end");
                }

                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("reportFormat", updatedReportFormat);
                myEdit.commit();

                Toast t1=Toast.makeText(getApplicationContext(),"Updated", Toast.LENGTH_SHORT);
                t1.show();

            }
        });


    }

    public void saveConfig(View v){

        if(sharedPreferences==null) return;

        EditText identifier = (EditText) findViewById(R.id.identifier);
        EditText separator = (EditText) findViewById(R.id.separator);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(sPref+"-identifier", identifier.getText().toString());
        myEdit.putString(sPref+"-separator", separator.getText().toString());
        myEdit.commit();

        Toast t1=Toast.makeText(getApplicationContext(),"Setting are Saved", Toast.LENGTH_SHORT);
        t1.show();

        Intent i = new Intent(getApplicationContext(), Display.class);
        i.putExtra("prev", "groupConfig");
        startActivity(i);

    }

    public void setText(SharedPreferences shPref){

        EditText identifier = (EditText) findViewById(R.id.identifier);
        EditText separator = (EditText) findViewById(R.id.separator);

        String identifierStr= shPref.getString(sPref+"-identifier", "SM");
        String separatorStr= shPref.getString(sPref+"-separator", "%");

        identifier.setText(identifierStr);
        separator.setText(separatorStr);

    }

    public void setReportFormat(String[] reportFormat){

//        String reportFormat = sharedPreferences.getString("reportFormat", "format");

        EditText reportFormatTag = (EditText) findViewById(R.id.reportFormat);

        String resultantReportFormatText="";

        for (String line:reportFormat){
            resultantReportFormatText+=line;
            resultantReportFormatText+="\n";
        }

        reportFormatTag.setText(resultantReportFormatText);
    }
}