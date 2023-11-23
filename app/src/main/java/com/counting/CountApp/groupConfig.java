package com.counting.CountApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.counting.CountApp.R;

public class groupConfig extends AppCompatActivity {

    String sPref = "";
    String tag = "groupConfigTag";
    String TAG = "groupConfigTag";

    String[] reportFormat =null;
    SharedPreferences sharedPreferences = null;

    String[] languages = { "C","C++","Java","C#","PHP","JavaScript","jQuery","AJAX","JSON" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_config);

        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                sPref = extras.getString("sPref");
                sharedPreferences = getSharedPreferences(sPref, MODE_PRIVATE);
            }


        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(intent);

            setTextToDisplay(sharedPreferences);

            EditText reportFormatTag = (EditText) findViewById(R.id.reportFormat);
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
                    String area = areaTag.getText().toString();
                    setArea(area);
                }
            });

            reportFormatTag.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {
                    String updatedReportFormat = reportFormatTag.getText().toString();

                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString(Helper.ReportFormat, updatedReportFormat);
                    myEdit.commit();

                    displayToast("Updated");
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

            });


        } catch (Exception e) {
            Log.d(TAG, "onCreate: Error" + e.toString());
        }
    }

    public void saveConfig(View v) {

        if (sharedPreferences == null) return;

        EditText identifier = (EditText) findViewById(R.id.identifier);
        EditText separator = (EditText) findViewById(R.id.separator);
        EditText areaTag = (EditText) findViewById(R.id.area);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(Helper.Identifier, identifier.getText().toString());
        myEdit.putString(Helper.Separator, separator.getText().toString());
        myEdit.commit();

        String area = areaTag.getText().toString();
        setArea(area);

        super.onBackPressed();
    }

    public void setArea(String area) {
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(Helper.Area, area);
        myEdit.commit();

        displayToast("Updated");

    }

    public void setTextToDisplay(SharedPreferences shPref) {

        try {
            EditText identifier = (EditText) findViewById(R.id.identifier);
            EditText separator = (EditText) findViewById(R.id.separator);
            EditText area=(EditText) findViewById(R.id.area);

            String identifierAsString = shPref.getString(Helper.Identifier, "10th");
            String separatorAsString = shPref.getString(Helper.Separator, "%");
            String areaAsString=shPref.getString(Helper.Area,"CBSE");
            String reportFormatAsString = shPref.getString(Helper.ReportFormat, "null");

            identifier.setText(identifierAsString);
            separator.setText(separatorAsString);
            area.setText(areaAsString);

            if (!reportFormatAsString.equals("null"))
                reportFormat = reportFormatAsString.split("\n");

            setReportFormat(reportFormat);

        } catch (Exception e) {
            Log.d(tag, "setText: error" + e.toString());
        }
    }

    public void setReportFormat(String[] reportFormat) {
        TextView reportFormatTag = (TextView) findViewById(R.id.reportFormat);

        StringBuilder resultantReportFormatText = new StringBuilder();

        for (String line : reportFormat) {
            resultantReportFormatText.append(line);
            resultantReportFormatText.append("\n");
        }

        if(resultantReportFormatText.length()>1)
        reportFormatTag.setText(resultantReportFormatText.toString());
    }

    public void displayToast(String displayText) {
        Toast t1 = Toast.makeText(getApplicationContext(), displayText, Toast.LENGTH_SHORT);
        t1.show();
    }

}