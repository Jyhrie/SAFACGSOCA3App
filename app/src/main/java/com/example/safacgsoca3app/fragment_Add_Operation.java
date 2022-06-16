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
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;


public class fragment_Add_Operation extends DialogFragment implements RecyclerViewInterface {

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
        EditText etAddExerciseKAH;
        EditText etDate;
        EditText etLocation;

        btnInsertExercise = (Button) v.findViewById(R.id.btnInsertExercise);
        etAddExerciseName = (EditText) v.findViewById(R.id.etAddExerciseName);
        etDate = (EditText) v.findViewById(R.id.etDate);
        etLocation = (EditText) v.findViewById(R.id.etLocation);

        data = new ArrayList<HashMap<String,String>>();
        data.add(new HashMap<String,String>());

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
                data.add(new HashMap<String,String>());
                rvAdapter.notifyDataSetChanged();
            }
        });

        btnInsertExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteDatabase db;
                db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);

                String locdate;
                String concatkah = "";
                String finalData = null;
                for(HashMap<String, String> entry : data)
                {
                    String kah;
                    kah = entry.get(TAG_DESIGNATION) + ": " +entry.get(TAG_DESIGNATION_NAME) + "\n";
                    concatkah += kah;
                }

                locdate = "DATE: "+etDate.getText().toString() + "\n" + "LOCATION: " + etLocation.getText().toString() + "\n";
                finalData = locdate + concatkah;
                //form data


                ContentValues content = new ContentValues();

                content.put(TAG_O_NAME, etAddExerciseName.getText().toString());
                content.put(TAG_O_KAH, finalData);

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
                }
    });
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
}