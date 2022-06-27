package com.example.safacgsoca3app;

import android.app.Dialog;
import android.app.Person;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.util.ArrayList;
import java.util.HashMap;



public class DeclareIssueReturnReceiveInfoActivity extends AppCompatActivity implements RecyclerViewInterface {

    private static final String TAG_DYNAMIC_ISSUE = "dynamic_issue";

    private static final String TAG_TD_ID = "td_id"
    private static final String TAG_DOC_NUMBER = "doc_id";
    private static final String TAG_TD_O_NAME = "td_o_name";
    private static final String TAG_TD_O_KAH = "td_o_kah";
    private static final String TAG_TD_O_UNIT = "td_o_unit";
    private static final String TAG_TD_D_NAME = "td_d_name";
    private static final String TAG_TD_AMMO_NAME = "td_a_name";
    private static final String TAG_TD_PERSONNEL_NAME = "td_p_name";
    private static final String TAG_TD_ISSUED = "td_issued";
    private static final String TAG_TD_RETURNED = "td_returned";
    private static final String TAG_TD_EXPENDED = "td_expended";
    private static final String TAG_TD_SPOILED = "td_spoiled";
    private static final String TAG_TD_ISSUEDATETIME = "td_issuedatetime";
    private static final String TAG_TD_ISSUESIGNATURE = "td_issuesignature";
    private static final String TAG_TD_RETURNDATETIME = "td_returndatetime";
    private static final String TAG_TD_RETURNSIGNATURE = "td_returnsignature";
    private static final String TAG_TD_EXPORTED = "td_exported";


    private static final String TAG_P_ID = "p_id";
    private static final String TAG_P_NAME = "p_name";
    private static final String TAG_P_NRIC = "p_nric";

    private static final String TAG_O_ID = "o_id";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_KAH = "o_kah";
    private static final String TAG_O_UNIT = "o_unit";

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

    private static final String TAG_NEW_ENTRY = "new_entry";

