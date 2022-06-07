package com.example.safacgsoca3app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonnelChecklistActivity extends AppCompatActivity {

    public static final String TAG_ID = "p_id";
    public static final String TAG_RANK = "p_rank";
    public static final String TAG_NAME = "p_name";
    public static final String TAG_REMARKS = "p_remarks";
    int initialisationvalue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel_checklist);

        Intent intent = getIntent();
        String Function2 = intent.getStringExtra("Function");

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,p_rank varchar(10) NOT NULL, p_name varchar(255) NOT NULL, p_remarks TEXT NOT NULL)");
        Cursor c1 = db.rawQuery("select * from Personnel", null);

        ArrayList<HashMap<String, String>> PersonnelList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_remarks = c1.getString(3);

            map.put("initialvalue","false");
            map.put(TAG_ID, line_id);
            map.put(TAG_RANK, line_rank);
            map.put(TAG_NAME, line_name);
            map.put(TAG_REMARKS, line_remarks);

            PersonnelList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lv_Personnel_Checklist);
        ListAdapter adapter = new SimpleAdapter(
                PersonnelChecklistActivity.this, //context
                PersonnelList, //hashmapdata
                R.layout.list_personnel_checklist, //layout of list
                new String[]{"initialvalue",TAG_ID,TAG_RANK,TAG_NAME,TAG_REMARKS}, //from array
                new int[]{R.id.tv_Personnel_Selected_Status,
                          R.id.tv_Personnel_ID_Checklist,
                          R.id.tv_Personnel_Rank_Checklist,
                          R.id.tv_Personnel_Name_Checklist,
                          R.id.tv_Personnel_Remarks_Checklist});//toarray

        // updating listview
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ConstraintLayout layout = ((ConstraintLayout) view.findViewById(R.id.ConstraintLayout));
                TextView select = (TextView) view.findViewById(R.id.tv_Personnel_Selected_Status);
                Log.i("xd", select.getText().toString());
                if(select.getText().toString() == "false")
                {
                    select.setText("true");
                    layout.setBackgroundResource(R.color.teal);
                }else {
                    select.setText("false");
                    layout.setBackgroundResource(R.color.white);
                }

            }

        });

        Button btnProceed = (Button) findViewById(R.id.btn_Proceed_Declare_Issue_Expend);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(PersonnelChecklistActivity.this, DeclareIssueInfoActivity.class);
                String Function3 = Function2;
                intent.putExtra("Function3", Function3);
                PersonnelChecklistActivity.this.startActivity(intent);
            }
        });
    }
}
