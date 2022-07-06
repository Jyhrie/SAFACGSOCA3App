package com.example.safacgsoca3app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_nominal_roll);

        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_O_ID);

        FloatingActionButton btn_operation_nominal_add_personnel;
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


        RecyclerView rv;
        rvAdapter = new adapter_Operation_Nominal_Roll(
                this,
                data,
                this

        );

        rv = findViewById(R.id.rv_operation_nominal_roll);

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);


        /*ListView lv = findViewById(R.id.rv_operation_nominal_roll);
        ListAdapter adapter = new SimpleAdapter(
                OperationNominalRollActivity.this, //context
                ammoList, //hashmapdata
                R.layout.list_operation_nominal_roll, //layout of list
                new String[]{TAG_OP_ID, TAG_P_NAME, TAG_P_NRIC, TAG_D_ID}, //from array
                new int[]{R.id.tv_personnel_o_id_list_operation_nominal_roll, R.id.tv_personnel_name_list_operation_nominal_roll, R.id.tv_personnel_nric_list_operation_nominal_roll, R.id.tv_personnel_detail_list_operation_nominal_roll});
        // updating listview
        lv.setAdapter(adapter);*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_O_ID);
        refreshData(o_id);


    }

    private void show_add_operation_personnel_dialog(String o_id) {
        Dialog DialogFragment = new Dialog(OperationNominalRollActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_add_operation_personnel);
        DialogFragment.setCancelable(true);
        DialogFragment.show();


        ArrayList<HashMap<String, String>> personnel_list = new ArrayList<HashMap<String, String>>();
        //Defines an array of hashmaps. Each hashmap contains multiple values associated to one key each
        //PersonnelList is the array of hashmaps


        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT p.p_id, p.p_rank, p.p_name, p.p_nric FROM personnel p WHERE p.p_id NOT IN (SELECT p.p_id FROM personnel p, operation_personnel op WHERE op.p_id = p.p_id AND op.o_id = "+ o_id+")", null);
        while (c1.moveToNext()) {

            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_nric = c1.getString(3);

            map.put(TAG_P_ID, line_id);
            map.put(TAG_P_NAME, line_rank + " " + line_name);
            map.put(TAG_P_NRIC, line_nric);

            personnel_list.add(map);
            //Adds a hashmap into the array
        }


        ListView lv = DialogFragment.findViewById(R.id.lv_add_operation_personnel);
        ListAdapter adapter = new SimpleAdapter(
                OperationNominalRollActivity.this, //context
                personnel_list, //hashmapdata
                R.layout.list_add_operation_personnel, //layout of list
                new String[]{TAG_P_ID, TAG_P_NAME, TAG_P_NRIC}, //from array
                new int[]{R.id.tv_add_operation_personnel_list_p_id, R.id.tv_add_operation_personnel_list_name, R.id.tv_add_operation_personnel_nric}); //toarray
        // updating listview
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckBox cb = view.findViewById(R.id.cb_add_operation_personnel_isselected);
                cb.setChecked(!cb.isChecked());

                LinearLayout layout = view.findViewById(R.id.layout_list_add_operation_personnel);
                if(cb.isChecked()) {
                    layout.setBackgroundResource(R.color.teal_200);
                }
                else
                {
                    layout.setBackgroundResource(R.color.white);
                }
            }

        });

        Button btn_add_operation_personnel_submit;
        btn_add_operation_personnel_submit = DialogFragment.findViewById(R.id.btn_add_operation_personnel_submit);
        btn_add_operation_personnel_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

                for(int i = 0; i<lv.getCount(); i++)
                {
                    View v = lv.getChildAt(i);
                    if(((CheckBox) v.findViewById(R.id.cb_add_operation_personnel_isselected)).isChecked())
                    {

                        String line_p_id = (((TextView) v.findViewById(R.id.tv_add_operation_personnel_list_p_id)).getText().toString());
                        Log.i("CHECKED", line_p_id);
                        ContentValues content = new ContentValues();
                        content.put(TAG_P_ID, line_p_id);
                        content.put(TAG_O_ID, o_id);

                        db.insert("operation_personnel", null, content);
                    }
                }
                db.close();
                refreshData(o_id);
                DialogFragment.dismiss();
            }
        });
    }

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
                    Log.i(String.valueOf(data.size()), String.valueOf(position));
                    removeGuyFromDB(position);
                    data.remove(position);
                    rvAdapter.notifyItemRemoved(position);
                    break;
                case ItemTouchHelper.RIGHT:
                    break;
            }
        }
    };

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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);


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
}

