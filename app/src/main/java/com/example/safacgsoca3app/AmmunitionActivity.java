package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.HashMap;

public class AmmunitionActivity extends AppCompatActivity {

    private static final String TAG_P_ID = "p_id";
    private static final String TAG_P_NAME = "p_name";
    private static final String TAG_P_NRIC = "p_nric";

    private static final String TAG_O_ID = "o_id";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_KAH = "o_kah";

    private static final String TAG_OP_ID = "op_id";

    private static final String TAG_D_ID = "d_id";

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ammunition);

        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_O_ID);

        ListView lv = findViewById(R.id.lvAmmunition);
        Button btnAddAmmunition = (Button) findViewById(R.id.btnAddAmmunition);
        Button btnShowLoadout = (Button) findViewById(R.id.btnShowLoadout);

        btnAddAmmunition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAmmoDialog(v, o_id);
            }
        });

        btnShowLoadout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ViewLoadoutActivity.class);
                //push id to next page
                i.putExtra(TAG_O_ID, String.valueOf(o_id));
                startActivity(i);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showEditAmmoDialog(view);
            }
        });
    }

    private void showEditAmmoDialog(View view)
    {
        String a_id = ((TextView) view.findViewById(R.id.tvAmmunitionID)).getText().toString();
        showDeleteAmmoDialog("Are you sure you want to remove this ammunition?", a_id);

    }

    private void showAddAmmoDialog(View view, String o_id)
    {

        fragment_add_ammunition fragment = new fragment_add_ammunition();
        Bundle args = new Bundle();
        args.putString(TAG_O_ID, o_id);
        fragment.setArguments(args);
        fragment.show(getSupportFragmentManager(), "fragment_add_ammunition");

        /*
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


                boolean _dataValidationPass = true;
                String _errorMessage = "there is no empty field, something went wrong";
                if(tvAmmoDescription.getText().toString().isEmpty())
                {
                    _dataValidationPass = false;
                    _errorMessage = "Please enter an ammo description";
                }
                else if(tvAmmoQty.getText().toString().isEmpty())
                {
                    _dataValidationPass = false;
                    _errorMessage = "Please enter an ammo quantity";
                }

                if(_dataValidationPass == false)
                {
                    showErrorAlertDialog(view, _errorMessage);
                    return;
                }

                ContentValues content = new ContentValues();

                content.put(TAG_A_NAME, String.valueOf(tvAmmoDescription.getText()));
                content.put(TAG_A_QTY, String.valueOf(tvAmmoQty.getText()));
                content.put(TAG_O_ID, o_id);

                SQLiteDatabase db;
                Log.i("test", "test");
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.insert("ammunition", null, content);
                db.close();

                DialogFragment.dismiss();
                onResume();
            }
        });

         */
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        SQLiteDatabase db;

        Intent intent = getIntent();
        String o_id = intent.getStringExtra(TAG_O_ID);

        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("select a_id, a_name, a_qty from ammunition where o_id = " + o_id, null);

        ArrayList<HashMap<String, String>> ammoList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);
            String line_qty = c1.getString(2);

            map.put(TAG_A_ID, line_id);
            map.put(TAG_A_NAME, line_name);
            map.put(TAG_A_QTY, line_qty);

            ammoList.add(map);
        }
        db.close();

        ListView lv = findViewById(R.id.lvAmmunition);
        ListAdapter adapter = new SimpleAdapter(
                AmmunitionActivity.this, //context
                ammoList, //hashmapdata
                R.layout.list_ammunition, //layout of list
                new String[] { TAG_A_ID, TAG_A_NAME, TAG_A_QTY}, //from array
                new int[] {R.id.tvAmmunitionID, R.id.tvAmmunition, R.id.tvQuantity}); //toarray
        // updating listview
        lv.setAdapter(adapter);
    }

    public void showErrorAlertDialog(View v, String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Error");
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }});
        alert.show();
    }

    public void showDeleteAmmoDialog(String message, String a_id)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Remove Ammunition?");
        alert.setMessage(message);
        alert.setPositiveButton("Remove", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                db.execSQL("DELETE FROM Ammunition WHERE a_id ="+ a_id);
                db.execSQL("DELETE FROM Personnel_Ammunition WHERE a_id ="+ a_id);
                db.close();
                onResume();

            }});
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }


}