package com.example.safacgsoca3app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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


    }

    private void openAddNominalRollDialog(View view)
    {
        fragment_add_nominal_roll fragment = new fragment_add_nominal_roll();
        fragment.show(getSupportFragmentManager(), "fragment_add_nominal_roll");
    }

    /*public void openEditDeleteNominalRoll(View v)
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

    }*/
    public void updateNominalRollAfterAddedEntry(HashMap<String,String> map)
    {
        data.add(map);
        rvAdapter.notifyItemInserted(rvAdapter.getItemCount());
    }

    public void showErrorAlertDialog(View v, String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Error");
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }});
        alert.show();
    }


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

    @Override
    public void onLongItemClick(int position) {
        showErrorAlertDialog("Are you sure you want to remove this person", position);
    }

    public void showErrorAlertDialog(String message, int position)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                removeGuyFromDb(position);
                data.remove(position);
                rvAdapter.notifyItemRemoved(position);
            }});
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }



}