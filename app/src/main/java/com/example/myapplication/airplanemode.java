package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class airplanemode extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        CharSequence text = "airplane mode turnoff/on";
        int duration = Toast.LENGTH_SHORT;
        Log.d("RCVG","language changed");
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        Intent myIntent = new Intent(context,MainActivity.class);

         context.startActivity(myIntent);
    }
}