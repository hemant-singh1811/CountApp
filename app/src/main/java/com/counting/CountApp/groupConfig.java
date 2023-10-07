package com.counting.CountApp;

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

import com.counting.CountApp.R;

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
            String reportFormat=extras.getString("reportFormat");
            Log.d(tag, "onCreate: reportFormat : "+reportFormat);
            this.reportFormat=reportFormat.split("#");

            sharedPreferences=getSharedPreferences(sPref,MODE_PRIVATE);
            setText(sharedPreferences);
        }

        setReportFormat(reportFormat);

        TextView reportFormatTag = (TextView) findViewById(R.id.reportFormat);

        EditText areaTag = (EditText) findViewById(R.id.area);

        areaTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String area=areaTag.getText().toString();

                setArea(area);

            }
        });

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
                myEdit.putString(Helper.ReportFormat, updatedReportFormat);
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
        EditText areaTag = (EditText) findViewById(R.id.area);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(Helper.Identifier, identifier.getText().toString());
        myEdit.putString(Helper.Separator, separator.getText().toString());
        myEdit.commit();

        Toast t1=Toast.makeText(getApplicationContext(),"Setting are Saved", Toast.LENGTH_SHORT);
        t1.show();

        String area=areaTag.getText().toString();
        setArea(area);

        Intent i = new Intent(getApplicationContext(), Display.class);
        i.putExtra("prev", "groupConfig");
        startActivity(i);

    }

    public void setArea(String area){
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(Helper.Area, area);
        myEdit.commit();

        Toast t1=Toast.makeText(getApplicationContext(),"Updated", Toast.LENGTH_SHORT);
        t1.show();
    }

    public void setText(SharedPreferences shPref){

        EditText identifier = (EditText) findViewById(R.id.identifier);
        EditText separator = (EditText) findViewById(R.id.separator);

        String identifierStr= shPref.getString(Helper.Identifier, "SM");
        String separatorStr= shPref.getString(Helper.Separator, "%");

        identifier.setText(identifierStr);
        separator.setText(separatorStr);

    }

    public void setReportFormat(String[] reportFormat){
        TextView reportFormatTag = (TextView) findViewById(R.id.reportFormat);

        StringBuilder resultantReportFormatText= new StringBuilder();

        for (String line:reportFormat){
            resultantReportFormatText.append(line);
            resultantReportFormatText.append("\n");
        }
        reportFormatTag.setText(resultantReportFormatText.toString());
    }
}