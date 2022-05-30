package com.example.safacgsoca3app;

import android.app.Dialog;
import android.content.ContentValues;
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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DeclareIssueInfoActivity extends AppCompatActivity {

    private static final String TAG_ID = "a_id";
    private static final String TAG_NAME = "a_name";
    private static final String TAG_DESC = "a_description";
    private static final String TAG_QTY = "a_qty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_issue_info);

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("DROP TABLE Ammunition");
        db.execSQL("CREATE TABLE IF NOT EXISTS Ammunition (a_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, a_name text NOT NULL, a_description varchar(255) NOT NULL, a_qty float NOT NULL)");


        //use this to insert values
        ContentValues content = new ContentValues();
        content.put("a_id", "0");
        content.put("a_name", "5.56 BALL");
        content.put("a_description", "Pew pew");
        content.put("a_qty", "6969");

        db.insert("Ammunition", null, content);
        //end


        Cursor c1 = db.rawQuery("select * from Ammunition", null);

        ArrayList<HashMap<String, String>> IssueAmmoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);
            String line_desc = c1.getString(2);
            String line_qty = c1.getString(3);

            map.put(TAG_ID, line_id);
            map.put(TAG_NAME, line_name);
            map.put(TAG_DESC, line_desc);
            map.put(TAG_QTY, line_qty);

            IssueAmmoList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lv_Issue_Ammunition);
        ListAdapter adapter = new SimpleAdapter(
                DeclareIssueInfoActivity.this, //context
                IssueAmmoList, //hashmapdata
                R.layout.list_issue_ammunition, //layout of list
                new String[]{TAG_ID,TAG_NAME,TAG_QTY}, //from array
                new int[]{R.id.tv_Ammunition_Id,
                        R.id.tv_Ammunition_Name,
                        R.id.tv_Ammunition_Quantity}); //toarray
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
        EditIssueDialog.setContentView(R.layout.dialog_edit_issue_ammunition);
        EditIssueDialog.setCancelable(true);
        EditIssueDialog.show();

        Button btn_EditQty = (Button) EditIssueDialog.findViewById(R.id.btn_Edit_Qty);
        btn_EditQty.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditIssueDialog.dismiss();
            }
        });

    }
}