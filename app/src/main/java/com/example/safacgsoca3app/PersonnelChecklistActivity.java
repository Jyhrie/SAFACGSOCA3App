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
import java.util.Map.Entry;

public class PersonnelChecklistActivity extends AppCompatActivity {

    public static final String TAG_ID = "op_id";
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
        String o_id = intent.getStringExtra("o_id");
        String d_id = intent.getStringExtra("d_id");
        String d_name = intent.getStringExtra("d_name");

        TextView tv_D_ID;
        TextView tv_Personnel_Checklist_Detail_Name;

        tv_D_ID = findViewById(R.id.tv_D_ID);
        tv_Personnel_Checklist_Detail_Name = findViewById(R.id.tv_Personnel_Checklist_Detail_Name);

        tv_D_ID.setText(d_id);
        tv_Personnel_Checklist_Detail_Name.setText(d_name);

        ArrayList<HashMap<String, String>> PersonnelList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> PersonnelSelectionMap = new HashMap<String, String>();

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, p_rank varchar(255) NOT NULL, p_name varchar(255) NOT NULL, p_nric text)");
        Cursor c1 = db.rawQuery("select op.op_id, p.p_rank, p.p_name, p.p_nric from personnel p, operation_personnel op where op.p_id = p.p_id and op.o_id = " + o_id + " and op.d_id = " + d_id, null);


        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_remarks = c1.getString(3);

            map.put("initialvalue", "false");
            map.put(TAG_ID, line_id);
            map.put(TAG_RANK, line_rank);
            map.put(TAG_NAME, line_name);
            map.put(TAG_REMARKS, line_remarks);
            map.put("SELECTED", "FALSE");
            PersonnelSelectionMap.put(line_id, "0");

            PersonnelList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lv_Personnel_Checklist);
        ListAdapter adapter = new SimpleAdapter(
                PersonnelChecklistActivity.this, //context
                PersonnelList, //hashmapdata
                R.layout.list_personnel_checklist, //layout of list
                new String[]{"initialvalue", TAG_ID, TAG_RANK, TAG_NAME, TAG_REMARKS}, //from array
                new int[]{R.id.tv_Personnel_Selected_Status,
                        R.id.tv_Personnel_ID_Checklist,
                        R.id.tv_Personnel_Rank_Checklist,
                        R.id.tv_Personnel_Name_Checklist,
                        R.id.tv_Personnel_NRIC_Checklist});//toarray

        // updating listview
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ConstraintLayout layout = ((ConstraintLayout) view.findViewById(R.id.ConstraintLayout));
                TextView select = (TextView) view.findViewById(R.id.tv_Personnel_Selected_Status);
                TextView selectid = (TextView) view.findViewById(R.id.tv_Personnel_ID_Checklist);

                if (select.getText().toString() == "false") {
                    select.setText("true");
                    layout.setBackgroundResource(R.color.teal);
                    PersonnelSelectionMap.put(selectid.getText().toString(), "1");
                    Log.i(" ", String.valueOf(PersonnelSelectionMap));
                    ;
                } else {
                    select.setText("false");
                    layout.setBackgroundResource(R.color.white);
                    PersonnelSelectionMap.put(selectid.getText().toString(), "0");
                    Log.i(" ", String.valueOf(PersonnelSelectionMap));
                }
            }
        });

        Button btnProceed = (Button) findViewById(R.id.btn_Proceed_Declare_Issue_Expend);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                for(Entry<String, String> entry: PersonnelSelectionMap.entrySet())
                {
                    if (entry.getValue() == "1")
                    {
                        for (int i = 0; i<PersonnelList.size(); i++)
                        {
                            if(PersonnelList.get(i).get(TAG_ID).equals(entry.getKey()))
                            {
                                PersonnelList.get(i).put("SELECTED", "TRUE");
                                Log.i(" ", "Selected id: " + entry.getKey());
                            }
                        }
                    }
                    else
                    {
                        for (int i = 0; i<PersonnelList.size(); i++)
                        {
                            if(PersonnelList.get(i).get(TAG_ID).equals(entry.getKey()))
                            {
                                PersonnelList.get(i).put("SELECTED", "FALSE");
                            }
                        }
                    }
                }

                Log.i(" ", String.valueOf(PersonnelList));

                ArrayList<HashMap<String, String>> PersonnelListAppended = new ArrayList<HashMap<String, String>>();
                for (int i = 0 ; i<PersonnelList.size(); i++)
                {
                    if (PersonnelList.get(i).get("SELECTED").equals("TRUE"))
                    {
                        PersonnelListAppended.add(PersonnelList.get(i));
                    }
                }

                Log.i(" ", String.valueOf(PersonnelListAppended));



                Intent intent = new Intent(PersonnelChecklistActivity.this, DeclareIssueReturnReceiveInfoActivity.class);
                String Function3 = Function2;
                intent.putExtra("Function3", Function3);
                intent.putExtra("o_id", o_id);
                intent.putExtra("d_id", d_id);
                intent.putExtra("d_name", d_name);
                intent.putExtra("PersonnelList", PersonnelListAppended);
                PersonnelChecklistActivity.this.startActivity(intent);
            }
        });
    }
}
