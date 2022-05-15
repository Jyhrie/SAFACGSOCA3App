package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewOperationActivity extends AppCompatActivity {

    private static final String TAG_ID = "e_id";
    private static final String TAG_PID ="p_id";
    private static final String TAG_NAME = "e_name";
    private static final String TAG_PNAME = "p_name";
    private static final String TAG_PRANK = "p_rank";
    private static final String TAG_KAH = "e_kah";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_operation);

        //get id
        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_ID);

        TextView tvOperationName;
        TextView tvDateLocation;
        TextView tvKAH;
        Button btnViewNominalRoll;
        Button btnViewAmmunition;

        tvOperationName = (TextView) findViewById(R.id.tvOperationName);
        tvDateLocation = (TextView) findViewById(R.id.tvDateLocation);
        tvKAH = (TextView) findViewById(R.id.tvKAH);
        btnViewNominalRoll = (Button) findViewById(R.id.btnViewNominalRoll);
        btnViewAmmunition = (Button) findViewById(R.id.btnViewAmmunition);

        //pull data from db
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS operation (o_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, o_name varchar(255) NOT NULL, o_unit varchar(255) NOT NULL, o_date text NOT NULL, o_location text NOT NULL, o_kah text NOT NULL)");
        Cursor c1 = db.rawQuery("SELECT * FROM operation WHERE o_id = " + o_id, null);

        //dump data into thing
        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
        c1.moveToLast();
        String line_id = c1.getString(0);
        String line_name = c1.getString(1);
        String line_unit = c1.getString(2);
        String line_date = c1.getString(3);
        String line_location = c1.getString(4);
        String line_kah = c1.getString(5);

        db.close();

        tvOperationName.setText(line_unit + " - " + line_name);
        tvDateLocation.setText(line_date + " @ " + line_location);
        tvKAH.setText(line_kah);

        btnViewNominalRoll.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), NominalRollActivity.class);
                startActivity(i);
            }
        });

        btnViewAmmunition.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), AmmunitionActivity.class);
                startActivity(i);
            }
        });


    }

    private void showAddDetailDialog(View view)
    {
        EditText etDetailName;
        EditText etDetailAmmmoQuantity;
        Spinner dropdownDetailAmmunition;
        Button btnCreateDetail;
        Button btnSelectPersonnel;

        Dialog DialogFragment = new Dialog(ViewOperationActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_add_issuing_detail);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        etDetailName = (EditText) DialogFragment.findViewById(R.id.etDetailName);

        /*
        etDetailAmmmoQuantity = (EditText) DialogFragment.findViewById(R.id.etDetailAmmoQuantity);
        dropdownDetailAmmunition = (Spinner) DialogFragment.findViewById(R.id.dropdownDetailAmmunition);
        */

        btnCreateDetail = (Button) DialogFragment.findViewById(R.id.btnCreateDetail);
        btnSelectPersonnel =(Button) DialogFragment.findViewById(R.id.btnSelectPersonnel);

        String DetailName = String.valueOf(etDetailName);



        btnCreateDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnSelectPersonnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showSelectPersonnelDialog();
            }
        });
    }

    private void showSelectPersonnelDialog()
    {
        Dialog DialogFragment = new Dialog(ViewOperationActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_select_personnel);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,p_rank varchar(10) NOT NULL, p_name varchar(255) NOT NULL, p_remarks TEXT NOT NULL)");
        Cursor c1 = db.rawQuery("select * from Personnel", null);

        ArrayList<HashMap<String, String>> personnelList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);

            map.put(TAG_PID, line_id);
            map.put(TAG_NAME, line_rank + " " + line_name);

            personnelList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lvSelectPersonnel);
        ListAdapter adapter = new SimpleAdapter(
                ViewOperationActivity.this, //context
                personnelList, //hashmapdata
                R.layout.list_select_personnel, //layout of list
                new String[] { TAG_PID, TAG_PNAME}, //from array
                new int[] {R.id.tvListPersonnelId, R.id.tvListPersonnelName}); //toarray
        // updating listview
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView tvListPersonnelId = (TextView) view.findViewById(R.id.tvListPersonnelId);
                    //toggle and add id to array;

                }
        });
    }

}