package com.example.safacgsoca3app;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ViewLoadoutActivity extends AppCompatActivity {

    public static final String TAG_LID="l_id";
    public static final String TAG_NAME="l_name";
    public static final String TAG_LAID="la_id";
    public static final String TAG_LANAME="la_name";
    public static final String TAG_LAQTY="la_qty";


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
                R.layout.list_ammunition, //layout of list
                new String[] { TAG_LID, TAG_NAME}, //from array
                new int[] {R.id.tvLoadoutID, R.id.tvLoadoutName}); //toarray
        // updating listview
        lv.setAdapter(adapter);

        lvLoadouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selected_loadout_id = parseInt(((TextView) view.findViewById(R.id.tvLoadoutID)).getText().toString());
                showViewLoadoutDialog(o_id, selected_loadout_id);
            }
        });
    }

    public void showViewLoadoutDialog(int o_id, int l_id)
    {


        Dialog DialogFragment = new Dialog(ViewLoadoutActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_editdelete_nominal_roll);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        Button btn_view_loadout_save_changes = (Button) DialogFragment.findViewById(R.id.btn_view_loadout_save_changes);
        ListView lv_view_loadout_ammunition_list = (ListView) DialogFragment.findViewById(R.id.lv_view_loadout_ammunition_list);


        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS loadout_ammunition (la_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, l_id varchar(255) NOT NULL, a_id integer NOT NULL, la_qty number NOT NULL)");
        Cursor c1 = db.rawQuery("select la_id, a_id, la_qty from loadout_ammunition where l_id = " + l_id, null);

        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);
            String line_qty = c1.getString(2);

            map.put(TAG_LAID, line_id);
            map.put(TAG_LANAME, line_name);
            map.put(TAG_LAQTY, line_qty);

            ammoList.add(map);
        }
        db.close();

        ListView lv = DialogFragment.findViewById(R.id.lv_view_loadout_ammunition_list);;
        ListAdapter adapter = new SimpleAdapter(
                ViewLoadoutActivity.this, //context
                ammoList, //hashmapdata
                R.layout.list_ammunition, //layout of list
                new String[] { TAG_LID, TAG_NAME}, //from array
                new int[] {R.id.tvLoadoutID, R.id.tvLoadoutName}); //toarray
        // updating listview
        lv.setAdapter(adapter);

        
    }
}