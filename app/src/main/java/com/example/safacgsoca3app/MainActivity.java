package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private static final String TAG_ID = "o_id";
    private static final String TAG_NAME = "o_name";
    private static final String TAG_KAH = "o_kah";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvExercises;

        FloatingActionButton btnFloatingAddExercise;

        lvExercises = (ListView) findViewById(R.id.lvOperations);

        btnFloatingAddExercise = (FloatingActionButton) findViewById(R.id.btnFloatingAddExercise);


        btnFloatingAddExercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showAddExerciseDialog();
            }
        });

        lvExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String oid = ((TextView) view.findViewById(R.id.tvOperationListId)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), ViewOperationActivity.class);
                intent.putExtra(TAG_ID, oid);
                startActivity(intent);
            }
        });


    }

    protected void onResume() {
        super.onResume();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS operation (o_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, o_name varchar(255) NOT NULL, o_kah text NOT NULL)");
        Cursor c1 = db.rawQuery("select * from operation", null);

        ArrayList<HashMap<String, String>> opsList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);
            String line_qty = c1.getString(2);

            map.put(TAG_ID, line_id);
            map.put(TAG_NAME, line_name);
            map.put(TAG_KAH, line_qty);

            opsList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lvOperations);
        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this, //context
                opsList, //hashmapdata
                R.layout.list_operation, //layout of list
                new String[]{TAG_ID, TAG_NAME, TAG_KAH}, //from array
                new int[]{R.id.tvOperationListId, R.id.tvOperationListName, R.id.tvOperationListKAH}); //toarray
        // updating listview
        lv.setAdapter(adapter);

    }

    private void showAddExerciseDialog() {

        Dialog DialogFragment = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_add_exercise);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        Button btnInsertExercise;
        EditText etAddExerciseName;
        EditText etAddExerciseKAH;

        btnInsertExercise = (Button) DialogFragment.findViewById(R.id.btnInsertExercise);
        etAddExerciseName = (EditText) DialogFragment.findViewById(R.id.etAddExerciseName);
        etAddExerciseKAH = (EditText) DialogFragment.findViewById(R.id.etAddExerciseKAH);

        btnInsertExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS operations (o_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, o_name varchar(255) NOT NULL, o_kah text NOT NULL)");

                ContentValues content = new ContentValues();

                content.put(TAG_NAME, String.valueOf(etAddExerciseName.getText()));
                content.put(TAG_KAH, String.valueOf(etAddExerciseKAH.getText()));

                db.insert("exercises", null, content);

                //get newest id
                Cursor cursor = db.rawQuery("SELECT o_id FROM operations ORDER BY o_id DESC LIMIT 1", null);
                cursor.moveToLast();
                int insertedID = cursor.getInt(0);

                db.close();

                //start new intent on exercise page
                Intent i = new Intent(getApplicationContext(), ViewOperationActivity.class);
                //push id to next page
                i.putExtra(TAG_ID, insertedID);
                startActivity(i);

                DialogFragment.dismiss();
            }
        });
    }
}


