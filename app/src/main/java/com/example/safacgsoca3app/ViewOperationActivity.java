package com.example.safacgsoca3app;

import static com.example.safacgsoca3app.ViewLoadoutActivity.TAG_AID;
import static java.lang.Float.parseFloat;
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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewOperationActivity extends AppCompatActivity {

    private static final String TAG_ID = "o_id";
    private static final String TAG_PID ="p_id";
    private static final String TAG_NAME = "e_name";
    private static final String TAG_PNAME = "p_name";
    private static final String TAG_PRANK = "p_rank";
    private static final String TAG_KAH = "o_kah";
    private static final String TAG_ANAME = "a_name";
    private static final String TAG_PREMARKS = "p_remarks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_operation);

        //get id
        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_ID);
        Log.i("test" + o_id, "test");

        TextView tvOperationName;
        TextView tvDateLocation;
        TextView tvKAH;
        Button btnViewNominalRoll;
        Button btnViewAmmunition;

        tvOperationName = (TextView) findViewById(R.id.tvOperationName);
        tvDateLocation = (TextView) findViewById(R.id.tvDateLocation);
        tvKAH = (TextView) findViewById(R.id.tvKAH);
        btnViewNominalRoll = (Button) findViewById(R.id.btnViewNominalRoll);
        btnViewAmmunition = (Button) findViewById(R.id.btnViewAmmunition);

        showlvIssueDetail();

        //pull data from db
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS operation (o_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, o_name varchar(255) NOT NULL, o_unit varchar(255) NOT NULL, o_date text NOT NULL, o_location text NOT NULL, o_kah text NOT NULL)");
        Log.i("querying for", o_id);
        Cursor c1 = db.rawQuery("SELECT * FROM operation WHERE o_id = " + o_id, null);

        //dump data into thing
        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
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

        btnViewNominalRoll.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), NominalRollActivity.class);
                startActivity(i);
            }
        });

        btnViewAmmunition.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), AmmunitionActivity.class);
                startActivity(i);
            }
        });
        
        Button btnIssue= (Button) findViewById(R.id.btn_Issue);
        btnIssue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(ViewOperationActivity.this, PersonnelChecklistActivity.class);
                ViewOperationActivity.this.startActivity(i);
            }
        });

        Button btnReturn= (Button) findViewById(R.id.btn_Return);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(ViewOperationActivity.this, PersonnelChecklistActivity.class);
                ViewOperationActivity.this.startActivity(i);
            }
        });

        Button btnReceive= (Button) findViewById(R.id.btn_Receive);
        btnReceive.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showReceiveDialog();
            }
        });
    }

    private void showAddDetailDialog(View view) {
        EditText etDetailName;
        EditText etDetailAmmmoQuantity;
        Spinner dropdownDetailAmmunition;
        Button btnCreateDetail;
        Button btnSelectPersonnel;

        Dialog DialogFragment = new Dialog(ViewOperationActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_add_issuing_detail);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        etDetailName = (EditText) DialogFragment.findViewById(R.id.etDetailName);

        btnCreateDetail = (Button) DialogFragment.findViewById(R.id.btnCreateDetail);
        btnSelectPersonnel = (Button) DialogFragment.findViewById(R.id.btnSelectPersonnel);

        String DetailName = String.valueOf(etDetailName);

        ListView lv_add_issuing_detail_ammunition;
        lv_add_issuing_detail_ammunition = (ListView) DialogFragment.findViewById(R.id.lv_view_loadout_ammunition_list);

        //add new entry to listview after initial listview added


        //populate ddl with items

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Ammunition (a_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, a_description varchar(255) NOT NULL, a_qty float NOT NULL)");
        Cursor c1 = db.rawQuery("select * from Ammunition", null);
        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);

            map.put(TAG_AID, line_id);
            map.put(TAG_ANAME, line_name);

            ammoList.add(map);
        }
        db.close();


        SimpleAdapter arrayAdapter = new SimpleAdapter(ViewOperationActivity.this,
                ammoList,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{TAG_ANAME},
                new int[]{android.R.id.text1});

        for(int i =0; i< lv_add_issuing_detail_ammunition.getCount(); i++) {
            View v = lv_add_issuing_detail_ammunition.getChildAt(i);
            if(((TextView) v.findViewById(R.id.tv_issuing_detail_ammo_id)).getText().toString().isEmpty());
            {
                Spinner ddl_issuing_detail_ammo_description;
                ddl_issuing_detail_ammo_description = (Spinner) v.findViewById(R.id.ddl_issuing_detail_ammo_description);
                ddl_issuing_detail_ammo_description.setAdapter(arrayAdapter);


                ///
                ddl_issuing_detail_ammo_description.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ((TextView) v.findViewById(R.id.tv_issuing_detail_ammo_id)).setText(ammoList.get(i).get(TAG_AID));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        //ignore

                    }
                });
            }
        }


        btnCreateDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < lv_add_issuing_detail_ammunition.getCount(); i++) {
                    View v = lv_add_issuing_detail_ammunition.getChildAt(i);

                    EditText et_issuing_detail_list_qty;
                    Spinner ddl_issuing_detail_ammo_description;

                    et_issuing_detail_list_qty = (EditText) v.findViewById(R.id.et_view_loadout_ammunition_qty);
                    ddl_issuing_detail_ammo_description = (Spinner) v.findViewById(R.id.ddl_issuing_detail_ammo_description);

                    ddl_issuing_detail_ammo_description.getSelectedItemPosition();



                }

            }
        });

        btnSelectPersonnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showSelectPersonnelDialog();
            }
        });


    }

    private void showSelectPersonnelDialog()
    {
        Dialog DialogFragment = new Dialog(ViewOperationActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_select_personnel);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,p_rank varchar(10) NOT NULL, p_name varchar(255) NOT NULL, p_remarks TEXT NOT NULL)");
        Cursor c1 = db.rawQuery("select * from Personnel", null);

        ArrayList<HashMap<String, String>> personnelList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);

            map.put(TAG_PID, line_id);
            map.put(TAG_NAME, line_rank + " " + line_name);

            personnelList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lvSelectPersonnel);
        ListAdapter adapter = new SimpleAdapter(
                ViewOperationActivity.this, //context
                personnelList, //hashmapdata
                R.layout.list_select_personnel, //layout of list
                new String[] { TAG_PID, TAG_PNAME}, //from array
                new int[] {R.id.tvListPersonnelId, R.id.tvListPersonnelName}); //toarray
        // updating listview
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView tvListPersonnelId = (TextView) view.findViewById(R.id.tvListPersonnelId);
                    //toggle and add id to array;

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
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT,p_rank varchar(10) NOT NULL, p_name varchar(255) NOT NULL, p_remarks TEXT NOT NULL)");

        Cursor c1 = db.rawQuery("select * from Personnel", null);

        ArrayList<HashMap<String, String>> PersonnelList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_remarks = c1.getString(3);

            map.put(TAG_PID, line_id);
            map.put(TAG_PRANK, line_rank);
            map.put(TAG_PNAME, line_name);
            map.put(TAG_PREMARKS, line_remarks);

            PersonnelList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lv_Issue_Detail_Info);
        ListAdapter adapter = new SimpleAdapter(
                ViewOperationActivity.this, //context
                PersonnelList, //hashmapdata
                R.layout.list_display_personnel, //layout of list
                new String[]{TAG_PID, TAG_PRANK, TAG_PNAME, TAG_PREMARKS}, //from array
                new int[]{R.id.tv_Personnel_ID_Checklist,
                        R.id.tv_Personnel_Rank_Checklist,
                        R.id.tv_Personnel_Name_Checklist,
                        R.id.tv_Personnel_Remarks_Checklist}); //toarray
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