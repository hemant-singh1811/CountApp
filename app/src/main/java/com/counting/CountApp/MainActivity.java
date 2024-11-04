package com.counting.CountApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivityTag";
    SharedPreferences sharedPreferences = null;
    ArrayList<String> groupsArray = new ArrayList<>();
    HashMap<Integer,Integer> positionToGroupIndex=new HashMap<>();
    HashMap<Integer,String> groupIndexToGroupName=new HashMap<>();
    ListView list=null;
    private LinearLayout bottomSlideLayout;
    private boolean isSlideUp = false;
    String groupsArrayAsString="";
    int maximumSerialNumber=0;
    int i = 1;
    int totalNumberOfGroup=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.list);
        bottomSlideLayout = findViewById(R.id.bottomSlideLayout);
        ImageView downIcon=(ImageView) findViewById(R.id.downIcon);
        ImageView appSettingButton = (ImageView) findViewById(R.id.appSetting);
        FloatingActionButton createGroup = (FloatingActionButton) findViewById(R.id.createGroup);
        AppCompatButton createGroupButton = (AppCompatButton) findViewById(R.id.createGroupButton);

        getDataFromSharedPreferences();
        displayGroups();

        createGroupButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: i : " + i);

                EditText groupNameView=(EditText) findViewById(R.id.groupName);
                EditText contactSeparatorView=(EditText) findViewById(R.id.contactSeparator);
                EditText contactIdentifierView=(EditText) findViewById(R.id.contactIdentifier);

                String groupName=groupNameView.getText().toString();
                String contactSeparator=contactSeparatorView.getText().toString();
                String contactIdentifier=contactIdentifierView.getText().toString();

                if(groupName == null || groupName.equals(" ") ) {
                    groupNameView.setError("Cannot Empty");
                    return;
                }

                int serialNumber=++maximumSerialNumber;

                groupsArrayAsString+=serialNumber+"@"+groupName+"#";
                String sharedPrefString="#CountAPP#GroupSharedPref"+serialNumber;

                Log.d(TAG, "onClick: serialNumber "+serialNumber);
                Log.d(TAG, "onClick: maximumSerialNumber : "+maximumSerialNumber);
                Log.d(TAG, "onClick: groupsArrayAsString : "+groupsArrayAsString);

                SharedPreferences sharedPreferences1= getSharedPreferences(sharedPrefString, MODE_PRIVATE);
                SharedPreferences.Editor myEdit1 = sharedPreferences1.edit();
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                myEdit1.putString(Helper.Area, "CBSE");
                myEdit1.putString(Helper.ReportFormat, "");
                myEdit1.putString(Helper.Separator,contactSeparator);
                myEdit1.putString(Helper.Identifier,contactIdentifier);
                myEdit.putString(Helper.GroupsDetails,groupsArrayAsString);

                myEdit1.apply();
                myEdit.apply();

                positionToGroupIndex.put(totalNumberOfGroup,serialNumber);
                groupIndexToGroupName.put(serialNumber,groupName);

                ++totalNumberOfGroup;

                groupsArray.add(groupName);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext()
                        , R.layout.list_group_view, groupsArray);

                if (isSlideUp) {
                    hideKeyboard();
                    slideDown(bottomSlideLayout);
                    createGroup.setVisibility(View.VISIBLE);
                }

                groupNameView.setText("");
                contactSeparatorView.setText("");
                contactIdentifierView.setText("");

                isSlideUp = !isSlideUp;

                list.setAdapter(arrayAdapter);
            }
        });

        createGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSlideUp) {
                    slideUp(bottomSlideLayout);
                    createGroup.setVisibility(View.INVISIBLE);
                }
                isSlideUp = !isSlideUp;
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                try{
                    final CharSequence[] items = { "Delete Item","Edit" };

                    int groupIndex=positionToGroupIndex.get(pos);
                    String groupName=groupIndexToGroupName.get(groupIndex);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Delete "+groupName);

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 0:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                            MainActivity.this);
                                    builder.setMessage(
                                                    "Are you sure you want to delete?")
                                            .setCancelable(false)
                                            // Prevents user to use "back button"
                                            .setPositiveButton(
                                                    "Delete",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {
                                                            //Todo code here

                                                            deleteGroup(pos,id);

                                                        }
                                                    })
                                            .setNegativeButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                    builder.show();
                                    break;
                                case 1:
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(
                                            MainActivity.this);
                                    builder1.setMessage(
                                                    "Are you sure you want to Edit?")
                                            .setCancelable(false)
                                            // Prevents user to use "back button"
                                            .setPositiveButton(
                                                    "Edit",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {
                                                            //Todo code here

                                                            deleteGroup(pos,id);

                                                        }
                                                    })
                                            .setNegativeButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                    builder1.show();
                                    break;
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return false;

                }catch (Exception e){
                    Log.d(TAG, "onItemLongClick: error"+e.toString());
                }
                return false;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                try {
                    Log.d(TAG, "onItemClick: position : "+position);
                    int groupIndex = positionToGroupIndex.get(position);
                    String selectedGroupName=groupIndexToGroupName.get(groupIndex);

                    Log.i(TAG, "click on pos : " + position);
                    Log.d(TAG, "onItemClick: groupIndex : "+groupIndex);

                    String sharedPrefString="#CountAPP#GroupSharedPref"+groupIndex;

                    emitIntent(sharedPrefString,selectedGroupName);
                }catch (Exception e){
                    Log.d(TAG, "onItemClick111: error : "+e.toString());
                }

            }
        });

        downIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSlideUp) {
                    hideKeyboard();
                    slideDown(bottomSlideLayout);
                    createGroup.setVisibility(View.VISIBLE);
                }

                isSlideUp = !isSlideUp;
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

    public void onResume() {
        super.onResume();

        getDataFromSharedPreferences();
        displayGroups();
    }

    public void onStart() {
        super.onStart();

        getDataFromSharedPreferences();
        displayGroups();
    }

    public void onPause() {
        super.onPause();

    }

    public void onStop() {
        super.onStop();

    }

    private void slideUp(View view) {

        ConstraintLayout ll1=(ConstraintLayout) findViewById(R.id.groupNameLayout);
        ConstraintLayout ll2=(ConstraintLayout) findViewById(R.id.contactSeparatorConstraintLayout);
        ConstraintLayout ll3=(ConstraintLayout) findViewById(R.id.contactIdentifierConstraintLayout);
        AppCompatButton createButton=(AppCompatButton) findViewById(R.id.createGroupButton);

        view.setVisibility(View.VISIBLE);

        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.VISIBLE);
        ll3.setVisibility(View.VISIBLE);
        createButton.setVisibility(View.VISIBLE);

        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight(), 0);
        animate.setDuration(800);
        animate.setFillAfter(true);
        view.startAnimation(animate);

    }

    private void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getHeight());
        animate.setDuration(800);
        animate.setFillAfter(true);
        view.startAnimation(animate);

        view.setVisibility(View.INVISIBLE);

        ConstraintLayout ll1=(ConstraintLayout) findViewById(R.id.groupNameLayout);
        ConstraintLayout ll2=(ConstraintLayout) findViewById(R.id.contactSeparatorConstraintLayout);
        ConstraintLayout ll3=(ConstraintLayout) findViewById(R.id.contactIdentifierConstraintLayout);
        AppCompatButton createButton=(AppCompatButton) findViewById(R.id.createGroupButton);

        ll1.setVisibility(View.INVISIBLE);
        ll2.setVisibility(View.INVISIBLE);
        ll3.setVisibility(View.INVISIBLE);
        createButton.setVisibility(View.INVISIBLE);
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            if (imm.isAcceptingText()) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                Log.d(TAG, "Software Keyboard was shown");
            } else {
                Log.d(TAG, "Software Keyboard was not shown");
            }
        }catch (Exception e){
            Log.d(TAG, "hideKeyboard: error : "+e.toString());
        }
    }

    public void emitIntent(String group,String groupName) {
        Log.d(TAG, "emitIntent: group : "+group);
        Intent i = new Intent(getApplicationContext(), Display.class);

        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString(Helper.SelectedGroup, group.toString());
        myEdit.putString(Helper.SelectedGroupName, groupName.toString());
        myEdit.commit();

        startActivity(i);
    }

    public void getDataFromSharedPreferences(){
        try {
            sharedPreferences = getSharedPreferences(Helper.MainAppSharedPreferences, MODE_PRIVATE);
            groupsArrayAsString = sharedPreferences.getString(Helper.GroupsDetails, "");
        }catch (Exception e){
            Log.d(TAG, "getDataFromSharedPreferences: error"+e.toString());
        }
    }

    public void displayGroups(){

        groupsArray= new ArrayList<>();
        setGroups(groupsArrayAsString);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext()
                , R.layout.list_group_view, groupsArray);

        list.setAdapter(arrayAdapter);

    }

    public void setGroups(String GroupsArrayAsString){
        try {
            Log.d(TAG, "setGroups: GroupsArrayAsString : "+GroupsArrayAsString);

            String[] groups=GroupsArrayAsString.split("#");

            Log.d(TAG, "setGroups: groups size : "+groups.length);

            totalNumberOfGroup=0;

            for(String group:groups){
                if(group.length()<1) continue;

                String[] groupInfo=group.split("@");

                String groupIndexAsString=groupInfo[0];
                String name=groupInfo[1];
                int groupIndex=Integer.parseInt(groupIndexAsString);

                Log.d(TAG, "setGroups: name :"+name);
                Log.d(TAG, "setGroups: groupIndex : "+groupIndex);

                groupsArray.add(name);

                positionToGroupIndex.put(totalNumberOfGroup,groupIndex);
                groupIndexToGroupName.put(groupIndex,name);

                totalNumberOfGroup++;

                maximumSerialNumber=Math.max(maximumSerialNumber,groupIndex);
            }

            Log.d(TAG, "setGroups: maximumSerialNumber : "+maximumSerialNumber);
        }catch (Exception e){
            Log.d(TAG, "setGroups: error "+e.toString());
        }
    }

    public void deleteGroup(int pos,int id){
        try {

            String[] groups=groupsArrayAsString.split("#");
            int groupIndexToBeDeleting=positionToGroupIndex.get(pos);
            StringBuilder updatedGroupsArrayAsString= new StringBuilder();

            for(String group:groups){
                if(group.length()<1) continue;

                String[] groupInfo=group.split("@");

                String groupName=groupInfo[1];
                String groupIndexAsString=groupInfo[0];
                int groupIndex=Integer.parseInt(groupIndexAsString);

                if(groupIndexToBeDeleting==groupIndex){
                    Log.d(TAG, "delete: groupIndex : "+groupIndex);
                    Log.d(TAG, "delete: groupName : "+groupName);
                }else{
                    updatedGroupsArrayAsString.append(groupIndexAsString).append("@").append(groupName).append("#");
                }
            }

            Log.d(TAG, "deleteGroup: groupsArrayAsString : "+groupsArrayAsString);
            Log.d(TAG, "deleteGroup: updatedGroupsArrayAsString : "+updatedGroupsArrayAsString);

            groupsArrayAsString=updatedGroupsArrayAsString.toString();

            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString(Helper.GroupsDetails,groupsArrayAsString);

            myEdit.commit();

            displayGroups();

        }catch (Exception e){
            Log.d(TAG, "deleteGroup: error : "+e.toString());
        }
    }

}