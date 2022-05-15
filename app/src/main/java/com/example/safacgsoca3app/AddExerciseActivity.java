package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddExerciseActivity extends AppCompatActivity {

    private static final String TAG_ID = "e_id";
    private static final String TAG_NAME = "e_name";
    private static final String TAG_KAH = "e_kah";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        Button btnInsertExercise;
        EditText etAddExerciseName;
        EditText etAddExerciseKAH;

        btnInsertExercise = (Button) findViewById(R.id.btnInsertExercise);
        etAddExerciseName = (EditText) findViewById(R.id.etAddExerciseName);
        etAddExerciseKAH = (EditText) findViewById(R.id.etAddExerciseKAH);

        btnInsertExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS exercises (e_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, e_name varchar(255) NOT NULL, e_kah text NOT NULL)");

                ContentValues content = new ContentValues();

                content.put(TAG_NAME, String.valueOf(etAddExerciseName.getText()));
                content.put(TAG_KAH, String.valueOf(etAddExerciseKAH.getText()));

                db.insert("exercises", null, content);

                //get newest id
                Cursor cursor = db.rawQuery("SELECT e_id FROM exercises ORDER BY e_id DESC LIMIT 1", null);
                cursor.moveToLast();
                int insertedID = cursor.getInt(0);

                db.close();

                //start new intent on exercise page
                Intent i = new Intent(getApplicationContext(), ViewOperationActivity.class);
                //push id to next page
                i.putExtra(TAG_ID, insertedID);
                startActivity(i);

                finish();

            }
        });
    }
}