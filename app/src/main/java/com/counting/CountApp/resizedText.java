package com.counting.CountApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.counting.CountApp.R;

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
        ImageView pasteButton=(ImageView) findViewById(R.id.pasteButton);

        inputStringView.setText(inputString);

        sharedPreferences = getSharedPreferences(sPref,MODE_PRIVATE);

        inputStringView.setSelection(inputStringView.getText().length());


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

        pasteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentInputString=inputStringView.getText().toString();

                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

                ClipData clipData=myClipboard.getPrimaryClip();

                String copiedText=clipData.getItemAt(0).coerceToText(getApplicationContext()).toString();

                currentInputString+=copiedText;

                Log.d(tag,copiedText);

                inputStringView.setText(currentInputString);

                inputStringView.setSelection(inputStringView.getText().length());

                vibrate(250);
            }
        });
    }

    public void vibrate(int duration) {
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 2000 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vib.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vib.vibrate(duration);
        }
    }
}