package com.example.safacgsoca3app;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fragment_add_ammunition extends DialogFragment {


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

    private static final String TAG_DDL_AMMO_ID = "ddl_ammo_id";
    private static final String TAG_DDL_AMMO_NAME = "ddl_ammo_name";

    AmmunitionActivity source;
    String o_id;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_add_ammunition, container, false);
        Context context = getContext();

        source = (AmmunitionActivity) getActivity();

        Bundle args = getArguments();

        o_id = args.getString(TAG_O_ID);

        EditText tvAmmoQty;
        Button btnInsertAmmo;
        Spinner ddl_add_ammunition;
        SearchView sv_filter_add_ammo;
        AutoCompleteTextView actv_add_ammo;

        tvAmmoQty = (EditText) v.findViewById(R.id.tvAmmoQty);
        btnInsertAmmo = (Button) v.findViewById(R.id.btnInsertAmmunition);
        actv_add_ammo = (AutoCompleteTextView) v.findViewById(R.id.actv_add_ammo);


        SQLiteDatabase db;
        db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT ddl_ammo_id, ddl_ammo_name FROM ddl_ammo", null);
        ArrayList<String> actv_ammo_list = new ArrayList<String>();
        while(c1.moveToNext())
        {
            actv_ammo_list.add(c1.getString(1));
        }
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, actv_ammo_list.toArray(new String[0]));
        actv_add_ammo.setAdapter(adapter);

        btnInsertAmmo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                boolean _dataValidationPass = true;
                String _errorMessage = "there is no empty field, something went wrong";
                if(actv_add_ammo.getText().toString().isEmpty())
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

                content.put(TAG_A_NAME, String.valueOf(actv_add_ammo.getText()));
                content.put(TAG_A_QTY, String.valueOf(tvAmmoQty.getText()));
                content.put(TAG_O_ID, o_id);

                SQLiteDatabase db;
                Log.i("test", "test");
                db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                db.insert("ammunition", null, content);
                db.close();
                dismiss();
                source.onResume();
            }
        });

        return v;

    }

    public void showErrorAlertDialog(View v, String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Error");
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }});
        alert.show();
    }
}
