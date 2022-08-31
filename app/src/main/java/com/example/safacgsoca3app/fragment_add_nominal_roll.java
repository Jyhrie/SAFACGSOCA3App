package com.example.safacgsoca3app;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_add_nominal_roll extends DialogFragment {

    NominalRollActivity source;

    public static final String TAG_P_ID = "p_id";
    public static final String TAG_P_RANK = "p_rank";
    public static final String TAG_P_NAME = "p_name";
    public static final String TAG_REMARKS = "p_remarks";
    public static final String TAG_P_NRIC = "p_nric";
    public static final String TAG_DDL_RANK_NAME = "ddl_rank_name";
    public static final String TAG_DDL_RANK_ID = "ddl_rank_id";

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_add_nominal_roll, container, false);
        Context context = getContext();

        source = (NominalRollActivity) getActivity();

        Spinner dropdownRank = (Spinner) v.findViewById(R.id.dropdownRank);

        //populate dropdown

        //get dropdown from database
        SQLiteDatabase db;
        ArrayList<HashMap<String,String>> ddlData= new ArrayList<>();
        db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT ddl_rank_id, ddl_rank_name FROM ddl_rank ORDER BY ddl_rank_hierarchy ASC", null);
        while(c1.moveToNext())
        {
            HashMap<String,String> map = new HashMap<>();
            map.put(TAG_DDL_RANK_ID, c1.getString(0));
            map.put(TAG_DDL_RANK_NAME, c1.getString(1));
            ddlData.add(map);
        }

        SimpleAdapter ddlAdapter = new SimpleAdapter(context,
                ddlData,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{TAG_DDL_RANK_NAME},
                new int[]{android.R.id.text1});
        dropdownRank.setAdapter(ddlAdapter);

        /*
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.ranks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownRank.setAdapter(adapter);
        */

        Button btnInsertNominalRoll = (Button) v.findViewById(R.id.btn_insert_personnel_close);
        EditText etName = (EditText) v.findViewById(R.id.et_personnel_name);
        EditText etRemarks = (EditText) v.findViewById(R.id.et_personnel_nric);

        btnInsertNominalRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rank = dropdownRank.getSelectedItem().toString();
                String name = etName.getText().toString();
                String nric = etRemarks.getText().toString();

                //data validation
                boolean _dataValidationPass = true;
                String _errorMessage = "there is no empty field, something went wrong";
                if (rank.isEmpty()) {
                    _dataValidationPass = false;
                    _errorMessage = "this is a dropdown field, something went wrong";
                } else if (name.isEmpty()) {
                    _dataValidationPass = false;
                    _errorMessage = "Please insert a personnel name";
                } else if (nric.isEmpty()) {
                    _dataValidationPass = false;
                    _errorMessage = "Please insert an NRIC number";
                }

                if (_dataValidationPass == false) {
                    showErrorAlertDialog(v, _errorMessage);
                    return;
                }


                SQLiteDatabase db;
                db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);

                ContentValues content = new ContentValues();
                content.put(TAG_P_RANK, rank);
                content.put(TAG_P_NAME, name);
                content.put(TAG_P_NRIC, nric);

                db.insert("Personnel", null, content);

                Cursor c1 = db.rawQuery("select * from personnel order by p_id DESC limit 1", null);
                HashMap<String, String> map = new HashMap<String, String>();
                if (c1.moveToFirst()) {
                    String line_id = c1.getString(0);
                    String line_rank = c1.getString(1);
                    String line_name = c1.getString(2);
                    String line_nric = c1.getString(3);

                    map.put(TAG_P_ID, line_id);
                    map.put(TAG_P_NAME, line_rank + " " + line_name);
                    map.put(TAG_P_NRIC, line_nric);
                }
                db.close();
                source.updateNominalRollAfterAddedEntry(map);
                dismiss();

            }
        });

        Button btnInsertNominalRollNoClose = (Button) v.findViewById(R.id.btn_insert_personnel_stay_open);

        btnInsertNominalRollNoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rank = dropdownRank.getSelectedItem().toString();
                String name = etName.getText().toString();
                String nric = etRemarks.getText().toString();

                boolean _dataValidationPass = true;
                String _errorMessage = "there is no empty field, something went wrong";
                if(rank.isEmpty())
                {
                    _dataValidationPass = false;
                    _errorMessage = "this is a dropdown field, something went wrong";
                }
                else if(name.isEmpty())
                {
                    _dataValidationPass = false;
                    _errorMessage = "Please insert a personnel name";
                }
                else if(nric.isEmpty())
                {
                    _dataValidationPass = false;
                    _errorMessage = "Please insert an NRIC number";
                }

                if(_dataValidationPass == false)
                {
                    showErrorAlertDialog(view, _errorMessage);
                    return;
                }

                SQLiteDatabase db;
                db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);

                ContentValues content = new ContentValues();
                content.put(TAG_P_RANK, rank);
                content.put(TAG_P_NAME, name);
                content.put(TAG_P_NRIC, nric);

                db.insert("Personnel", null, content);

                Cursor c1 = db.rawQuery("select * from personnel order by p_id DESC limit 1", null);
                HashMap<String, String> map = new HashMap<String, String>();
                if (c1.moveToFirst()) {

                    String line_id = c1.getString(0);
                    String line_rank = c1.getString(1);
                    String line_name = c1.getString(2);
                    String line_nric = c1.getString(3);

                    map.put(TAG_P_ID, line_id);
                    map.put(TAG_P_NAME, line_rank + " " + line_name);
                    map.put(TAG_P_NRIC, line_nric);
                }
                source.updateNominalRollAfterAddedEntry(map);

                etName.setText("");
                etRemarks.setText("");

                db.close();
            }
        });

        return v;
    }
            public void showErrorAlertDialog(View v, String message) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage(message);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }


}
