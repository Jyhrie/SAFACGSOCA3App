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
import android.util.Log;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_P_ID = "p_id";
    private static final String TAG_P_NAME = "p_name";
    private static final String TAG_P_NRIC = "p_nric";

    private static final String TAG_O_ID = "o_id";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_KAH = "o_kah";

    private static final String TAG_OP_ID = "op_id";

    private static final String TAG_D_ID = "d_id";
    private static final String TAG_D_NAME = "d_name";

    private static final String TAG_PA_ID = "pa_id";
    private static final String TAG_PA_ISSUE_QTY = "pa_issue_qty";
    private static final String TAG_PA_ISSUED = "pa_issued";
    private static final String TAG_PA_RETURNED = "pa_returned";
    private static final String TAG_PA_EXPENEDED = "pa_expended";
    private static final String TAG_PA_SPOILED = "pa_spoiled";

    private static final String TAG_A_ID = "a_id";
    private static final String TAG_A_NAME = "a_name";
    private static final String TAG_A_QTY = "a_qty";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initialize_database(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvExercises;
        FloatingActionButton btnFloatingAddExercise;
        Button btnViewNominalRoll;

        lvExercises = (ListView) findViewById(R.id.lvOperations);
        btnFloatingAddExercise = (FloatingActionButton) findViewById(R.id.btnFloatingAddExercise);
        btnViewNominalRoll = (Button) findViewById(R.id.btn_view_nominal_roll);


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
                intent.putExtra(TAG_O_ID, oid);
                startActivity(intent);
            }
        });

        lvExercises.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String oid = ((TextView) view.findViewById(R.id.tvOperationListId)).getText().toString();
                DeleteDialog(oid);
                return true;
                }

        });

        btnViewNominalRoll.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), NominalRollActivity.class);
                startActivity(i);
            }
        });

        Button btnTesting;
        btnTesting = findViewById(R.id.btnTesting);
        btnTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), GenerateDocumentsActivity.class);
                startActivity(i);
            }
        });

    }

    protected void onResume() {
        super.onResume();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("select * from operation", null);

        ArrayList<HashMap<String, String>> opsList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);
            String line_qty = c1.getString(2);

            map.put(TAG_O_ID, line_id);
            map.put(TAG_O_NAME, line_name);
            map.put(TAG_O_KAH, line_qty);

            opsList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lvOperations);
        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this, //context
                opsList, //hashmapdata
                R.layout.list_operation, //layout of list
                new String[]{TAG_O_ID, TAG_O_NAME, TAG_O_KAH}, //from array
                new int[]{R.id.tvOperationListId, R.id.tvOperationListName, R.id.tvOperationListKAH}); //toarray
        // updating listview
        lv.setAdapter(adapter);


        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
    }

    private void showAddExerciseDialog() {
        fragment_Add_Operation fragment = new fragment_Add_Operation();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "fragment_assign_personnel_ammunition");


        /*
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
        etAddExerciseKAH = (EditText) DialogFragment.findViewById(R.id.etName);






        btnInsertExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

                ContentValues content = new ContentValues();

                content.put(TAG_NAME, String.valueOf(etAddExerciseName.getText()));
                content.put(TAG_KAH, String.valueOf(etAddExerciseKAH.getText()));

                db.insert("operation", null, content);
                db.close();

                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

                //get newest id
                int insertedID = -1;
                Cursor cursor = db.rawQuery("SELECT o_id FROM operation ORDER BY o_id DESC LIMIT 1 ", null);
                while(cursor.moveToNext())
                {
                    insertedID = cursor.getInt(0);
                    Log.i("data has entry", String.valueOf(insertedID));
                }


                db.close();

                //start new intent on exercise page
                Intent i = new Intent(getApplicationContext(), ViewOperationActivity.class);
                //push id to next page
                i.putExtra(TAG_ID, String.valueOf(insertedID));
                startActivity(i);

                DialogFragment.dismiss();
            }
        });*/
    }

    public void initialize_database(boolean reset)
    {
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        if(reset == true) {
            Log.i("purging data", "all databases");
            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table'", null);
            List<String> tables = new ArrayList<>();
            while (c.moveToNext()) {
                if (!(c.getString(0).equals("sqlite_sequence"))) {

                    tables.add(c.getString(0));
                }
            }

            for (String table : tables) {
                String deleteQuery = "DELETE FROM " + table;
                String dropQuery = "DROP TABLE IF EXISTS " + table;
                db.execSQL(deleteQuery);
                db.execSQL(dropQuery);
            }

            db.execSQL("DELETE FROM sqlite_sequence");
        }

        db.execSQL("CREATE TABLE IF NOT EXISTS personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, p_rank varchar(255) NOT NULL, p_name varchar(255) NOT NULL, p_nric text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS operation (o_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, o_name varchar(255) NOT NULL, o_kah text NOT NULL, o_unit text)");
        db.execSQL("CREATE TABLE IF NOT EXISTS operation_personnel (op_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, p_id integer NOT NULL, o_id integer NOT NULL, d_id integer)");
        db.execSQL("CREATE TABLE IF NOT EXISTS ammunition (a_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, o_id integer NOT NULL, a_name varchar(255) NOT NULL, a_qty number NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS detail (d_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, d_name text NOT NULL, o_id integer NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS personnel_ammunition (pa_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, op_id integer NOT NULL, a_id integer NOT NULL, pa_issue_qty number NOT NULL, pa_issued number, pa_expended number, pa_returned number, pa_spoiled number)");
        db.execSQL("CREATE TABLE IF NOT EXISTS transaction_data (" +
                "td_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "pa_id integer NOT NULL, " +
                "doc_id integer NOT NULL, " +

                // Ammunition name to split documents based on Ammo Type
                "td_a_name text NOT NULL, " +

                // Personnel name associated with each entry in each document
                "td_p_name text NOT NULL, " +

                // issued/returned/expended/spoiled associated with each entry in each document
                "td_issued number NOT NULL, " +
                "td_returned number," +
                "td_expended number," +
                "td_spoiled number, " +

                // Issue and Return date/time, signatures
                "td_issuedatetime text, " +
                "td_issuesignature image, " +
                "td_returndatetime text, " +
                "td_returnsignature image, " +

                // Exported or not
                "td_exported bool DEFAULT 0 NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS document (doc_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, d_name text NOT NULL, o_name text NOT NULL, o_unit text NOT NULL, doc_date text, doc_closed bool DEFAULT 0 NOT NULL )");



        if(reset == true) {
            addexampleOperation();
            addexamplePersonnel();
            addexampleOperationPersonnel();
            addexampleAmmunition();
            addexamplePersonnelAmmunition();
            addexampleDetail();
        }

        db.close();
    }

    private void DeleteDialog(String oid) {
        Dialog DialogFragment = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar);
        DialogFragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        DialogFragment.setContentView(R.layout.dialog_delete_exercise);
        DialogFragment.setCancelable(true);
        DialogFragment.show();

        String OperationName = null;

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS operation (o_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, o_name varchar(255) NOT NULL, o_kah text NOT NULL)");
        Cursor cursor = db.rawQuery("SELECT o_name FROM operation WHERE o_id = " + oid, null);
        while(cursor.moveToNext())
        {
            OperationName = cursor.getString(0);
        }
        db.close();

        TextView tv_OperationName = (TextView) DialogFragment.findViewById(R.id.tv_ExerciseName);
        tv_OperationName.setText(OperationName + "?");

        Button btn_confirm = (Button) DialogFragment.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS operation (o_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, o_name varchar(255) NOT NULL, o_kah text NOT NULL)");
                db.execSQL("DELETE FROM operation WHERE o_id = " + oid);
                db.close();
                DialogFragment.dismiss();
                onResume();
            }
        });
    }

    private void addexampleOperation(){
        ContentValues content = new ContentValues();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

        content.put("o_name", "OPS GLUON");
        content.put("o_unit", "SOF PTCO");
        content.put("o_kah", "Conducting: ME4 Yuen Weng Kin\nSupervising: ME3 Teo Kwee Teck\nSafety: ME3 Raymond Tan\nLocation: Changi Naval Base\nDate: 12/09/2022");
        db.insert("operation", null, content);

        content.put("o_name", "OPS GUARDIAN ANGEL");
        content.put("o_unit", "SOF PTCO");
        content.put("o_kah", "Conducting: ME4 Khoo Wei Liang\nSupervising: ME2 Fung Xue Ming\nSafety: ME1 Melvin\nLocation: Nee Soon Camp\nDate: 18/07/2022");
        db.insert("operation", null, content);

        content.put("o_name", "RANGE: PULAU HANTU");
        content.put("o_unit", "SOF PTCO");
        content.put("o_kah", "Conducting: ME2 Fung Xue Ming\nSupervising: ME1 Melvin\nSafety: ME1 Lincoln\nLocation: Pulau Hantu\nDate: 20/04/2023");
        db.insert("operation", null, content);
        db.close();
    }

    private void addexamplePersonnel(){
        ContentValues content = new ContentValues();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

        content.put("p_rank", "LCP");
        content.put("p_name", "Zackermax See");
        content.put("p_nric", "220I");
        db.insert("personnel", null, content);

        content.put("p_rank", "LCP");
        content.put("p_name", "Do Le");
        content.put("p_nric", "485H");
        db.insert("personnel", null, content);

        content.put("p_rank", "PTE");
        content.put("p_name", "Jun Hao");
        content.put("p_nric", "420F");
        db.insert("personnel", null, content);

        content.put("p_rank", "CFC");
        content.put("p_name", "Gabriel");
        content.put("p_nric", "488H");
        db.insert("personnel", null, content);

        content.put("p_rank", "3SG");
        content.put("p_name", "Shih Rong");
        content.put("p_nric", "Chao Keng Warrior");
        db.insert("personnel", null, content);

        content.put("p_rank", "LCP");
        content.put("p_name", "Daniel");
        content.put("p_nric", "696Z");
        db.insert("personnel", null, content);

        content.put("p_rank", "PTE");
        content.put("p_name", "Lambert");
        content.put("p_nric", "353J");
        db.insert("personnel", null, content);

        content.put("p_rank", "ME1");
        content.put("p_name", "Reeve");
        content.put("p_nric", "007J");
        db.insert("personnel", null, content);

        content.put("p_rank", "PTE");
        content.put("p_name", "Bernard");
        content.put("p_nric", "101D");
        db.insert("personnel", null, content);

        content.put("p_rank", "PTE");
        content.put("p_name", "Aidan");
        content.put("p_nric", "404E");
        db.insert("personnel", null, content);

        content.put("p_rank", "LCP");
        content.put("p_name", "Jing Yan");
        content.put("p_nric", "666F");
        db.insert("personnel", null, content);

        db.close();
    }

    private void addexampleOperationPersonnel(){
        ContentValues content = new ContentValues();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

        content.put("p_id", "1");
        content.put("o_id", "1");
        content.put("d_id", "1");
        db.insert("operation_personnel", null, content);

        content.put("p_id", "1");
        content.put("o_id", "2");
        content.put("d_id", "3");
        db.insert("operation_personnel", null, content);

        content.put("p_id", "2");
        content.put("o_id", "1");
        content.put("d_id", "1");
        db.insert("operation_personnel", null, content);

        content.put("p_id", "2");
        content.put("o_id", "2");
        content.put("d_id", "3");
        db.insert("operation_personnel", null, content);

        content.put("p_id", "3");
        content.put("o_id", "1");
        content.put("d_id", "1");
        db.insert("operation_personnel", null, content);

        content.put("p_id", "3");
        content.put("o_id", "2");
        content.put("d_id", "3");
        db.insert("operation_personnel", null, content);

        content.put("p_id", "4");
        content.put("o_id", "1");
        content.put("d_id", "2");
        db.insert("operation_personnel", null, content);

        content.put("p_id", "4");
        content.put("o_id", "3");
        content.put("d_id", "4");
        db.insert("operation_personnel", null, content);

        content.put("p_id", "5");
        content.put("o_id", "1");
        content.put("d_id", "2");
        db.insert("operation_personnel", null, content);

        content.put("p_id", "5");
        content.put("o_id", "3");
        content.put("d_id", "4");
        db.insert("operation_personnel", null, content);

        db.close();
    }

    private void addexampleDetail(){
        ContentValues content = new ContentValues();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

        content.put("o_id", "1");
        content.put("d_name", "ALPHA");
        db.insert("Detail", null, content);

        content.put("o_id", "1");
        content.put("d_name", "BRAVO");
        db.insert("Detail", null, content);

        content.put("o_id", "2");
        content.put("d_name", "TRIDENT");
        db.insert("Detail", null, content);

        content.put("o_id", "3");
        content.put("d_name", "DETAIL 1");
        db.insert("Detail", null, content);

        db.close();
    }

    private void addexampleAmmunition(){
        ContentValues content = new ContentValues();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

        content.put("o_id", "1");
        content.put("a_name", "5.56 Ball");
        content.put("a_qty", "1000");
        db.insert("ammunition", null, content);

        content.put("o_id", "1");
        content.put("a_name", "7.62 Ball");
        content.put("a_qty", "1250");
        db.insert("ammunition", null, content);

        content.put("o_id", "1");
        content.put("a_name", "SFG 87");
        content.put("a_qty", "48");
        db.insert("ammunition", null, content);

        content.put("o_id", "1");
        content.put("a_name", "GREN HAND SMK RED");
        content.put("a_qty", "96");
        db.insert("ammunition", null, content);

        content.put("o_id", "3");
        content.put("a_name", "GREN HAND SMK RED");
        content.put("a_qty", "96");
        db.insert("ammunition", null, content);

        content.put("o_id", "3");
        content.put("a_name", "5.56 FRANGIBLE");
        content.put("a_qty", "1000");
        db.insert("ammunition", null, content);

        content.put("o_id", "3");
        content.put("a_name", "7.62 LAPUA SCENAR");
        content.put("a_qty", "400");
        db.insert("ammunition", null, content);

        db.close();
    }

    private void addexamplePersonnelAmmunition(){
        ContentValues content = new ContentValues();
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);

        content.put("op_id", "1");
        content.put("a_id", "1");
        content.put("pa_issue_qty", "120");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "1");
        content.put("a_id", "3");
        content.put("pa_issue_qty", "2");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "2");
        content.put("a_id", "1");
        content.put("pa_issue_qty", "120");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "2");
        content.put("a_id", "3");
        content.put("pa_issue_qty", "2");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "3");
        content.put("a_id", "1");
        content.put("pa_issue_qty", "120");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "3");
        content.put("a_id", "3");
        content.put("pa_issue_qty", "2");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "4");
        content.put("a_id", "1");
        content.put("pa_issue_qty", "120");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "4");
        content.put("a_id", "3");
        content.put("pa_issue_qty", "2");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "5");
        content.put("a_id", "2");
        content.put("pa_issue_qty", "200");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "5");
        content.put("a_id", "3");
        content.put("pa_issue_qty", "2");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");

        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "6");
        content.put("a_id", "2");
        content.put("pa_issue_qty", "200");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "6");
        content.put("a_id", "3");
        content.put("pa_issue_qty", "2");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "7");
        content.put("a_id", "2");
        content.put("pa_issue_qty", "200");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "7");
        content.put("a_id", "3");
        content.put("pa_issue_qty", "2");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "8");
        content.put("a_id", "2");
        content.put("pa_issue_qty", "200");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "8");
        content.put("a_id", "3");
        content.put("pa_issue_qty", "2");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "9");
        content.put("a_id", "1");
        content.put("pa_issue_qty", "90");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "9");
        content.put("a_id", "3");
        content.put("pa_issue_qty", "2");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "10");
        content.put("a_id", "1");
        content.put("pa_issue_qty", "90");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        content.put("op_id", "10");
        content.put("a_id", "3");
        content.put("pa_issue_qty", "2");
        content.put("pa_issued", "0");
        content.put("pa_returned", "0");
        content.put("pa_expended", "0");
        content.put("pa_spoiled", "0");
        db.insert("personnel_ammunition", null, content);

        db.close();
    }
}


