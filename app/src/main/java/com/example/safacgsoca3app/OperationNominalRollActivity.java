package com.example.safacgsoca3app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class OperationNominalRollActivity extends AppCompatActivity implements RecyclerViewInterface{

    private static final String TAG_P_ID = "p_id";
    private static final String TAG_P_NAME = "p_name";
    private static final String TAG_P_NRIC = "p_nric";

    private static final String TAG_O_ID = "o_id";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_KAH = "o_kah";

    private static final String TAG_OP_ID = "op_id";

    private static final String TAG_D_ID = "d_id";
    private static final String TAG_D_NAME = "d_name";

    private static final String TAG_PA_ID = "pa_id";
    private static final String TAG_PA_ISSUE_QTY = "pa_issue_qty";
    private static final String TAG_PA_ISSUED = "pa_issued";
    private static final String TAG_PA_RETURNED = "pa_returned";
    private static final String TAG_PA_EXPENEDED = "pa_expended";
    private static final String TAG_PA_SPOILED = "pa_spoiled";

    private static final String TAG_A_ID = "a_id";
    private static final String TAG_A_NAME = "a_name";

    ArrayList<HashMap<String,String>> data;
    adapter_Operation_Nominal_Roll rvAdapter;
    fragment_add_operation_personnel fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_nominal_roll);

        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_O_ID);

        ExtendedFloatingActionButton btn_operation_nominal_add_personnel;
        btn_operation_nominal_add_personnel = findViewById(R.id.btn_operation_nominal_add_personnel);

        btn_operation_nominal_add_personnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_add_operation_personnel_dialog(o_id);
            }
        });

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT op.op_id, p.p_rank, p.p_name, p.p_nric, d.d_name FROM personnel p LEFT OUTER JOIN operation_personnel op ON op.p_id = p.p_id LEFT OUTER JOIN detail d ON d.d_id = op.d_id WHERE op.o_id = " + o_id , null);

        data = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            Log.i(c1.getString(0),"test2");
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_nric = c1.getString(3);
            String line_detail_name = c1.getString(4);

            if(line_detail_name == null)
            {
                line_detail_name = "UNASSIGNED DETAIL";
            }

            map.put(TAG_OP_ID, line_id);
            map.put(TAG_P_NAME, line_rank + " " + line_name);
            map.put(TAG_P_NRIC, line_nric);
            map.put(TAG_D_NAME, line_detail_name);

            data.add(map);
        }
        db.close();
        Log.i(String.valueOf(data.size()), "str");

        SearchView sv_operation_nominal_roll = (SearchView) findViewById(R.id.sv_operation_nominal_roll);
        sv_operation_nominal_roll.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });

        RecyclerView rv;
        rvAdapter = new adapter_Operation_Nominal_Roll(
                this,
                data,
                this

        );

        rv = findViewById(R.id.rv_operation_nominal_roll);

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    private void filter(String text)
    {
        ArrayList<HashMap<String,String>> filteredList = new ArrayList<>();
        for(HashMap<String,String> item : data)
        {
            if(item.get(TAG_P_NAME).toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }

        rvAdapter.filterList(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_O_ID);
        refreshData(o_id);


    }

    private void show_add_operation_personnel_dialog(String o_id) {
        fragment = new fragment_add_operation_personnel();
        Bundle args = new Bundle();
        args.putString(TAG_O_ID, o_id);
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "fragment_add_edit_detail");
    }


    public void refreshData(String o_id)
    {

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT op.op_id, p.p_rank, p.p_name, p.p_nric, d.d_name FROM personnel p LEFT OUTER JOIN operation_personnel op ON op.p_id = p.p_id LEFT OUTER JOIN detail d ON d.d_id = op.d_id WHERE op.o_id = " + o_id , null);

        data = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            Log.i(c1.getString(0),"test2");
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_nric = c1.getString(3);
            String line_detail_name = c1.getString(4);

            if(line_detail_name == null)
            {
                line_detail_name = "UNASSIGNED DETAIL";
            }

            map.put(TAG_OP_ID, line_id);
            map.put(TAG_P_NAME, line_rank + " " + line_name);
            map.put(TAG_P_NRIC, line_nric);
            map.put(TAG_D_NAME, line_detail_name);

            data.add(map);
        }
        RecyclerView rv;
        rvAdapter = new adapter_Operation_Nominal_Roll(
                this,
                data,
                this
        );

        rv = findViewById(R.id.rv_operation_nominal_roll);

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));


    }

    public void removeGuyFromDB(int position)
    {
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("DELETE FROM personnel_ammunition WHERE op_id = " + data.get(position).get(TAG_OP_ID));
        db.execSQL("DELETE FROM operation_personnel WHERE op_id = " + data.get(position).get(TAG_OP_ID));
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
                removeGuyFromDB(position);
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

