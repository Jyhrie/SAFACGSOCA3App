package com.example.safacgsoca3app;

import static com.example.safacgsoca3app.ViewLoadoutActivity.TAG_AID;
import static java.lang.Integer.parseInt;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewOperationActivity extends AppCompatActivity {

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

    private static final String TAG_ID = "o_id";
    private static final String TAG_PID ="p_id";
    private static final String TAG_NAME = "e_name";
    private static final String TAG_PNAME = "p_name";
    private static final String TAG_PRANK = "p_rank";
    private static final String TAG_KAH = "o_kah";
    private static final String TAG_ANAME = "a_name";
    private static final String TAG_OPID = "op_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_operation);

        //get id
        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_ID);
        Log.i("o_id = " + o_id, "view_operation_activity");

        TextView tvOperationName;
        TextView tvDateLocation;
        TextView tvKAH;
        Button btnViewOperationNominalRoll;
        Button btnViewAmmunition;
        Button btnAddIssueDetail;

        tvOperationName = (TextView) findViewById(R.id.tv_operation_name);
        tvDateLocation = (TextView) findViewById(R.id.tv_date_location);
        tvKAH = (TextView) findViewById(R.id.tv_kah);
        btnViewOperationNominalRoll = (Button) findViewById(R.id.btn_view_operation_nominal);
        btnViewAmmunition = (Button) findViewById(R.id.btn_view_ammunition);
        btnAddIssueDetail = (Button) findViewById(R.id.btn_add_issue_detail);

        showlvIssueDetail();

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
            tvDateLocation.setText("test date");
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
                String Function = "Issuing: ";
                intent.putExtra("Function", Function);
                ViewOperationActivity.this.startActivity(intent);
            }
        });

        Button btnReturn= (Button) findViewById(R.id.btn_Return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ViewOperationActivity.this, PersonnelChecklistActivity.class);
                String Function = "Returning: ";
                intent.putExtra("Function", Function);
                ViewOperationActivity.this.startActivity(intent);
            }
        });

        Button btnReceive= (Button) findViewById(R.id.btn_Receive);
        btnReceive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showReceiveDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_ID);

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

        //ammo dropdownlist adapter
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
                showlvIssueDetail();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //ignore
            }
        });

        //refresh


    }

    private void showAddEditDetailDialog(int context, String o_id, boolean reset)
    {
        EditText etDetailName;
        Button btnSaveDetail;
        Button btnSelectPersonnel;

        Dialog DialogFragment = new Dialog(ViewOperationActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_add_edit_detail);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        etDetailName = (EditText) DialogFragment.findViewById(R.id.etDetailName);
        btnSaveDetail = (Button) DialogFragment.findViewById(R.id.btnCreateDetail);
        btnSelectPersonnel = (Button) DialogFragment.findViewById(R.id.btnSelectPersonnel);

        String DetailName = String.valueOf(etDetailName);

        ListView lv_add_issuing_detail_personnel;
        lv_add_issuing_detail_personnel = (ListView) DialogFragment.findViewById(R.id.lv_edit_issuing_detail_personnel);


        //grab operation_personnel from db

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

        //wipe prev selected stuff
        if(reset == true) {
            db.execSQL("UPDATE operation_personnel SET d_id = 0 WHERE d_id = -1");
        }

        Cursor c1 = db.rawQuery("select * from operation_personnel where d_id = -1", null);
        ArrayList<HashMap<String, String>> op_list = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);

            map.put(TAG_OP_ID, line_id);
            map.put(TAG_P_NAME, line_name);

            op_list.add(map);
        }
        db.close();

        ListView lv = DialogFragment.findViewById(R.id.lv_edit_issuing_detail_personnel);
        ListAdapter adapter = new SimpleAdapter(
                ViewOperationActivity.this, //context
                op_list, //hashmapdata
                R.layout.list_view_detail_personnel, //layout of list
                new String[] {TAG_OP_ID, TAG_P_NAME}, //from array
                new int[] {R.id.tv_edit_detail_personnel_id, R.id.tv_edit_detail_personnel_name}); //toarray
        // updating listview
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAssignPersonnelAmmo(parseInt(((TextView) view.findViewById(R.id.tv_edit_detail_personnel_id)).getText().toString()), parseInt(o_id));
            }
        });

        btnSaveDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //update db to add detail name, id
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

                ContentValues content = new ContentValues();

                content.put(TAG_D_NAME, String.valueOf(etDetailName.getText()));
                content.put(TAG_O_ID, String.valueOf(o_id));

                db.insert("detail", null, content);
                db.close();

                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                int sel_detail_id = -1;
                Cursor cursor = db.rawQuery("SELECT d_id FROM detail ORDER BY d_id DESC LIMIT 1 ", null);
                while(cursor.moveToNext())
                {
                    sel_detail_id = cursor.getInt(0);
                }


                for (int i = 0; i < lv_add_issuing_detail_personnel.getCount(); i++) {
                    View v = lv_add_issuing_detail_personnel.getChildAt(i);
                    //update each instance of personnel to add to detail
                    String line_op_id = ((TextView) v.findViewById(R.id.tv_edit_detail_personnel_id)).getText().toString();

                    ContentValues cv = new ContentValues();
                    cv.put(TAG_D_ID, sel_detail_id);

                    db.update("operation_personnel", cv, "op_id = ?", new String[]{line_op_id});
                }

                db.close();

            }
        });

        btnSelectPersonnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectPersonnelDialog(o_id, DialogFragment, context);
            }
        });


    }

    private void showSelectPersonnelDialog(String o_id, Dialog prevDialog, int context)
    {
        Dialog DialogFragment = new Dialog(ViewOperationActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_assign_detail_personnel);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

        //wipe all previously selected personnel
        //db.execSQL("UPDATE operation_personnel SET d_id = 0 WHERE d_id = -1");

        //Cursor c1 = db.rawQuery("select op.op_id, p.p_rank, p.p_name from operation_personnel op, personnel p where op.d_id != -1 and p.p_id = op.p_id and op.o_id = "+ o_id , null);
        Cursor c1 = db.rawQuery("select op.op_id, p.p_rank, p.p_name from operation_personnel op, personnel p where op.d_id is null and p.p_id = op.p_id and op.o_id = "+ o_id , null);
        ArrayList<HashMap<String, String>> personnelList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            Log.i("data found", "test");
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);

            map.put(TAG_OP_ID, line_id);
            map.put(TAG_P_NAME, line_rank + " " + line_name);

            personnelList.add(map);
        }
        db.close();

        ListView lv = DialogFragment.findViewById(R.id.lv_assign_personnel_to_detail);
        ListAdapter adapter = new SimpleAdapter(
                ViewOperationActivity.this, //context
                personnelList, //hashmapdata
                R.layout.list_assign_detail_personnel, //layout of list
                new String[] { TAG_OP_ID, TAG_P_NAME}, //from array
                new int[] {R.id.tv_assign_detail_personnel_id, R.id.tv_assign_detail_personnel_name}); //toarray
        // updating listview
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView tvListPersonnelId = (TextView) view.findViewById(R.id.tv_assign_detail_personnel_id);
                    //toggle and add id to array;
                    CheckBox cb = view.findViewById(R.id.cb_assign_detail_personnel_bool);
                    cb.setChecked(!cb.isChecked());

                    LinearLayout layout = view.findViewById(R.id.layout_list_assign_detail_personnel);
                    if(cb.isChecked()) {
                        layout.setBackgroundResource(R.color.teal_200);
                    }
                    else
                    {
                        layout.setBackgroundResource(R.color.white);
                    }

                    }
        });

        Button btn_assign_detail_personnel_submit;
        btn_assign_detail_personnel_submit = DialogFragment.findViewById(R.id.btn_assign_detail_personnel_submit);
        btn_assign_detail_personnel_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

                for(int i = 0; i<lv.getCount(); i++)
                {
                    View v = lv.getChildAt(i);
                    if(((CheckBox) v.findViewById(R.id.cb_assign_detail_personnel_bool)).isChecked())
                    {
                        String op_id = ((TextView) v.findViewById(R.id.tv_assign_detail_personnel_id)).getText().toString();

                        ContentValues content = new ContentValues();
                        content.put(TAG_D_ID, -1);

                        //update detail
                        //assign d_id as -1 as temp value
                        db.update("operation_personnel", content, TAG_OP_ID + " = ?", new String[]{op_id});
                    }
                }
                db.close();

                prevDialog.dismiss();
                DialogFragment.dismiss();
                showAddEditDetailDialog(context, o_id, false);
            }
        });

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
                Intent i = new Intent(ViewOperationActivity.this, PersonnelChecklistActivity.class);
                ViewOperationActivity.this.startActivity(i);
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

    private void showlvIssueDetail()
    {
        String search_for_detail_id = ((TextView) findViewById(R.id.tv_selected_detail_id)).getText().toString();
        if(!search_for_detail_id.isEmpty()) {
            SQLiteDatabase db;
            db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
            Log.i("select p.p_id, p.p_rank, p.p_name, p.p_nric from personnel p, operation_personnel op where op.p_id = p.p_id and op.d_id = " + search_for_detail_id, "test");
            Cursor c1 = db.rawQuery("select p.p_id, p.p_rank, p.p_name, p.p_nric from personnel p, operation_personnel op where op.p_id = p.p_id and op.d_id = " + search_for_detail_id, null);

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

    private void showAssignPersonnelAmmo(int op_id, int o_id)
    {
        Dialog DialogFragment = new Dialog(ViewOperationActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_assign_personnel_ammunition);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        ListView lv_assign_personnel_ammunition;
        lv_assign_personnel_ammunition = (ListView) DialogFragment.findViewById(R.id.lv_assign_personnel_ammunition);

        //get all operation ammunition data
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("select a_id, a_name, a_qty from ammunition where o_id = " + o_id, null);
        ArrayList<HashMap<String, String>> ammo_list = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);

            map.put(TAG_OPID, line_id);
            map.put(TAG_PNAME, line_name);

            ammo_list.add(map);
        }
        db.close();

        //ammo dropdownlist adapter
        SimpleAdapter ddlAdapter = new SimpleAdapter(ViewOperationActivity.this,
                ammo_list,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{TAG_PNAME},
                new int[]{android.R.id.text1});

        ArrayList<HashMap<String, String>> personnel_ammo_list = new ArrayList<HashMap<String, String>>();

        //do query to get hashmap of all ammo from personnel
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c2 = db.rawQuery("select a_id, pa_issue_qty from personnel_ammunition where op_id = " +op_id, null);
        while(c2.moveToNext())
        {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_a_id = c2.getString(0);
            String line_pa_issue_qty = c2.getString(1);

            map.put(TAG_A_ID, line_a_id);
            map.put(TAG_PA_ISSUE_QTY, line_pa_issue_qty);

            personnel_ammo_list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(ViewOperationActivity.this,
                personnel_ammo_list,
                R.layout.list_assign_personnel_ammunition,
                new String[]{},
                new int[]{}
                );

        lv_assign_personnel_ammunition.setAdapter(adapter);

        Button btn_assign_personnel_ammunition_add_entry;
        btn_assign_personnel_ammunition_add_entry = (Button) DialogFragment.findViewById(R.id.btn_assign_personnel_ammunition_add_entry);
        btn_assign_personnel_ammunition_add_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //add all existing into new hashmap array
                ArrayList<HashMap<String,String>> existing_entries = new ArrayList<HashMap<String,String>>();
                for(int i =0; i < lv_assign_personnel_ammunition.getCount(); i++)
                {

                    HashMap<String,String> map = new HashMap<String,String>();
                    View v = lv_assign_personnel_ammunition.getChildAt(i);
                    String entry_a_id = ((TextView) v.findViewById(R.id.tv_selected_ammo_id)).getText().toString();
                    String entry_pa_issue_qty = ((TextView) v.findViewById(R.id.et_assign_personnel_ammunition_qty)).getText().toString();

                    existing_entries.add(map);
                }

            }
        });

        //foreach item in list

        /*for (int i = 0; i < lv_assign_personnel_ammunition.getCount(); i++) {
            View v = lv_assign_personnel_ammunition.getChildAt(i);
            if (((TextView) v.findViewById(R.id.tv_issuing_detail_ammo_id)).getText().toString().isEmpty());
            {
                //get & assign spinner data
                Spinner ddl_assign_personnel_ammunition;
                ddl_assign_personnel_ammunition = (Spinner) v.findViewById(R.id.ddl_assign_personnel_ammunition);

                //active update id of operation personnel
                ddl_assign_personnel_ammunition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) v.findViewById(R.id.tv_selected_ammo_id)).setText(ammo_list.get(i).get(TAG_AID));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //ignore

                    }
                });

            }

        }*/

        Button btn_save_assigned_ammunition;
        btn_save_assigned_ammunition = DialogFragment.findViewById(R.id.btn_save_assigned_ammunition);

        TextView tv_selected_ammo_id;
        tv_selected_ammo_id = DialogFragment.findViewById(R.id.tv_selected_ammo_id);

        TextView tv_existing_pa_id;
        tv_existing_pa_id = DialogFragment.findViewById(R.id.tv_existing_pa_id);

        btn_save_assigned_ammunition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0; i < lv_assign_personnel_ammunition.getCount(); i++)
                {

                    EditText et_assign_personnel_ammunition_qty;
                    et_assign_personnel_ammunition_qty = view.findViewById(R.id.et_assign_personnel_ammunition_qty);
                    View v = lv_assign_personnel_ammunition.getChildAt(i);
                    if((((TextView) v.findViewById(R.id.tv_existing_pa_id)).getText().toString().isEmpty()))
                    {
                        ContentValues content = new ContentValues();
                        content.put(TAG_OP_ID, String.valueOf(op_id));
                        content.put(TAG_A_ID, tv_selected_ammo_id.getText().toString());
                        content.put(TAG_PA_ISSUE_QTY, et_assign_personnel_ammunition_qty.getText().toString());

                        SQLiteDatabase db;
                        db = openOrCreateDatabase("A3App", MODE_PRIVATE, null);
                        db.insert("personnel_ammunition", null, content);
                        db.close();

                    }
                    else
                    {
                        ContentValues content = new ContentValues();
                        content.put(TAG_PA_ID, tv_existing_pa_id.getText().toString());
                        content.put(TAG_OP_ID, String.valueOf(op_id));
                        content.put(TAG_A_ID, tv_selected_ammo_id.getText().toString());
                        content.put(TAG_PA_ISSUE_QTY, et_assign_personnel_ammunition_qty.getText().toString());

                        SQLiteDatabase db;
                        db = openOrCreateDatabase("A3App", MODE_PRIVATE, null);
                        db.update("personnel_ammunition", content, TAG_PA_ID, new String[]{content.get(TAG_PA_ID).toString()});
                        db.close();
                    }
                }
            }
        });
    }


}


