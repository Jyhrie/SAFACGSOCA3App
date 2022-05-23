package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    public static final String TAG_ID = "p_id";
    public static final String TAG_RANK = "p_rank";
    public static final String TAG_NAME = "p_name";
    public static final String TAG_REMARKS = "p_remarks";
    //Defines a key for the data. TAG_ID variable is for ease of changing the key
    //In hashmap, key is column name, index is row
    //Rest of app references key as TAG_ID. Key is currently "a_id"
    //To change the key, change "a_id"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_personnel);

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,p_rank varchar(10) NOT NULL, p_name varchar(255) NOT NULL, p_remarks TEXT NOT NULL)");

        //use this to insert values
        ContentValues content = new ContentValues();

        content.put("p_rank", "LCP");
        content.put("p_name", "ZACKERMAX SEE");
        content.put("p_remarks", "ISO ROOM LOCKED SADGE");

        db.insert("personnel", null, content);
        //end

        Cursor c1 = db.rawQuery("select * from Personnel", null);

        ArrayList<HashMap<String, String>> PersonnelList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_remarks = c1.getString(3);

            map.put(TAG_ID, line_id);
            map.put(TAG_RANK, line_rank);
            map.put(TAG_NAME, line_name);
            map.put(TAG_REMARKS, line_remarks);

            PersonnelList.add(map);
        }
        db.close();

        /*
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
        }*/

        ListView lv = findViewById(R.id.lv_Issue_Detail_Info);
        ListAdapter adapter = new SimpleAdapter(
                DisplayPersonnelActivity.this, //context
                PersonnelList, //hashmapdata
                R.layout.list_display_personnel, //layout of list
                new String[]{TAG_ID,TAG_RANK,TAG_NAME,TAG_REMARKS}, //from array
                new int[]{R.id.Personnel_ID_Checklist, R.id.Personnel_Rank_Checklist, R.id.Personnel_Name_Checklist, R.id.Personnel_Remarks_Checklist}); //toarray
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