package com.example.safacgsoca3app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class NominalRollActivity extends AppCompatActivity implements RecyclerViewInterface{

    public static final String TAG_P_ID = "p_id";
    public static final String TAG_P_RANK = "p_rank";
    public static final String TAG_P_NAME = "p_name";
    public static final String TAG_REMARKS = "p_remarks";
    public static final String TAG_P_NRIC = "p_nric";

    adapter_Nominal_Roll rvAdapter;
    ArrayList<HashMap<String,String>> data;

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

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT * FROM personnel", null);
        data = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_nric = c1.getString(3);

            map.put(TAG_P_ID, line_id);
            map.put(TAG_P_NAME, line_rank + " " + line_name);
            map.put(TAG_P_NRIC, line_nric);

            data.add(map);
        }
        db.close();

        RecyclerView rv;
        rv = findViewById(R.id.rvNominalRoll);

        rvAdapter = new adapter_Nominal_Roll(
                this,
                data,
                this
        );

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);

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
                content.put(TAG_P_RANK, rank);
                content.put(TAG_P_NAME, name);
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
                content.put(TAG_P_RANK, rank);
                content.put(TAG_P_NAME, name);
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

    /*
    protected void onNeverCall() {
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
            map.put(TAG_P_NAME, line_rank + " " + line_name);
            map.put(TAG_P_NRIC, line_nric);

            ammoList.add(map);
        }
        db.close();



        ListView lv = findViewById(R.id.lvNominalRoll);
        ListAdapter adapter = new SimpleAdapter(
                NominalRollActivity.this, //context
                ammoList, //hashmapdata
                R.layout.list_nominalroll, //layout of list
                new String[] { TAG_ID, TAG_P_NAME, TAG_P_NRIC}, //from array
                new int[] {R.id.tvPersonnelId, R.id.tvPersonnelName, R.id.tvPersonnelRemarks});  //toarray
        // updating listview
        lv.setAdapter(adapter);
    }*/

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch(direction){
                case ItemTouchHelper.LEFT:
                    removeGuyFromDb(position);
                    data.remove(position);
                    rvAdapter.notifyItemRemoved(position);
                    break;
                case ItemTouchHelper.RIGHT:
                    break;
            }
        }
    };

    public void removeGuyFromDb(int position)
    {
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

        //delete from first layer
        Cursor c1 = db.rawQuery("select op_id from operation_personnel where p_id = " + data.get(position).get(TAG_P_ID), null);
        while(c1.moveToNext())
        {
            db.delete("personnel_ammunition", "op_id = ?", new String[]{c1.getString(0)});
        }

        //delete from 2nd layer
        db.execSQL("DELETE FROM operation_personnel WHERE p_id = "+ data.get(position).get(TAG_P_ID));
        //delete from last layer
        db.execSQL("DELETE FROM personnel WHERE p_id = " + data.get(position).get(TAG_P_ID));
        db.close();
    }


    @Override
    public void onItemClick(int position) {

    }
}