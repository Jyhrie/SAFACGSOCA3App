package com.example.safacgsoca3app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonnelChecklistActivity extends AppCompatActivity {

    public static final String TAG_ID_1 = "a_id";
    public static final String TAG_ID_2 = "b_id";
    int personnelselected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel_checklist);

        ArrayList<HashMap<String, String>> PersonnelList = new ArrayList<HashMap<String, String>>();
        //Defines an array of hashmaps. Each hashmap contains multiple values associated to one key each
        //PersonnelList is the array of hashmaps

        for (int i = 0; i < 3; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            String Personnel_Name = "ME3 TEO KWEE TECK";
            String Personnel_Remarks = "No remarks";
            //In reality, the number of entries is not defined by the for loop but by database entries
            //For this example, we will manually create 2 entries containing a hardcoded string

            map.put("initialisationvalue", "false");
            map.put(TAG_ID_1, Personnel_Name);
            map.put(TAG_ID_2, Personnel_Remarks);
            //Puts key and data into hashmap
            PersonnelList.add(map);
            //Adds a hashmap into the array
        }

        ListView lv = findViewById(R.id.lv_Personnel_Checklist);
        ListAdapter adapter = new SimpleAdapter(
                PersonnelChecklistActivity.this, //context
                PersonnelList, //hashmapdata
                R.layout.list_personnel_checklist, //layout of list
                new String[]{"initialisationvalue",TAG_ID_1,TAG_ID_2}, //from array
                new int[]{R.id.tvPersonnel_Selected_Status,R.id.Personnel_Name_Checklist, R.id.Personnel_Remarks_Checklist}); //toarray
        // updating listview
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ConstraintLayout layout = ((ConstraintLayout) view.findViewById(R.id.abc));
                TextView sel = (TextView) view.findViewById(R.id.tvPersonnel_Selected_Status);
                Log.i("xd", sel.getText().toString());
                if(sel.getText().toString() == "false")
                {
                    sel.setText("true");
                    layout.setBackgroundResource(R.color.teal);
                }else {
                    sel.setText("false");
                    layout.setBackgroundResource(R.color.white);
                }

            }

        });
    }
}
