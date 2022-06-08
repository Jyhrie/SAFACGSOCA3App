package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class OperationNominalRollActivity extends AppCompatActivity {

    private static final String TAG_P_ID = "p_id";
    private static final String TAG_P_NAME = "p_name";
    private static final String TAG_P_NRIC = "p_nric";

    private static final String TAG_O_ID = "o_id";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_KAH = "o_kah";

    private static final String TAG_OP_ID = "op_id";

    private static final String TAG_D_ID = "d_id";

    private static final String TAG_PA_ID = "pa_id";
    private static final String TAG_PA_ISSUE_QTY = "pa_issue_qty";
    private static final String TAG_PA_ISSUED = "pa_issued";
    private static final String TAG_PA_RETURNED = "pa_returned";
    private static final String TAG_PA_EXPENEDED = "pa_expended";
    private static final String TAG_PA_SPOILED = "pa_spoiled";

    private static final String TAG_A_ID = "a_id";
    private static final String TAG_A_NAME = "a_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation_nominal_roll);

        Button btn_operation_nominal_add_personnel;
        btn_operation_nominal_add_personnel = findViewById(R.id.btn_operation_nominal_add_personnel);

        btn_operation_nominal_add_personnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_add_operation_personnel_dialog();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_O_ID);

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("select op.o_id, p.p_rank, p.p_name, p.p_nric from operation_personnel op, personnel p WHERE op.o_id = " + o_id, null);

        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_nric = c1.getString(3);

            map.put(TAG_OP_ID, line_id);
            map.put(TAG_P_NAME, line_rank + " " + line_name);
            map.put(TAG_P_NRIC, line_nric);

            ammoList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lv_operation_nominal_roll);
        ListAdapter adapter = new SimpleAdapter(
                OperationNominalRollActivity.this, //context
                ammoList, //hashmapdata
                R.layout.list_operation_nominal_roll, //layout of list
                new String[]{TAG_OP_ID, TAG_P_NAME, TAG_P_NRIC}, //from array
                new int[]{R.id.tv_personnel_o_id_list_operation_nominal_roll, R.id.tv_personnel_name_list_operation_nominal_roll, R.id.tv_personnel_nric_list_operation_nominal_roll, R.id.tv_personnel_detail_list_operation_nominal_roll});
        // updating listview
        lv.setAdapter(adapter);
    }

    private void show_add_operation_personnel_dialog() {
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
        Cursor c1 = db.rawQuery("SELECT * FROM personnel", null);
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
    }
}

