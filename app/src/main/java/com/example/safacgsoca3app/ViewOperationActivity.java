package com.example.safacgsoca3app;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
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
    private static final String TAG_DOC_NUMBER = "doc_id";
    private static final String TAG_STATE = "state";
    private static final String TAG_PAST_DOC = "past_doc";

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
        TextView tvDateLoc;
        Button btnViewOperationNominalRoll;
        Button btnViewAmmunition;
        Button btnAddIssueDetail;

        tvOperationName = (TextView) findViewById(R.id.tv_operation_name);
        tvKAH = (TextView) findViewById(R.id.tv_kah);
        tvDateLoc = (TextView) findViewById(R.id.tv_dateloc);
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
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);
            String line_kah = c1.getString(2);
            String line_unit = c1.getString(3);
            String line_date = c1.getString(4);
            String line_location = c1.getString(5);

            tvOperationName.setText(line_name + " - " + line_unit);
            tvDateLoc.setText(line_date + " @ " + line_location);
            tvKAH.setText(line_kah);
        }

        db.close();

        Button btnEditOperationDetails = findViewById(R.id.btn_edit_operation_details);
        btnEditOperationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_Add_Operation fragment = new fragment_Add_Operation();
                Bundle args = new Bundle();
                args.putString(TAG_STATE, "1");
                args.putString(TAG_O_ID, o_id);
                fragment.setArguments(args);
                fragment.show(getSupportFragmentManager(), "fragment_assign_personnel_ammunition");
            }
        });

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
                intent.putExtra("type", "1");
                intent.putExtra("o_id", Selected_o_id);
                intent.putExtra("d_id", Selected_d_id);
                intent.putExtra("d_name", Selected_d_name);
                ViewOperationActivity.this.startActivity(intent);
            }
        });

        Button btnRecieve= (Button) findViewById(R.id.btn_Recieve);
        btnRecieve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showReceiveDialog();
            }
        });

        Button btnGenerateDocuments= (Button) findViewById(R.id.btn_generate_documents);
        btnGenerateDocuments.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GenerateDocumentsActivity.class);
                String doc_o_name = null;
                String doc_o_unit = null;
                String doc_d_name = null;

                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                Cursor c1 = db.rawQuery("select o_name, o_unit from operation where o_id = " + o_id, null);
                if (c1.moveToFirst()) {
                    doc_o_name = c1.getString(0);
                    doc_o_unit = c1.getString(1);
                }
                c1 = db.rawQuery("select d_name from detail d where d_id = ?", new String[]{Selected_d_id});
                if (c1.moveToFirst()) {
                    doc_d_name = c1.getString(0);
                }
                Log.i("bindvalues", doc_o_name + doc_o_unit + doc_d_name);
                //check if document already exists (CHECK ALL STRING PARAMS IN CASE OF DETAIL CHANGE, THUS CREATE NEW DOC IF CHANGE OCCURS)
                Log.i("select doc_id from document where o_name = ? and o_unit = ? and d_name = ? and doc_closed = 0", String.valueOf(new String[]{doc_o_name, doc_o_unit, doc_d_name}));
                c1 = db.rawQuery("select doc_id from document where o_name = ? and o_unit = ? and d_name = ? and doc_closed = 0", new String[]{doc_o_name, doc_o_unit, doc_d_name});

                if(c1.moveToFirst())
                {
                    i.putExtra(TAG_DOC_NUMBER, c1.getString(0));
                    i.putExtra(TAG_PAST_DOC, "0");
                    startActivity(i);
                }
                else
                {
                    showErrorAlertDialog(v, "No current open documents for this detail");
                }
                db.close();

            }
        });
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
                intent.putExtra("type", "2");
                intent.putExtra("o_id", Selected_o_id);
                intent.putExtra("d_id", Selected_d_id);
                intent.putExtra("d_name", Selected_d_name);
                ViewOperationActivity.this.startActivity(intent);
                ReceiveDialog.dismiss();
            }
        });

        /*Button btn_ScanQRCode = (Button) ReceiveDialog.findViewById(R.id.btn_ScanQRCode);
        btn_ScanQRCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ReceiveDialog.dismiss();
            }
        });*/

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
                if(detail_list.size()==0)
                {
                    showErrorAlertDialog(view, "Please Create a new Detail");
                    return;
                }
                showAddEditDetailDialog(Integer.parseInt(detail_list.get((int) ddl_select_operation_detail.getSelectedItemId()).get(TAG_D_ID)), o_id, true);
            }
        });

        Button btn_delete_selected_detail;
        btn_delete_selected_detail = findViewById(R.id.btn_delete_selected_detail);
        btn_delete_selected_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(detail_list.size()==0)
                {
                    showErrorAlertDialog(view, "Please Create a new Detail");
                    return;
                }
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.execSQL("DELETE FROM Detail WHERE d_id = "+ detail_list.get((int) ddl_select_operation_detail.getSelectedItemId()).get(TAG_D_ID));
                db.execSQL("UPDATE Operation_Personnel SET d_id = null WHERE d_id = "+ detail_list.get((int) ddl_select_operation_detail.getSelectedItemId()).get(TAG_D_ID));
                db.close();
                refreshPageAfterEditDetail(o_id, 0);
            }
        });
    }

    public void refreshlvActivityDetail(String o_id)
    {
        String search_for_detail_id = ((TextView) findViewById(R.id.tv_selected_detail_id)).getText().toString();
        if(!search_for_detail_id.isEmpty()) {
            SQLiteDatabase db;
            db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
            Log.i("select op.op_id, p.p_rank, p.p_name, p.p_nric from personnel p, operation_personnel op where op.p_id = p.p_id and op.o_id = " + o_id + " and op.d_id = " + search_for_detail_id, "query");
            Cursor c1 = db.rawQuery("select op.op_id, p.p_rank, p.p_name, p.p_nric from personnel p, operation_personnel op where op.p_id = p.p_id and op.o_id = " + o_id + " and op.d_id = " + search_for_detail_id, null);

            ArrayList<HashMap<String, String>> PersonnelList = new ArrayList<HashMap<String, String>>();
            while (c1.moveToNext()) {
                HashMap<String, String> map = new HashMap<String, String>();
                String line_id = c1.getString(0);
                String line_rank = c1.getString(1);
                String line_name = c1.getString(2);
                String line_nric = c1.getString(3);

                map.put(TAG_OPID, line_id);
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
                    new String[]{TAG_OPID, TAG_PRANK, TAG_PNAME, TAG_P_NRIC}, //from array
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
                    intent.putExtra("op_id", ((TextView) view.findViewById(R.id.tv_Personnel_ID_Checklist)).getText().toString());
                    Log.i("op_id",  ((TextView) view.findViewById(R.id.tv_Personnel_ID_Checklist)).getText().toString());
                    startActivity(intent);
                }

            });
        }
    }

    public void showAssignPersonnelAmmo(String op_id, int o_id, ArrayList<String> op_ids, boolean all)
    {
        fragment_Assign_Personnel_Ammunition fragment = new fragment_Assign_Personnel_Ammunition();
        Bundle args = new Bundle();
        args.putString(TAG_OP_ID, op_id);
        args.putString(TAG_O_ID, String.valueOf(o_id));
        args.putStringArrayList("OP_ID_LIST", op_ids);
        args.putBoolean("WHOLEDETAIL",all);
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

    @Override
    public void onLongItemClick(int position) {

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
}


