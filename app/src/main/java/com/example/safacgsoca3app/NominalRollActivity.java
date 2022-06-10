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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class NominalRollActivity extends AppCompatActivity {

    public static final String TAG_ID = "p_id";
    public static final String TAG_RANK = "p_rank";
    public static final String TAG_NAME = "p_name";
    public static final String TAG_REMARKS = "p_remarks";
    public static final String TAG_P_NRIC = "P_NRIC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_ID);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominal_roll);


        Button btnAddNominalRoll;
        btnAddNominalRoll = (Button) findViewById(R.id.btnAddNominalRoll);

        btnAddNominalRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddNominalRollDialog(view);
            }
        });

        Button btnDeletePersonnel;
        btnDeletePersonnel = (Button) findViewById(R.id.btnDeletePersonnel);

        btnDeletePersonnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                String pid = ((TextView) view.findViewById(R.id.tvPersonnelId)).getText().toString();
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, p_rank varchar(255) NOT NULL, p_name varchar(255) NOT NULL, p_nric text)");
                db.execSQL("DELETE FROM operation WHERE p_id = " + pid);
                db.close();
                onResume();
                */
            }
        });


    }

    private void openAddNominalRollDialog(View view)
    {
        Dialog DialogFragment = new Dialog(NominalRollActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_add_nominal_roll);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        Spinner dropdownRank = (Spinner) DialogFragment.findViewById(R.id.dropdownRank);

        //populate dropdown
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.ranks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownRank.setAdapter(adapter);

        Button btnInsertNominalRoll = (Button) DialogFragment.findViewById(R.id.btn_insert_personnel_close);
        EditText etName = (EditText) DialogFragment.findViewById(R.id.et_personnel_name);
        EditText etRemarks = (EditText) DialogFragment.findViewById(R.id.et_personnel_nric);

        btnInsertNominalRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rank = dropdownRank.getSelectedItem().toString();
                String name = etName.getText().toString();
                String nric = etRemarks.getText().toString();

                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

                ContentValues content = new ContentValues();
                content.put(TAG_RANK, rank);
                content.put(TAG_NAME, name);
                content.put(TAG_P_NRIC, nric);

                db.insert("Personnel", null, content);
                DialogFragment.dismiss();
                onResume();
            }
        });

        Button btnInsertNominalRollNoClose = (Button) DialogFragment.findViewById(R.id.btn_insert_personnel_stay_open);

        btnInsertNominalRollNoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rank = dropdownRank.getSelectedItem().toString();
                String name = etName.getText().toString();
                String nric = etRemarks.getText().toString();

                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

                ContentValues content = new ContentValues();
                content.put(TAG_RANK, rank);
                content.put(TAG_NAME, name);
                content.put(TAG_P_NRIC, nric);

                db.insert("Personnel", null, content);

                etName.setText("");
                etRemarks.setText("");

                db.close();
                onResume();
            }
        });

    }

    public void openEditDeleteNominalRoll(View v)
    {
        //get person id

        Dialog DialogFragment = new Dialog(NominalRollActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_editdelete_nominal_roll);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        Spinner dropdownRank = (Spinner) DialogFragment.findViewById(R.id.dropdownRank);

        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.ranks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownRank.setAdapter(adapter);

        Button btnEditNominalRoll = (Button) DialogFragment.findViewById(R.id.btnEditNominalRoll);
        EditText etName = (EditText) DialogFragment.findViewById(R.id.et_personnel_name);
        EditText etRemarks = (EditText) DialogFragment.findViewById(R.id.et_personnel_nric);

        String rank = dropdownRank.getSelectedItem().toString();
        String name = etName.getText().toString();
        String remarks = etRemarks.getText().toString();



    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db;


        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT * FROM personnel", null);

        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_nric = c1.getString(3);

            map.put(TAG_ID, line_id);
            map.put(TAG_NAME, line_rank + " " + line_name);
            map.put(TAG_P_NRIC, line_nric);

            ammoList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lvNominalRoll);
        ListAdapter adapter = new SimpleAdapter(
                NominalRollActivity.this, //context
                ammoList, //hashmapdata
                R.layout.list_nominalroll, //layout of list
                new String[] { TAG_ID, TAG_NAME, TAG_P_NRIC}, //from array
                new int[] {R.id.tvPersonnelId, R.id.tvPersonnelName, R.id.tvPersonnelRemarks});  //toarray
        // updating listview
        lv.setAdapter(adapter);
    }


}