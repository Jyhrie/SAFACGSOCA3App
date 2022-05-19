package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayPersonnelActivity extends AppCompatActivity {

    public static final String TAG_ID_1 = "a_id";
    public static final String TAG_ID_2 = "b_id";
    //Defines a key for the data. TAG_ID variable is for ease of changing the key
    //In hashmap, key is column name, index is row
    //Rest of app references key as TAG_ID. Key is currently "a_id"
    //To change the key, change "a_id"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_personnel);

        ArrayList<HashMap<String, String>> PersonnelList = new ArrayList<HashMap<String, String>>();
        //Defines an array of hashmaps. Each hashmap contains multiple values associated to one key each
        //PersonnelList is the array of hashmaps

        for (int i = 0; i < 3; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            String Personnel_Name = "ME3 TEO KWEE TECK";
            String Personnel_Remarks = "No remarks";
            //In reality, the number of entries is not defined by the for loop but by database entries
            //For this example, we will manually create 2 entries containing a hardcoded string

            map.put(TAG_ID_1, Personnel_Name);
            map.put(TAG_ID_2, Personnel_Remarks);
            //Puts key and data into hashmap
            PersonnelList.add(map);
            //Adds a hashmap into the array
        }

        ListView lv = findViewById(R.id.lv_Issue_Detail_Info);
        ListAdapter adapter = new SimpleAdapter(
                DisplayPersonnelActivity.this, //context
                PersonnelList, //hashmapdata
                R.layout.list_display_personnel, //layout of list
                new String[]{TAG_ID_1,TAG_ID_2}, //from array
                new int[]{R.id.Personnel_Name_Checklist, R.id.Personnel_Remarks_Checklist}); //toarray
        // updating listview
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DisplayPersonnelInfoActivity.class);
                intent.putExtra("detail_id", i);//replace with detail id from sql
                startActivity(intent);
            }

        });

        Button btnIssue= (Button) findViewById(R.id.btn_Issue);
        btnIssue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(DisplayPersonnelActivity.this, PersonnelChecklistActivity.class);
                DisplayPersonnelActivity.this.startActivity(i);
            }
        });

        Button btnReturn= (Button) findViewById(R.id.btn_Return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(DisplayPersonnelActivity.this, PersonnelChecklistActivity.class);
                DisplayPersonnelActivity.this.startActivity(i);
            }
        });

        Button btnReceive= (Button) findViewById(R.id.btn_Receive);
        btnReceive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showReceiveDialog();
            }
        });
    }

    private void showReceiveDialog()
    {
        Dialog DialogFragment = new Dialog(DisplayPersonnelActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100,0,0,0)));
        DialogFragment.setContentView(R.layout.dialog_receive);
        DialogFragment.setCancelable(true);
        DialogFragment.show();


        Button btn_SelectPersonnel = (Button) DialogFragment.findViewById(R.id.btn_SelectPersonnel);
        btn_SelectPersonnel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(DisplayPersonnelActivity.this, PersonnelChecklistActivity.class);
                DisplayPersonnelActivity.this.startActivity(i);
            }
        });

        Button btn_ScanQRCode = (Button) DialogFragment.findViewById(R.id.btn_ScanQRCode);
        btn_ScanQRCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

    }
}