    ArrayList<HashMap<String,String>> rvData = new ArrayList<HashMap<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_issue_return_receive_info);

        Intent intent = getIntent();

        String type = intent.getStringExtra("type");
        String o_id = intent.getStringExtra("o_id");
        String d_id = intent.getStringExtra("d_id");
        ArrayList<HashMap<String, String>> PersonnelList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("PersonnelList");
        String current_op_id = PersonnelList.remove(0).get(TAG_OP_ID);


        TextView tv_Issue_Return_Receive;
        Button btn_ClearPad;
        Button btn_Validate;
        SignaturePad Signature_Pad;
        ImageView imageView;

        tv_Issue_Return_Receive = (TextView) findViewById(R.id.tv_Issue_Return_Receive);
        btn_ClearPad = (Button) findViewById(R.id.btn_ClearPad);
        btn_Validate = (Button) findViewById(R.id.btn_Validate);
        Signature_Pad = findViewById(R.id.Signature_Pad);
        imageView = findViewById(R.id.imageView);

        if(Integer.valueOf(type) == 1)//ammo unissued
        {
            tv_Issue_Return_Receive.setText("ISSUING");
        }
        else if(Integer.valueOf(type) == 2)
        {
            tv_Issue_Return_Receive.setText("RETURN");
        }
        else if(Integer.valueOf(type) == 3)
        {
            tv_Issue_Return_Receive.setText("RECIEVE");
        }

        //query for all pa_id entries with op_id
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        //Cursor c1 = db.rawQuery("select pa_id from personnel_ammunition where pa_id not in (select pa_id from transaction_data where td_exported = 1 and pa_id in (select pa_id from personnel_ammunition where op_id = " + current_op_id + ")) and op_id = " + current_op_id , null);


        //add into arraylist of new or opened
        Cursor c1 = db.rawQuery("select pa.pa_id, iif(td.td_exported is null, 1, td.td_exported) new_entry, iif(td.td_id is null, -1, td.td_exported) td_id  from personnel_ammunition pa left join transaction_data td on pa.pa_id = td.pa_id where pa.op_id = " + current_op_id, null);
        ArrayList<HashMap<String,String>> all_pa_id = new ArrayList<>();
        while(c1.moveToNext())
        {
            HashMap<String,String> line_map = new HashMap<String, String>();
            line_map.put(TAG_PA_ID, c1.getString(0));
            line_map.put(TAG_NEW_ENTRY, c1.getString(1));
            line_map.put(TAG_TD_ID, c1.getString(2));
        }

        //form rvData
        for(HashMap<String,String> entries : all_pa_id)
        {

            HashMap<String,String> map = new HashMap<String, String>();
            int current_pa_id = Integer.valueOf(entries.get(TAG_PA_ID));
            boolean current_is_new_entry = Boolean.parseBoolean(entries.get(TAG_NEW_ENTRY));
            String current_td_id = entries.get(TAG_TD_ID);

            //TAG_DYNAMIC_ISSUE = true if new entry, false if existing entry
            //TAG_PA_ID = taken from above query
            //TAG_A_NAME = raw text taken from transaction details if old entry, referenced from ammunition table if old entry
            //TAG_PA_ISSUE_QTY = text taken from personnel_ammunition if entry is new, else take issued value from transaction detail
            //TAG_TD_RETURNED = text taken only from transaction detail. automatically set this value to issued quantity in event of ops
            //TAG_TD_EXPENDED = text taken only from transaction detail. automatically set this value to issued quantity in event of range
            //TAG_TD_SPOILED = text taken only from transaction detail.

            if(current_is_new_entry == true)
            {
                map.put(TAG_DYNAMIC_ISSUE, "true");
                map.put(TAG_PA_ID, String.valueOf(current_pa_id));

                c1 = db.rawQuery("select a.a_name, pa.pa_issue_qty from personnel_ammunition pa, ammunition a where a.a_id = pa.a_id and pa.pa_id = "+ current_pa_id, null);
                if(c1.moveToFirst())
                {
                    map.put(TAG_A_NAME, c1.getString(0));
                    map.put(TAG_PA_ISSUE_QTY, c1.getString(1));
                }
            }
            else
            {
                map.put(TAG_DYNAMIC_ISSUE, "false");
                map.put(TAG_TD_ID, current_td_id);
                map.put(TAG_PA_ID, String.valueOf(current_pa_id));

                //query for the rest of the data in transaction_details
                //c1 = db.rawQuery("select ")


            }


        }

        btn_ClearPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signature_Pad.clear();
            }
        });



        btn_Validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = Signature_Pad.getSignatureBitmap();
                imageView.setImageBitmap(bitmap);

                int doc = -1;
                int pa_id = 1;
                //open database
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);


                String doc_o_name = null;
                String doc_o_unit = null;
                String doc_d_name = null;
                //get o_name, kah, unit date from o_id
                Cursor c1 = db.rawQuery("select o_name, o_unit from operation where o_id = " + o_id, null);
                if(c1.moveToFirst())
                {
                    String line_o_name = c1.getString(0);
                    String line_o_unit = c1.getString(1);
                }
                c1 = db.rawQuery("select d.d_name from detail d, personnel_ammunition pa, operation_personnel op where pa.op_id = op.op_id and d.d_id = op.d_id and pa.pa_id = " + pa_id, null);
                if(c1.moveToFirst())
                {
                    String line_d_name = c1.getString(0);
                }
                //check if document already exists (CHECK ALL STRING PARAMS IN CASE OF DETAIL CHANGE, THUS CREATE NEW DOC IF CHANGE OCCURS)
                Cursor c3 = db.rawQuery("select doc_id from document where o_name = " + doc_o_name + " and o_unit = " + doc_o_unit + " and d_name = " + doc_d_name + " and doc_closed = 0", null);
                int accessed_doc_id = -1;
                if(c1.moveToFirst())
                {
                    accessed_doc_id = c1.getInt(0);
                }
                //if document is not opened, create new doc according to data
                if(accessed_doc_id == -1)
                {
                    ContentValues content = new ContentValues();
                    content.put(TAG_D_NAME, doc_d_name);
                    content.put(TAG_O_NAME, doc_o_name);
                    content.put(TAG_O_UNIT, doc_o_unit);
                    db.insert("document" ,null,content);
                    c1 = db.rawQuery("select doc_id from document order by doc_id DESC LIMIT 1", null);
                    if(c1.moveToFirst())
                    {
                        accessed_doc_id = c1.getInt(0);
                    }
                }

                //check entry type
                //issuing
                if(Integer.valueOf(type) == 1)
                {
                    //this will change td_issued in transaction_data

                    ContentValues cv = new ContentValues();
                    //form the initial entry

                    //get td_ammo name from a_id
                    //c1 = db.rawQuery("select a_name from ammunition a, personnel_ammunition pa where pa.a_id = a.a_id and pa_id =", null);
                    //get td_personnel_name from pa_id ref p_id

                    //dump issue qty to new entry

                    //dump issue datetime as of realtime

                    //dump issue signature

                    //commit into db

                }
                //returning
                else if(Integer.valueOf(type) == 2)
                {
                    //this will change td_returned/expended/
                }
                //recieving
                else if(Integer.valueOf(type) == 3)
                {
                    //ignore first
                }

                //close database
                db.close();
            }
        });
    }
                /*
                // SAVE TOISSUE AS ISSUED
                String FromToIssueToIssued = null;
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                Cursor c1 = db.rawQuery("SELECT pa_id FROM personnel_ammunition WHERE op_id =" + op_id, null);
                while (c1.moveToNext()) {


                        pa_id = c1.getString(0);


                        c1 = db.rawQuery("SELECT pa_issue_qty FROM personnel_ammunition WHERE pa_id =" + pa_id, null);
                        while (c1.moveToNext()) {
                            FromToIssueToIssued = c1.getString(0);
                        }


                        db.execSQL("UPDATE personnel_ammunition SET pa_issued = " + FromToIssueToIssued + " WHERE pa_id = " + pa_id);


                        ContentValues content = new ContentValues();

                        c1 = db.rawQuery("SELECT a.a_name, pa.a_id FROM personnel_ammunition pa, ammunition a WHERE a.a_id = pa.a_id and pa.pa_id =" + pa_id, null);

                        while (c1.moveToNext()) {
                            HashMap<String, String> map = new HashMap<String, String>();
                            a_name = c1.getString(0);
                            a_id = c1.getString(1);
                        }

                        content.put("td_ammo_name", a_name);
                        content.put("pa_id", pa_id);
                        content.put("a_id", a_id);

                        //Cursor c1 = db.rawQuery("SELECT a.a_id, pa.pa_id, o.o_name, o.o_kah, o.o_unit, a.a_name, pa.pa_issued, pa.pa_returned, pa.pa_expended, pa.pa_spoiled FROM personnel p, operation_personnel op, operation o, ammunition a, personnel_ammunition pa WHERE p.p_id = op.p_id and o.o_id = a.o_id = op.o_id and a.a_id = pa.a_id and pa.op_id ="+ op_id, null);
                        c1 = db.rawQuery("SELECT pa_issued, pa_returned, pa_expended, pa_spoiled FROM personnel_ammunition WHERE pa_id =" + pa_id, null);
                        while (c1.moveToNext()) {
                            pa_issued = c1.getString(0);
                            pa_returned = c1.getString(1);
                            pa_expended = c1.getString(2);
                            pa_spoiled = c1.getString(3);
                        }

                        content.put("td_issued", pa_issued);
                        content.put("td_returned", pa_returned);
                        content.put("td_expended", pa_expended);
                        content.put("td_spoiled", pa_spoiled);

                        c1 = db.rawQuery("SELECT o_name, o_kah, o_unit FROM operation WHERE o_id = " + o_id, null);
                        while (c1.moveToNext()) {
                            o_name = c1.getString(0);
                            o_kah = c1.getString(1);
                            o_unit = c1.getString(2);
                        }

                        content.put("o_name", o_name);
                        content.put("o_kah", o_kah);
                        content.put("o_unit", o_unit);


                        content.put("d_name", d_name);
                        content.put("td_personnel_name", p_name);
                        content.put("td_issuedatetime", " ");
                        content.put("td_issuesignature", " ");
                        content.put("td_returndatetime", " ");
                        content.put("td_returnsignature", " ");
                        content.put("td_exported", "0");

                        Log.i("Content", content.toString());
                        db.insert("Transaction_Data", null, content);
                }
                db.close();

                // SAVE BITMAP

                // GENERATE DOCUMENTS WITHOUT AMMO IC SIGNATURE

                // AMMO IC CHECKS AND SIGNS

                // EXPORT

                if (SelectedPersonnel<PersonnelList.size())
                {
                    SelectedPersonnel++;
                    Log.i("SelectedPersonnel", String.valueOf(SelectedPersonnel));
                    onResume();
                }
                else if (SelectedPersonnel>PersonnelList.size())
                {
                    SelectedAmmunition = 0;
                    Log.i("SelectedAmmunition Reset", String.valueOf(SelectedPersonnel));
                }

            }*/


    protected void onResume() {
        super.onResume();

        /*
        op_id = PersonnelList.get(SelectedPersonnel).get("op_id");
        p_rank = PersonnelList.get(SelectedPersonnel).get("p_rank");
        p_name = PersonnelList.get(SelectedPersonnel).get("p_name");
        p_nric = PersonnelList.get(SelectedPersonnel).get("p_nric");

        TextView tv_Detail_Name;
        TextView tv_Personnel_Name;

        tv_Detail_Name = (TextView) findViewById(R.id.tv_Detail_Name);
        tv_Personnel_Name = (TextView) findViewById(R.id.tv_Personnel_Name);

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS personnel_ammunition (pa_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, op_id integer NOT NULL, a_id integer NOT NULL, pa_issue_qty number NOT NULL, pa_issued number, pa_returned number, pa_expended number, pa_spoiled number)");

        Cursor c1 = db.rawQuery("SELECT a.a_name, pa.pa_id, pa.a_id, pa.pa_issue_qty, pa.pa_issued, pa.pa_returned, pa.pa_expended, pa.pa_spoiled FROM personnel_ammunition pa, ammunition a WHERE a.a_id = pa.a_id and pa.op_id =" + op_id, null);

        ArrayList<HashMap<String, String>> IssueAmmoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_aname = c1.getString(0);
            String line_paid = c1.getString(1);
            String line_aid = c1.getString(2);

            String line_issue_issued = null;
            String line_toissue_issued_desc = null;
            if (Function4.equals("Issuing: ")) {
                line_issue_issued = c1.getString(3);
                line_toissue_issued_desc = "To Issue";
            } else {
                line_issue_issued = c1.getString(4);
                line_toissue_issued_desc = "Issued";
            }

            String line_returned = c1.getString(5);
            String line_expended = c1.getString(6);
            String line_spoiled = c1.getString(7);

            map.put(TAG_ANAME, line_aname);
            map.put(TAG_PAID, line_paid);
            map.put(TAG_AID, line_aid);
            map.put(TAG_TOISSUE_ISSUED, line_issue_issued);
            map.put(TAG_TOISSUE_ISSUED_DESC, line_toissue_issued_desc);
            map.put(TAG_RETURNED, line_returned);
            map.put(TAG_EXPENDED, line_expended);
            map.put(TAG_SPOILED, line_spoiled);
            map.put(TAG_OPID, PersonnelList.get(SelectedPersonnel).get("op_id"));
            map.put(Selected, PersonnelList.get(SelectedPersonnel).get("SELECTED"));
            map.put(p_rank, PersonnelList.get(SelectedPersonnel).get("p_rank"));
            map.put(p_name, PersonnelList.get(SelectedPersonnel).get("p_name"));
            map.put(p_nric, PersonnelList.get(SelectedPersonnel).get("p_nric"));

            IssueAmmoList.add(map);
        }
        length = IssueAmmoList.size();

        db.execSQL("CREATE TABLE IF NOT EXISTS detail (d_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, d_name text NOT NULL, o_id integer NOT NULL)");
        c1 = db.rawQuery("select d.d_name, p.p_name from detail d, personnel p, operation_personnel op where d.d_id = op.d_id and p.p_id = op.p_id and op.op_id = "+op_id, null);
        while (c1.moveToNext()) {
            tv_Detail_Name.setText(c1.getString(0));
            tv_Personnel_Name.setText(c1.getString(1));
        }
        db.close();

        ListView lv = findViewById(R.id.lv_Issue_Ammunition);
        ListAdapter adapter = new SimpleAdapter(
                DeclareIssueReturnReceiveInfoActivity.this, //context
                IssueAmmoList, //hashmapdata
                R.layout.list_issue_return_receive_ammunition, //layout of list
                new String[]{TAG_ANAME, TAG_PAID, TAG_OPID, TAG_AID, TAG_TOISSUE_ISSUED_DESC, TAG_TOISSUE_ISSUED, TAG_RETURNED, TAG_EXPENDED, TAG_SPOILED}, //from array
                new int[]{R.id.tv_Ammunition_Name,
                        R.id.tv_PAID,
                        R.id.tv_OPID,
                        R.id.tv_AID,
                        R.id.tv_ToIssueOrIssued_Text,
                        R.id.tv_ToIssueOrIssued_Qty,
                        R.id.tv_Returned_Qty,
                        R.id.tv_Expended_Qty,
                        R.id.tv_Spoilt_Qty,
                }); //toarray
        // updating listview
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showEditIssueDialog(IssueAmmoList, i);
            }
        });*/
    }

    @Override
    public void onItemClick(int position) {

    }

    /*private void showEditIssueDialog(ArrayList<HashMap<String, String>> IssueAmmoList, int i) {
        Dialog EditIssueDialog = new Dialog(DeclareIssueReturnReceiveInfoActivity.this, android.R.style.Theme_Black_NoTitleBar);
        EditIssueDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        EditIssueDialog.setContentView(R.layout.dialog_edit_issue_return_receive_ammunition);
        EditIssueDialog.setCancelable(true);
        EditIssueDialog.show();

        Button btn_EditQty;

        EditText et_ToIssueOrIssued_Qty;
        EditText et_Expended_Qty;
        EditText et_Returned_Qty;
        EditText et_Spoilt_Qty;
        TextView tv_Ammunition_Name;
        TextView tv_ToIssueOrIssued_Text;

        et_ToIssueOrIssued_Qty = (EditText) EditIssueDialog.findViewById(R.id.et_ToIssueOrIssued_Qty);
        et_Expended_Qty = (EditText) EditIssueDialog.findViewById(R.id.et_Expended_Qty);
        et_Returned_Qty = (EditText) EditIssueDialog.findViewById(R.id.et_Returned_Qty);
        et_Spoilt_Qty = (EditText) EditIssueDialog.findViewById(R.id.et_Spoilt_Qty);
        tv_Ammunition_Name = (TextView) EditIssueDialog.findViewById(R.id.tv_Ammunition_Name);
        tv_ToIssueOrIssued_Text = (TextView) EditIssueDialog.findViewById(R.id.tv_ToIssueOrIssued_Text);

        et_ToIssueOrIssued_Qty.setText(IssueAmmoList.get(i).get("pa_toissue_issued"));
        et_Expended_Qty.setText(IssueAmmoList.get(i).get("pa_expended"));
        et_Returned_Qty.setText(IssueAmmoList.get(i).get("pa_returned"));
        et_Spoilt_Qty.setText(IssueAmmoList.get(i).get("pa_spoiled"));
        tv_Ammunition_Name.setText(IssueAmmoList.get(i).get("a_name"));
        tv_ToIssueOrIssued_Text.setText(IssueAmmoList.get(i).get("pa_toissue_issued_desc"));

        if (IssueAmmoList.get(i).get("pa_toissue_issued_desc").equals("To Issue")) {
            et_ToIssueOrIssued_Qty.setEnabled(true);
            et_Expended_Qty.setEnabled(false);
            et_Returned_Qty.setEnabled(false);
            et_Spoilt_Qty.setEnabled(false);
            Log.i(TAG_AID, "TO ISSUE");
        } else {
            et_ToIssueOrIssued_Qty.setEnabled(false);
            et_Expended_Qty.setEnabled(true);
            et_Returned_Qty.setEnabled(true);
            et_Spoilt_Qty.setEnabled(true);
            Log.i(TAG_AID, "ISSUED");
        }

        btn_EditQty = (Button) EditIssueDialog.findViewById(R.id.btn_Confirm);
        btn_EditQty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS personnel_ammunition (pa_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, op_id integer NOT NULL, a_id integer NOT NULL, pa_issue_qty number NOT NULL, pa_issued number, pa_returned number, pa_expended number, pa_spoiled number)");

                ContentValues content = new ContentValues();
                content.put(TAG_EXPENDED, String.valueOf(et_Expended_Qty.getText()));
                content.put(TAG_RETURNED, String.valueOf(et_Returned_Qty.getText()));
                content.put(TAG_SPOILED, String.valueOf(et_Spoilt_Qty.getText()));

                if (IssueAmmoList.get(i).get("pa_toissue_issued_desc").equals("To Issue"))
                {
                    content.put(TAG_ISSUE_QTY, String.valueOf(et_ToIssueOrIssued_Qty.getText()));
                }
                else
                {
                    content.put(TAG_ISSUED, String.valueOf(et_ToIssueOrIssued_Qty.getText()));
                }

                db.update("personnel_ammunition", content, "pa_id = ?", new String[]{IssueAmmoList.get(i).get("pa_id")});



                db.close();
                onResume();
                EditIssueDialog.dismiss();
            }
        });
    }*/
}