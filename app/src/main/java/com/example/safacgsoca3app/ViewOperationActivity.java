package com.example.safacgsoca3app;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewOperationActivity extends AppCompatActivity implements RecyclerViewInterface {

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
    private static final String TAG_A_QTY = "a_qty";

    private static final String TAG_ID = "o_id";
    private static final String TAG_PID ="p_id";
    private static final String TAG_NAME = "e_name";
    private static final String TAG_PNAME = "p_name";
    private static final String TAG_PRANK = "p_rank";
    private static final String TAG_KAH = "o_kah";
    private static final String TAG_ANAME = "a_name";
    private static final String TAG_OPID = "op_id";

    public String Selected_o_id;
    public String Selected_d_id;
    public String Selected_d_name;

    private adapter_Personnel_Ammunition assign_personnel_adapter;

    fragment_Add_Edit_Detail fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_operation);

        //get id
        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_ID);
        Selected_o_id = o_id;
        Log.i("o_id = " + o_id, "view_operation_activity");

        TextView tvOperationName;
        TextView tvKAH;
        Button btnViewOperationNominalRoll;
        Button btnViewAmmunition;
        Button btnAddIssueDetail;

        tvOperationName = (TextView) findViewById(R.id.tv_operation_name);
        tvKAH = (TextView) findViewById(R.id.tv_kah);
        btnViewOperationNominalRoll = (Button) findViewById(R.id.btn_view_operation_nominal);
        btnViewAmmunition = (Button) findViewById(R.id.btn_view_ammunition);
        btnAddIssueDetail = (Button) findViewById(R.id.btn_add_issue_detail);

        refreshlvActivityDetail(o_id);

        //pull data from db
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT * FROM operation WHERE o_id = " + o_id, null);

        //dump data into thing
        while(c1.moveToNext()) {
            Log.i("entry found", o_id);
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);
            //String line_unit = c1.getString(2);
            //String line_date = c1.getString(3);
            //String line_location = c1.getString(4);
            String line_kah = c1.getString(2);

            tvOperationName.setText(line_name);
            tvKAH.setText(line_kah);
        }

        db.close();

        btnViewOperationNominalRoll.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), OperationNominalRollActivity.class);
                i.putExtra(TAG_O_ID, o_id);
                startActivity(i);
            }
        });

        btnViewAmmunition.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), AmmunitionActivity.class);
                i.putExtra(TAG_O_ID, o_id);
                startActivity(i);
            }
        });

        btnAddIssueDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDetailDialog(-1, o_id, true);
            }
        });
        
        Button btnIssue= (Button) findViewById(R.id.btn_Issue);
        btnIssue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ViewOperationActivity.this, PersonnelChecklistActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra("o_id", Selected_o_id);
                intent.putExtra("d_id", Selected_d_id);
                intent.putExtra("d_name", Selected_d_name);
                ViewOperationActivity.this.startActivity(intent);
            }
        });

        /*Button btnReturn= (Button) findViewById(R.id.btn_Return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ViewOperationActivity.this, PersonnelChecklistActivity.class);
                String Function = "Returning: ";
                intent.putExtra("Function", Function);
                intent.putExtra("o_id", Selected_o_id);
                intent.putExtra("d_id", Selected_d_id);
                intent.putExtra("d_name", Selected_d_name);
                ViewOperationActivity.this.startActivity(intent);
            }
        });

        Button btnReceive= (Button) findViewById(R.id.btn_Receive);
        btnReceive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showReceiveDialog();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_ID);

        refreshPageAfterEditDetail(o_id, -1);


    }

    public void showAddEditDetailDialog(int d_id, String o_id, boolean reset)
    {
        fragment = new fragment_Add_Edit_Detail();
        Bundle args = new Bundle();
        args.putInt(TAG_D_ID, d_id);
        args.putString(TAG_O_ID, o_id);
        args.putBoolean("reset", reset);
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "fragment_add_edit_detail");

    }

    public void showSelectPersonnelDialog(String o_id, int d_id)
    {
        fragment_Assign_Detail_Personnel fragment = new fragment_Assign_Detail_Personnel();
        Bundle args = new Bundle();
        args.putString(TAG_O_ID, String.valueOf(o_id));
        args.putInt(TAG_D_ID, d_id);
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "fragment_assign_detail_personnel");

    }

    private void showReceiveDialog()
    {
        Dialog ReceiveDialog = new Dialog(ViewOperationActivity.this, android.R.style.Theme_Black_NoTitleBar);
        ReceiveDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100,0,0,0)));
        ReceiveDialog.setContentView(R.layout.dialog_receive);
        ReceiveDialog.setCancelable(true);
        ReceiveDialog.show();


        Button btn_SelectPersonnel = (Button) ReceiveDialog.findViewById(R.id.btn_SelectPersonnel);
        btn_SelectPersonnel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ViewOperationActivity.this, PersonnelChecklistActivity.class);
                String Function = "Receiving: ";
                intent.putExtra("Function", Function);
                intent.putExtra("o_id", Selected_o_id);
                intent.putExtra("d_id", Selected_d_id);
                intent.putExtra("d_name", Selected_d_name);
                ViewOperationActivity.this.startActivity(intent);
                ReceiveDialog.dismiss();
            }
        });

        Button btn_ScanQRCode = (Button) ReceiveDialog.findViewById(R.id.btn_ScanQRCode);
        btn_ScanQRCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReceiveDialog.dismiss();
            }
        });

    }

    public void refreshddlDetail(String o_id, int d_id)
    {
        //update ddl
        Spinner ddl_select_operation_detail = findViewById(R.id.ddl_select_operation_detail);

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("select * from detail where o_id = " + o_id, null);
        ArrayList<HashMap<String, String>> detail_list = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);

            map.put(TAG_D_ID, line_id);
            map.put(TAG_D_NAME, line_name);

            detail_list.add(map);
        }
        db.close();

        SimpleAdapter ddlAdapter = new SimpleAdapter(ViewOperationActivity.this,
                detail_list,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{TAG_D_NAME},
                new int[]{android.R.id.text1});

        ddl_select_operation_detail.setAdapter(ddlAdapter);


        ddl_select_operation_detail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) findViewById(R.id.tv_selected_detail_id)).setText(detail_list.get(i).get(TAG_D_ID));
                refreshlvActivityDetail(o_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //ignore
            }
        });

        //set detail id based on d_id parameter

        if(d_id != -1) {
            TextView tv_selected_detail_id = ((TextView) findViewById(R.id.tv_selected_detail_id));
            tv_selected_detail_id.setText(String.valueOf(d_id));
            //get selection position;
            for (int i = 0; i < detail_list.size(); i++) {
                if (detail_list.get(i).get(TAG_D_ID).equals(d_id)) {
                    ddl_select_operation_detail.setSelection(i);
                    break;
                }
            }
        }


        Button btn_edit_selected_detail;
        btn_edit_selected_detail = findViewById(R.id.btn_edit_selected_detail);
        btn_edit_selected_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddEditDetailDialog(Integer.parseInt(detail_list.get((int) ddl_select_operation_detail.getSelectedItemId()).get(TAG_D_ID)), o_id, true);
            }
        });
    }

    public void refreshlvActivityDetail(String o_id)
    {
        String search_for_detail_id = ((TextView) findViewById(R.id.tv_selected_detail_id)).getText().toString();
        if(!search_for_detail_id.isEmpty()) {
            SQLiteDatabase db;
            db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
            Log.i("select p.p_id, p.p_rank, p.p_name, p.p_nric from personnel p, operation_personnel op where op.p_id = p.p_id and op.o_id = " + o_id + " and op.d_id = " + search_for_detail_id, "query");
            Cursor c1 = db.rawQuery("select p.p_id, p.p_rank, p.p_name, p.p_nric from personnel p, operation_personnel op where op.p_id = p.p_id and op.o_id = " + o_id + " and op.d_id = " + search_for_detail_id, null);

            ArrayList<HashMap<String, String>> PersonnelList = new ArrayList<HashMap<String, String>>();
            while (c1.moveToNext()) {
                HashMap<String, String> map = new HashMap<String, String>();
                String line_id = c1.getString(0);
                String line_rank = c1.getString(1);
                String line_name = c1.getString(2);
                String line_nric = c1.getString(3);

                map.put(TAG_PID, line_id);
                map.put(TAG_PRANK, line_rank);
                map.put(TAG_PNAME, line_name);
                map.put(TAG_P_NRIC, line_nric);

                PersonnelList.add(map);
            }

            c1 = db.rawQuery("select * from detail where d_id = " + search_for_detail_id, null);
            ArrayList<HashMap<String, String>> detail_list = new ArrayList<HashMap<String, String>>();
            while (c1.moveToNext()) {
                Selected_d_id = c1.getString(0);
                Selected_d_name = c1.getString(1);
            }

            db.close();


            ListView lv = findViewById(R.id.lv_Issue_Detail_Info);
            ListAdapter adapter = new SimpleAdapter(
                    ViewOperationActivity.this, //context
                    PersonnelList, //hashmapdata
                    R.layout.list_display_personnel, //layout of list
                    new String[]{TAG_PID, TAG_PRANK, TAG_PNAME, TAG_P_NRIC}, //from array
                    new int[]{R.id.tv_Personnel_ID_Checklist,
                            R.id.tv_Personnel_Rank_Checklist,
                            R.id.tv_Personnel_Name_Checklist,
                            R.id.tv_Personnel_NRIC_Checklist}); //toarray
            // updating listview
            lv.setAdapter(adapter);



            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), DisplayPersonnelInfoActivity.class);
                    intent.putExtra("detail_id", i);//replace with detail id from sql
                    startActivity(intent);
                }

            });
        }
    }

    public void showAssignPersonnelAmmo(String op_id, int o_id)
    {
        fragment_Assign_Personnel_Ammunition fragment = new fragment_Assign_Personnel_Ammunition();
        Bundle args = new Bundle();
        args.putString(TAG_OP_ID, op_id);
        args.putString(TAG_O_ID, String.valueOf(o_id));
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "fragment_assign_personnel_ammunition");

    }

    public void refreshPageAfterEditDetail(String o_id, int d_id)
    {
        refreshddlDetail(o_id, d_id);
        refreshlvActivityDetail(o_id);
    }

    public void refreshFragmentData()
    {
        fragment.refreshData();
    }

    @Override
    public void onItemClick(int position) {

    }
}


