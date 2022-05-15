package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewLoadoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_loadout);

        Button btnAddLoadout;
        ListView lvLoadouts;

        btnAddLoadout = (Button) findViewById(R.id.btnAddLoadout);
        lvLoadouts = (ListView) findViewById(R.id.lvLoadouts);



    }

    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Ammunition (a_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, a_description varchar(255) NOT NULL, a_qty float NOT NULL)");
        Cursor c1 = db.rawQuery("select * from Ammunition", null);

        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);

            map.put(TAG_ID, line_id);
            map.put(TAG_NAME, line_name);

            ammoList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lvLoadouts);
        ListAdapter adapter = new SimpleAdapter(
                ViewLoadoutActivity.this, //context
                ammoList, //hashmapdata
                R.layout.list_ammunition, //layout of list
                new String[] { TAG_ID, TAG_NAME}, //from array
                new int[] {R.id.tvAmmunitionID, R.id.tvAmmunition, R.id.tvQuantity}); //toarray
        // updating listview
        lv.setAdapter(adapter);

    }
}