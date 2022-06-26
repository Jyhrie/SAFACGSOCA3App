package com.example.safacgsoca3app;

import android.app.Dialog;
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



public class DeclareIssueReturnReceiveInfoActivity extends AppCompatActivity {

    private static final String TAG_PAID = "pa_id";
    private static final String TAG_OPID = "op_id";
    private static final String TAG_AID = "a_id";
    private static final String TAG_TOISSUE_ISSUED = "pa_toissue_issued";
    private static final String TAG_TOISSUE_ISSUED_DESC = "pa_toissue_issued_desc";
    private static final String TAG_ISSUE_QTY = "pa_issue_qty";
    private static final String TAG_ISSUED = "pa_issued";
    private static final String TAG_RETURNED = "pa_returned";
    private static final String TAG_EXPENDED = "pa_expended";
    private static final String TAG_SPOILED = "pa_spoiled";
    private static final String TAG_ANAME = "a_name";

    String Selected;
    String o_id;
    String d_id;
    String d_name;
    String p_rank;
    String p_name;
    String p_nric;
    String op_id;
    String a_id;
    String pa_id;
    String o_name;
    String o_kah;
    String o_unit;
    String a_name;
    String pa_issued;
    String pa_returned;
    String pa_expended;
    String pa_spoiled;
    int length;
    ArrayList<HashMap<String, String>> PersonnelList;
    String Function4;
    int SelectedPersonnel;
    int SelectedAmmunition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_issue_return_receive_info);

        Intent intent = getIntent();
        Function4 = intent.getStringExtra("Function3");
        o_id = intent.getStringExtra("o_id");
        d_id = intent.getStringExtra("d_id");
        d_name = intent.getStringExtra("d_name");
        PersonnelList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("PersonnelList");
        SelectedPersonnel = 0;
        SelectedAmmunition = 0;

        op_id = PersonnelList.get(SelectedPersonnel).get("op_id");
        p_rank = PersonnelList.get(SelectedPersonnel).get("p_rank");
        p_name = PersonnelList.get(SelectedPersonnel).get("p_name");
        p_nric = PersonnelList.get(SelectedPersonnel).get("p_nric");

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

        tv_Issue_Return_Receive.setText(Function4);

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

                // SAVE TOISSUE AS ISSUED
                String FromToIssueToIssued = null;
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

                Cursor c1 = db.rawQuery("SELECT pa_id FROM personnel_ammunition WHERE op_id =" + op_id, null);
                while (c1.moveToNext()) {

                    for (int i = 0; !c1.isAfterLast(); i++) {

                        pa_id = c1.getString(i);
                        Log.i("Selected op_id", op_id);
                        Log.i("Selected pa_id", pa_id);


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
                }
                else if (SelectedPersonnel>PersonnelList.size())
                {
                    SelectedAmmunition = 0;
                    Log.i("SelectedAmmunition Reset", String.valueOf(SelectedPersonnel));
                }

            }
        });
    }

    protected void onResume() {
        super.onResume();

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
        });
    }

    private void showEditIssueDialog(ArrayList<HashMap<String, String>> IssueAmmoList, int i) {
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
    }
}