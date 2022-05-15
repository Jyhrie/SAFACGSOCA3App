package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

public class NominalRollActivity extends AppCompatActivity {

    public static final String TAG_ID = "p_id";
    public static final String TAG_RANK = "p_rank";
    public static final String TAG_NAME = "p_name";
    public static final String TAG_REMARKS = "p_remarks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        Button btnInsertNominalRoll = (Button) DialogFragment.findViewById(R.id.btnInsertNominalRoll);
        EditText etName = (EditText) DialogFragment.findViewById(R.id.etName);
        EditText etRemarks = (EditText) DialogFragment.findViewById(R.id.etRemarks);

        btnInsertNominalRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rank = dropdownRank.getSelectedItem().toString();
                String name = etName.getText().toString();
                String remarks = etRemarks.getText().toString();

                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS Personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,p_rank varchar(10) NOT NULL, p_name varchar(255) NOT NULL, p_remarks TEXT NOT NULL)");

                ContentValues content = new ContentValues();
                content.put(TAG_NAME, name);
                content.put(TAG_RANK, rank);
                content.put(TAG_REMARKS, remarks);

                db.insert("Personnel", null, content);
                DialogFragment.dismiss();
            }
        });

        Button btnInsertNominalRollNoClose = (Button) DialogFragment.findViewById(R.id.btnInsertNominalRollNoClose);

        btnInsertNominalRollNoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rank = dropdownRank.getSelectedItem().toString();
                String name = etName.getText().toString();
                String remarks = etRemarks.getText().toString();

                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS Personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,p_rank varchar(10) NOT NULL, p_name varchar(255) NOT NULL, p_remarks TEXT NOT NULL)");

                ContentValues content = new ContentValues();
                content.put(TAG_NAME, name);
                content.put(TAG_RANK, rank);
                content.put(TAG_REMARKS, remarks);

                db.insert("Personnel", null, content);

                etName.setText("");
                etRemarks.setText("");
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
        EditText etName = (EditText) DialogFragment.findViewById(R.id.etName);
        EditText etRemarks = (EditText) DialogFragment.findViewById(R.id.etRemarks);

        String rank = dropdownRank.getSelectedItem().toString();
        String name = etName.getText().toString();
        String remarks = etRemarks.getText().toString();



    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,p_rank varchar(10) NOT NULL, p_name varchar(255) NOT NULL, p_remarks TEXT NOT NULL)");
        Cursor c1 = db.rawQuery("select * from Personnel", null);

        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
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

            ammoList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lvNominalRoll);
        ListAdapter adapter = new SimpleAdapter(
                NominalRollActivity.this, //context
                ammoList, //hashmapdata
                R.layout.list_nominalroll, //layout of list
                new String[] { TAG_ID, TAG_RANK, TAG_NAME, TAG_REMARKS}, //from array
                new int[] {R.id.tvPersonnelId, R.id.tvPersonnelRank, R.id.tvPersonnelName, R.id.tvPersonnelRemarks}); //toarray
        // updating listview
        lv.setAdapter(adapter);

    }


}