package com.example.safacgsoca3app;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AmmunitionActivity extends AppCompatActivity {

    private static final String TAG_ID = "a_id";
    private static final String TAG_NAME = "a_name";
    private static final String TAG_DESC = "a_description";
    private static final String TAG_QTY = "a_qty";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ammunition);

        ListView lv = findViewById(R.id.lvAmmunition);
        Button btnAddAmmunition = (Button) findViewById(R.id.btnAddAmmunition);
        Button btnShowLoadout = (Button) findViewById(R.id.btnShowLoadout);

        btnAddAmmunition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAmmoDialog(v);
            }
        });

        btnShowLoadout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showViewAmmoDialog(view);
                String pid = ((TextView) view.findViewById(R.id.tvAmmunitionID)).getText().toString();
            }
        });


    }

    private void showAddAmmoDialog(View view)
    {
        EditText tvAmmoDescription;
        EditText tvAmmoQty;
        Button btnInsertAmmo;

        Dialog DialogFragment = new Dialog(AmmunitionActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_add_ammunition);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        tvAmmoDescription = (EditText) DialogFragment.findViewById(R.id.tvAmmoDescription);
        tvAmmoQty = (EditText) DialogFragment.findViewById(R.id.tvAmmoQty);
        btnInsertAmmo = (Button) DialogFragment.findViewById(R.id.btnInsertAmmunition);

        btnInsertAmmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues content = new ContentValues();

                content.put(TAG_DESC, String.valueOf(tvAmmoDescription.getText()));
                content.put(TAG_QTY, String.valueOf(tvAmmoQty.getText()));

                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.insert("Ammunition", null, content);
                db.close();
            }
        });
    }

    private void showViewAmmoDialog(View view)
    {
        String a_id = ((TextView) view.findViewById(R.id.tvAmmunitionID)).getText().toString();

        Dialog DialogFragment = new Dialog(AmmunitionActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_view_ammunition);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Ammunition (a_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, a_name text NOT NULL, a_description varchar(255) NOT NULL, a_qty float NOT NULL)");
        Cursor c1 = db.rawQuery("select * from Ammunition", null);

        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);
            String line_qty = c1.getString(2);

            map.put(TAG_ID, line_id);
            map.put(TAG_NAME, line_name);
            map.put(TAG_QTY, line_qty);

            ammoList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lvAmmunition);
        ListAdapter adapter = new SimpleAdapter(
                AmmunitionActivity.this, //context
                ammoList, //hashmapdata
                R.layout.list_ammunition, //layout of list
                new String[] { TAG_ID, TAG_NAME, TAG_QTY}, //from array
                new int[] {R.id.tvAmmunitionID, R.id.tvAmmunition, R.id.tvQuantity}); //toarray
        // updating listview
        lv.setAdapter(adapter);

    }
}