package com.example.safacgsoca3app;

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
    private static final String TAG_TOISSUE = "pa_issue_qty";
    private static final String TAG_ISSUED = "pa_issued";
    private static final String TAG_RETURNED = "pa_returned";
    private static final String TAG_EXPENDED = "pa_expended";
    private static final String TAG_SPOILED = "pa_spoiled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_issue_return_receive_info);

        Intent intent = getIntent();
        String Function4 = intent.getStringExtra("Function3");
        String ToIssueOrIssuedText;
        String ToIssueOrIssuedQty;

        TextView tv_Issue_Return_Receive;
        TextView tv_Detail_Name;
        TextView tv_Personnel_Name;
        TextView tv_Issued;
        Button btn_ClearPad;
        Button btn_Validate;

        tv_Issue_Return_Receive = (TextView) findViewById(R.id.tv_Issue_Return_Receive);
        tv_Detail_Name = (TextView) findViewById(R.id.tv_Detail_Name);
        tv_Personnel_Name = (TextView) findViewById(R.id.tv_Personnel_Name);
        btn_ClearPad = (Button) findViewById(R.id.btn_ClearPad);
        btn_Validate = (Button) findViewById(R.id.btn_Validate);
        tv_Issued = (TextView) findViewById(R.id.tv_ToIssueOrIssued_Text);

        tv_Issue_Return_Receive.setText(Function4);
        tv_Detail_Name.setText("Detail Name");
        tv_Personnel_Name.setText("Personnel Name");

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS personnel_ammunition (pa_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, op_id integer NOT NULL, a_id integer NOT NULL, pa_issue_qty number NOT NULL, pa_issued number, pa_returned number, pa_expended number, pa_spoiled number)");

        Cursor c1 = db.rawQuery("select * from personnel_ammunition where op_id = 1", null);

        ArrayList<HashMap<String, String>> IssueAmmoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_paid = c1.getString(0);
            String line_opid = c1.getString(1);
            String line_aid = c1.getString(2);
            String line_toissue = c1.getString(3);
            String line_issued = c1.getString(4);
            String line_returned = c1.getString(5);
            String line_expended = c1.getString(6);
            String line_spoiled = c1.getString(7);

            map.put(TAG_PAID, line_paid);
            map.put(TAG_OPID, line_opid);
            map.put(TAG_AID, line_aid);
            map.put(TAG_TOISSUE, line_toissue);
            map.put(TAG_ISSUED, line_issued);
            map.put(TAG_RETURNED, line_returned);
            map.put(TAG_EXPENDED, line_expended);
            map.put(TAG_SPOILED, line_spoiled);

            IssueAmmoList.add(map);
        }
        db.close();

        if (Function4.equals("Issuing: "))
        {
            ToIssueOrIssuedText = "To Issue";
            ToIssueOrIssuedQty = TAG_TOISSUE;
        }
        else
        {
            ToIssueOrIssuedText = "Issued";
            ToIssueOrIssuedQty = TAG_ISSUED;
        }

        ListView lv = findViewById(R.id.lv_Issue_Ammunition);
        ListAdapter adapter = new SimpleAdapter(
                DeclareIssueInfoActivity.this, //context
                IssueAmmoList, //hashmapdata
                R.layout.list_issue_return_receive_ammunition, //layout of list
                new String[]{TAG_PAID, TAG_OPID, TAG_AID, ToIssueOrIssuedText, ToIssueOrIssuedQty, TAG_RETURNED, TAG_EXPENDED, TAG_SPOILED}, //from array
                new int[]{//R.id.tv_Ammunition_Name,
                        R.id.tv_PAID,
                        R.id.tv_OPID,
                        R.id.tv_AID,
                        R.id.tv_ToIssueOrIssued_Text,
                        R.id.tv_ToIssueOrIssued_Qty,
                        R.id.tv_Returned_Qty,
                        R.id.tv_Expended_Qty,
                        R.id.tv_Spoilt_Qty
                }); //toarray
        // updating listview
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showEditIssueDialog();
            }
        });
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