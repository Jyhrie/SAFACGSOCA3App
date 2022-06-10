package com.example.safacgsoca3app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectPersonnelActivity extends AppCompatActivity {

    public static final String TAG_ID_1 = "a_id";
    public static final String TAG_ID_2 = "b_id";
    public static final String TAG_ID_3 = "c_id";
    public static final String TAG_ID_4 = "d_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_display_personnel);

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
                SelectPersonnelActivity.this, //context
                PersonnelList, //hashmapdata
                R.layout.list_display_personnel, //layout of list
                new String[]{TAG_ID_1, TAG_ID_2}, //from array
                new int[]{R.id.tv_Personnel_Name_Checklist, R.id.tv_Personnel_NRIC_Checklist}); //toarray
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
    }
}
