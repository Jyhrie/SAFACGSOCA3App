package com.example.safacgsoca3app;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewLoadoutActivity extends AppCompatActivity {

    public static final String TAG_LID = "l_id";
    public static final String TAG_NAME = "l_name";
    public static final String TAG_LAID = "la_id";
    public static final String TAG_LAQTY = "la_qty";
    public static final String TAG_AID = "a_id";
    public static final String TAG_ANAME = "a_name";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_loadout);

        int o_id = parseInt(getIntent().getStringExtra("o_id"));

        Button btnAddLoadout;
        ListView lvLoadouts;

        btnAddLoadout = (Button) findViewById(R.id.btnAddLoadout);
        lvLoadouts = (ListView) findViewById(R.id.lvLoadouts);

        super.onResume();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS loadout (l_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, l_name varchar(255) NOT NULL, o_id integer NOT NULL)");
        Cursor c1 = db.rawQuery("select * from loadout where o_id = " + o_id, null);

        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);

            map.put(TAG_LID, line_id);
            map.put(TAG_NAME, line_name);

            ammoList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lvLoadouts);
        ListAdapter adapter = new SimpleAdapter(
                ViewLoadoutActivity.this, //context
                ammoList, //hashmapdata
                R.layout.list_loadout, //layout of list
                new String[]{TAG_LID, TAG_NAME}, //from array
                new int[]{R.id.tvLoadoutID, R.id.tvLoadoutName}); //toarray
        // updating listview
        lv.setAdapter(adapter);

        lvLoadouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selected_loadout_id = parseInt(((TextView) view.findViewById(R.id.tvLoadoutID)).getText().toString());
                showViewLoadoutDialog(selected_loadout_id);
            }
        });
    }


    public void showViewLoadoutDialog(int l_id) {

        Dialog DialogFragment = new Dialog(ViewLoadoutActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_editdelete_nominal_roll);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        Button btn_view_loadout_save_changes = (Button) DialogFragment.findViewById(R.id.btn_view_loadout_save_changes);
        ListView lv_view_loadout_ammunition_list = (ListView) DialogFragment.findViewById(R.id.lv_view_loadout_ammunition_list);


        //populate loadout ammunition

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS loadout_ammunition (la_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, l_id varchar(255) NOT NULL, a_id integer NOT NULL, la_qty number NOT NULL)");
        Cursor c1 = db.rawQuery("select la_id, a_id, la_qty from loadout_ammunition where l_id = " + l_id, null);

        ArrayList<HashMap<String, String>> loadoutAmmoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {

            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_aid = c1.getString(1);
            String line_qty = c1.getString(2);

            map.put(TAG_LAID, line_id);
            map.put(TAG_AID, line_aid);
            map.put(TAG_LAQTY, line_qty);
            loadoutAmmoList.add(map);

        }
        db.close();

        ListView lv = DialogFragment.findViewById(R.id.lv_view_loadout_ammunition_list);
        ;
        ListAdapter adapter = new SimpleAdapter(
                ViewLoadoutActivity.this, //context
                loadoutAmmoList, //hashmapdata
                R.layout.list_view_loadout_ammunition, //layout of list
                new String[]{TAG_LAID, TAG_AID, TAG_LAQTY}, //from array
                new int[]{R.id.tv_loadout_ammo_id, R.id.tv_loadout_ammo_ammo_id, R.id.et_view_loadout_ammunition_qty }); //toarray
        // updating listview
        lv.setAdapter(adapter);

        //populate ammo dropdown list

        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Ammunition (a_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, a_description varchar(255) NOT NULL, a_qty float NOT NULL)");
        c1 = db.rawQuery("select * from Ammunition", null);
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

        for (int i = 0; i < lv_view_loadout_ammunition_list.getCount(); i++) {
            View v = lv_view_loadout_ammunition_list.getChildAt(i);

            Spinner ddl_view_loadout_ammunition_type = (Spinner) v.findViewById(R.id.ddl_view_loudout_ammunition_type);
            SimpleAdapter arrayAdapter = new SimpleAdapter(ViewLoadoutActivity.this,
                    ammoList,
                    android.R.layout.simple_spinner_dropdown_item,
                    new String[]{TAG_ANAME},
                    new int[]{android.R.id.text1});
            ddl_view_loadout_ammunition_type.setAdapter(arrayAdapter);

            HashMap<String, String> ddlEntry = new HashMap<String, String>();
            TextView tv_loadout_ammo_ammo_id = (TextView) v.findViewById(R.id.tv_loadout_ammo_ammo_id);
            for(int j = 0; j<ammoList.size(); j++)
            {
                ddlEntry = ammoList.get(j);
                if(ddlEntry.get(TAG_AID) == tv_loadout_ammo_ammo_id.getText().toString())
                {
                    ddl_view_loadout_ammunition_type.setSelection(j);
                }
                else if (tv_loadout_ammo_ammo_id.getText().toString() == "-1") {

                }
            }

            ddl_view_loadout_ammunition_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    ((TextView) v.findViewById(R.id.tv_loadout_ammo_ammo_id)).setText(ammoList.get(i).get(TAG_AID));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //ignore

                }
            });
        }

        btn_view_loadout_save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < lv_view_loadout_ammunition_list.getCount(); i++) {
                    View v = lv_view_loadout_ammunition_list.getChildAt(i);
                    TextView tv_loadout_ammo_id = (TextView) v.findViewById(R.id.tv_loadout_ammo_id);
                    TextView tv_loadout_ammo_ammo_id = (TextView) v.findViewById(R.id.tv_loadout_ammo_ammo_id);

                    EditText et_view_loadout_ammunition_qty = (EditText) v.findViewById(R.id.et_view_loadout_ammunition_qty);
                    Spinner ddl_view_loadout_ammunition_type = (Spinner) v.findViewById(R.id.ddl_view_loudout_ammunition_type);

                    int loadout_ammo_id = parseInt(tv_loadout_ammo_id.getText().toString());
                    int loadout_ammo_ammo_id = parseInt(tv_loadout_ammo_ammo_id.getText().toString());
                    float loadout_ammunition_qty = parseFloat(et_view_loadout_ammunition_qty.getText().toString());

                    if (loadout_ammo_id == -1)
                    {

                        SQLiteDatabase db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                        db.execSQL("CREATE TABLE IF NOT EXISTS loadout_ammunition (la_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, l_id varchar(255) NOT NULL, a_id integer NOT NULL, la_qty number NOT NULL)");

                        ContentValues content = new ContentValues();
                        content.put(TAG_AID, loadout_ammo_ammo_id);
                        content.put(TAG_LAQTY, loadout_ammunition_qty);
                        content.put(TAG_LID, l_id);
                        db.insert("loadout_ammunition", null, content);
                        db.close();
                    }
                    else
                    {

                        SQLiteDatabase db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                        db.execSQL("CREATE TABLE IF NOT EXISTS loadout_ammunition (la_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, l_id varchar(255) NOT NULL, a_id integer NOT NULL, la_qty number NOT NULL)");
                        ContentValues content = new ContentValues();
                        content.put(TAG_AID, loadout_ammo_ammo_id);
                        content.put(TAG_LAQTY, loadout_ammunition_qty);
                        content.put(TAG_LID, l_id);
                        db.update("loadout_ammunition", content, "la_id", new String[]{String.valueOf(loadout_ammo_id)});

                    }

                }
            }
        });




    }
}
