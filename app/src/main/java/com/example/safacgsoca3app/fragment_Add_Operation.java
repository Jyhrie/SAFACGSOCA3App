package com.example.safacgsoca3app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_Add_Operation extends DialogFragment implements RecyclerViewInterface {

    private static final String TAG_P_ID = "p_id";
    private static final String TAG_P_NAME = "p_name";
    private static final String TAG_P_NRIC = "p_nric";

    private static final String TAG_O_ID = "o_id";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_KAH = "o_kah";
    private static final String TAG_O_UNIT = "o_unit";
    private static final String TAG_O_DATE = "o_date";
    private static final String TAG_O_LOC = "o_loc";
    private static final String TAG_O_OPS = "o_ops";

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

    private static final String TAG_DESIGNATION_NAME = "designation_name";
    private static final String TAG_DESIGNATION = "designation";


    public ArrayList<HashMap<String,String>> data;
    adapter_Operation_KAH rvAdapter;
    RecyclerViewInterface recyclerViewInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_add_operation, container, false);
        Context context = getContext();
        recyclerViewInterface = this;

        Button btnInsertExercise;
        EditText etAddExerciseName;
        EditText etDate;
        EditText etLocation;
        EditText etUnit;
        TextView tv_ops_or_range;
        Switch sw_ops_or_range;
        TextView tv_sw_togglestate;

        btnInsertExercise = (Button) v.findViewById(R.id.btnInsertExercise);
        etAddExerciseName = (EditText) v.findViewById(R.id.etAddExerciseName);
        etDate = (EditText) v.findViewById(R.id.etDate);
        etLocation = (EditText) v.findViewById(R.id.etLocation);
        etUnit = (EditText) v.findViewById(R.id.et_unit);
        tv_ops_or_range = (TextView) v.findViewById(R.id.tv_ops_or_range);
        sw_ops_or_range = (Switch) v.findViewById(R.id.sw_ops_or_range);
        tv_sw_togglestate = (TextView) v.findViewById(R.id.tv_switch_togglestate);

        sw_ops_or_range.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == false)
                {
                    tv_ops_or_range.setText(" Ops");
                    tv_sw_togglestate.setText("0");
                }
                if(b == true)
                {
                    tv_ops_or_range.setText(" Range");
                    tv_sw_togglestate.setText("1");
                }
            }
        });


        HashMap<String,String> _emptymap = new HashMap<String,String>();
        _emptymap.put(TAG_DESIGNATION, "");
        _emptymap.put(TAG_DESIGNATION_NAME, "");


        data = new ArrayList<HashMap<String,String>>();
        data.add(new HashMap<String,String>(_emptymap));

        rvAdapter = new adapter_Operation_KAH(
                context,
                data,
                recyclerViewInterface
        );

        RecyclerView rv = v.findViewById(R.id.rv_kah);
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(context));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);


        Button btn_add_designation;
        btn_add_designation = (Button) v.findViewById(R.id.btn_add_new_designation);
        btn_add_designation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.add(new HashMap<String,String>(_emptymap));
                Log.i(data.toString(), "adding empty");
                rvAdapter.notifyItemInserted(rvAdapter.getItemCount());
            }
        });

        btnInsertExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //data validation
                boolean _dataValidationPass = true;
                String _errorMessage = "there is no empty field, something went wrong";
                if(etAddExerciseName.getText().toString().isEmpty())
                {
                    _dataValidationPass = false;
                    _errorMessage = "Please add an exercise name, then try again";
                }
                else if(etUnit.getText().toString().isEmpty())
                {
                    _dataValidationPass = false;
                    _errorMessage = "Please add a unit name, then try again";
                }
                else if(etDate.getText().toString().isEmpty())
                {
                    _dataValidationPass = false;
                    _errorMessage = "Please add a date, then try again";
                }
                else if(etLocation.getText().toString().isEmpty())
                {
                    _dataValidationPass = false;
                    _errorMessage = "Please add a location, then try again";
                }
                if(_dataValidationPass != false) {
                    for (HashMap<String, String> entry : data) {
                        if (entry.get(TAG_DESIGNATION_NAME).isEmpty() || entry.get(TAG_DESIGNATION).isEmpty()) {
                            _dataValidationPass = false;
                            _errorMessage = "Please fill in all required fields in designation & designation personnel name, then try again";
                        }
                    }
                }

                Log.i(String.valueOf(_dataValidationPass), "yes");

                if(_dataValidationPass == false)
                {
                    showErrorAlertDialog(view, _errorMessage);
                    return;
                }


                SQLiteDatabase db;
                db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);

                String concatkah = "";
                for(HashMap<String, String> entry : data)
                {
                    String kah;
                    kah = entry.get(TAG_DESIGNATION) + ": " +entry.get(TAG_DESIGNATION_NAME) + "\n";
                    concatkah += kah;
                }

                //form data
                ContentValues content = new ContentValues();

                content.put(TAG_O_NAME, etAddExerciseName.getText().toString());
                content.put(TAG_O_UNIT, etUnit.getText().toString());
                content.put(TAG_O_DATE, etDate.getText().toString());
                content.put(TAG_O_LOC, etLocation.getText().toString());
                content.put(TAG_O_KAH, concatkah);
                content.put(TAG_O_OPS, tv_sw_togglestate.getText().toString());

                db.insert("operation", null, content);
                db.close();

                db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);

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
                Intent i = new Intent(context, ViewOperationActivity.class);
                //push id to next page
                i.putExtra(TAG_O_ID, String.valueOf(insertedID));
                context.startActivity(i);

                dismiss();
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

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch(direction){
                case ItemTouchHelper.LEFT:
                    data.remove(position);
                    rvAdapter.notifyItemRemoved(position);
                    break;
                case ItemTouchHelper.RIGHT:
                    break;
            }
        }
    };


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }

}