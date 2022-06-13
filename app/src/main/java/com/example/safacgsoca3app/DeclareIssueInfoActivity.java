package com.example.safacgsoca3app;

import android.app.Dialog;
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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DeclareIssueInfoActivity extends AppCompatActivity {

    private static final String TAG_PAID = "pa_id";
    private static final String TAG_OPID = "op_id";
    private static final String TAG_AID = "a_id";
    private static final String TAG_TOISSUE_ISSUED = "pa_toissue_issued";
    private static final String TAG_TOISSUE_ISSUED_DESC = "pa_toissue_issued_desc";
    private static final String TAG_RETURNED = "pa_returned";
    private static final String TAG_EXPENDED = "pa_expended";
    private static final String TAG_SPOILED = "pa_spoiled";
    private static final String TAG_ANAME = "a_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_issue_return_receive_info);

        Intent intent = getIntent();
        String Function4 = intent.getStringExtra("Function3");

        TextView tv_Issue_Return_Receive;
        TextView tv_Detail_Name;
        TextView tv_Personnel_Name;
        TextView tv_Ammunition_Name;
        Button btn_ClearPad;
        Button btn_Validate;

        tv_Issue_Return_Receive = (TextView) findViewById(R.id.tv_Issue_Return_Receive);
        tv_Detail_Name = (TextView) findViewById(R.id.tv_Detail_Name);
        tv_Personnel_Name = (TextView) findViewById(R.id.tv_Personnel_Name);
        tv_Ammunition_Name = (TextView) findViewById(R.id.tv_Ammunition_Name);
        btn_ClearPad = (Button) findViewById(R.id.btn_ClearPad);
        btn_Validate = (Button) findViewById(R.id.btn_Validate);

        tv_Issue_Return_Receive.setText(Function4);

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS personnel_ammunition (pa_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, op_id integer NOT NULL, a_id integer NOT NULL, pa_issue_qty number NOT NULL, pa_issued number, pa_returned number, pa_expended number, pa_spoiled number)");

        Cursor c1 = db.rawQuery("SELECT a.a_name, pa.pa_id, pa.op_id, pa.a_id, pa.pa_issue_qty, pa.pa_issued, pa.pa_returned, pa.pa_expended, pa.pa_spoiled FROM personnel_ammunition pa, ammunition a WHERE a.a_id = pa.a_id and pa.op_id = 1", null);

        ArrayList<HashMap<String, String>> IssueAmmoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_aname = c1.getString(0);
            String line_paid = c1.getString(1);
            String line_opid = c1.getString(2);
            String line_aid = c1.getString(3);

            String line_issue_issued = null;
            String line_toissue_issued_desc = null;
            if (Function4.equals("Issuing: ")) {
                line_issue_issued = c1.getString(4);
                line_toissue_issued_desc = "To Issue";
            } else {
                line_issue_issued = c1.getString(5);
                line_toissue_issued_desc = "Issued";
            }

            String line_returned = c1.getString(6);
            String line_expended = c1.getString(7);
            String line_spoiled = c1.getString(8);

            map.put(TAG_ANAME, line_aname);
            map.put(TAG_PAID, line_paid);
            map.put(TAG_OPID, line_opid);
            map.put(TAG_AID, line_aid);
            map.put(TAG_TOISSUE_ISSUED, line_issue_issued);
            map.put(TAG_TOISSUE_ISSUED_DESC, line_toissue_issued_desc);
            map.put(TAG_RETURNED, line_returned);
            map.put(TAG_EXPENDED, line_expended);
            map.put(TAG_SPOILED, line_spoiled);

            IssueAmmoList.add(map);
        }

        db.execSQL("CREATE TABLE IF NOT EXISTS detail (d_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, d_name text NOT NULL, o_id integer NOT NULL)");
        c1 = db.rawQuery("select d.d_name, p.p_name from detail d, personnel p, operation_personnel op where d.d_id = op.d_id and p.p_id = op.p_id and op.op_id = 1 ", null);
        while (c1.moveToNext()) {
            tv_Detail_Name.setText(c1.getString(0));
            tv_Personnel_Name.setText(c1.getString(1));
        }
        db.close();

        ListView lv = findViewById(R.id.lv_Issue_Ammunition);
        ListAdapter adapter = new SimpleAdapter(
                DeclareIssueInfoActivity.this, //context
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
                showEditIssueDialog();
            }
        });

        /*
        SQLiteDatabase db2;
        db2 = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db2.execSQL("CREATE TABLE IF NOT EXISTS detail (d_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, d_name text NOT NULL, o_id integer NOT NULL)");

        Cursor c2 = db2.rawQuery("select * from Detail where d_id = 1", null);
        while (c2.moveToNext()) {
            tv_Detail_Name.setText(c2.getString(1));
        }
        db2.close();

        SQLiteDatabase db3;
        db3 = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db3.execSQL("CREATE TABLE IF NOT EXISTS personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, p_rank varchar(255) NOT NULL, p_name varchar(255) NOT NULL, p_nric text)");

        Cursor c3 = db3.rawQuery("select * from Personnel where p_id = 1", null);
        while (c3.moveToNext()) {
            tv_Personnel_Name.setText(c3.getString(2));
        }
        db3.close();\
        */
    }

    private void showEditIssueDialog()
    {
        Dialog EditIssueDialog = new Dialog(DeclareIssueInfoActivity.this, android.R.style.Theme_Black_NoTitleBar);
        EditIssueDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100,0,0,0)));
        EditIssueDialog.setContentView(R.layout.dialog_edit_issue_return_receive_ammunition);
        EditIssueDialog.setCancelable(true);
        EditIssueDialog.show();

        Button btn_EditQty = (Button) EditIssueDialog.findViewById(R.id.btn_Confirm);
        btn_EditQty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditIssueDialog.dismiss();
            }
        });

    }
}