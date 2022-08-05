package com.example.safacgsoca3app;

import static java.lang.Integer.parseInt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class fragment_Add_Edit_Detail extends DialogFragment implements RecyclerViewInterface{
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

    private static final String TAG_ID = "o_id";
    private static final String TAG_PID ="p_id";
    private static final String TAG_NAME = "e_name";
    private static final String TAG_PNAME = "p_name";
    private static final String TAG_PRANK = "p_rank";
    private static final String TAG_KAH = "o_kah";
    private static final String TAG_ANAME = "a_name";
    private static final String TAG_OPID = "op_id";

    ArrayList<HashMap<String, String>> op_list;
    ViewOperationActivity source;
    String o_id;
    int d_id;
    boolean reset;
    adapter_Detail_Personnel rvAdapter;
    View v;
    RecyclerViewInterface recyclerViewInterface;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.fragment_add_edit_detail, container, false);
        Context context = getContext();
        recyclerViewInterface = this;

        source = (ViewOperationActivity) getActivity();

        Bundle args = getArguments();

        d_id = args.getInt(TAG_D_ID);
        o_id = args.getString(TAG_O_ID);
        reset = args.getBoolean("reset");

        EditText etDetailName;
        Button btnSaveDetail;
        Button btnSelectPersonnel;

        etDetailName = (EditText) v.findViewById(R.id.etDetailName);
        btnSaveDetail = (Button) v.findViewById(R.id.btnCreateDetail);
        btnSelectPersonnel = (Button) v.findViewById(R.id.btnSelectPersonnel);


        //initialize fields
        SQLiteDatabase db;
        db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT d_name FROM detail WHERE d_id = " + d_id, null);
        while (cursor.moveToNext())
        {
            etDetailName.setText(cursor.getString(0));
        }

        //grab operation_personnel from db
        db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);

        //wipe prev selected stuff
        if(reset == true) {
            db.execSQL("UPDATE operation_personnel SET d_id = NULL WHERE d_id = -1");
        }

        db.close();

        refreshData();


        btnSaveDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean _dataValidationPass = true;
                String _errorMessage = "there is no empty field, something went wrong";
                if(etDetailName.getText().toString().isEmpty())
                {
                    _dataValidationPass = false;
                    _errorMessage = "Please insert a detail name, then try again";
                }

                if(_dataValidationPass == false)
                {
                    showErrorAlertDialog(view, _errorMessage);
                    return;
                }

                SQLiteDatabase db;
                if (d_id != -1) {
                    //update detail name
                    ContentValues content = new ContentValues();
                    content.put(TAG_D_NAME, etDetailName.getText().toString());

                    db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                    db.update("detail", content, "d_id = ?", new String[]{String.valueOf(d_id)});

                    //query all personnel in specific detail
                    ArrayList<String> existing_detail_personnel_id = new ArrayList<String>();
                    Cursor c1 = db.rawQuery("SELECT op_id from operation_personnel where d_id = " + d_id,null);
                    while(c1.moveToNext())
                    {
                        existing_detail_personnel_id.add(c1.getString(0));
                    }

                    //remove difference
                    //compare both lists
                    ArrayList<String> new_detail_personnel_id = new ArrayList<String>();
                    for(int i = 0; i < op_list.size(); i++)
                    {
                        if(op_list.get(i).get(TAG_OP_ID) != String.valueOf(-1));
                        {
                            new_detail_personnel_id.add(op_list.get(i).get(TAG_OP_ID));
                        }
                    }

                    //iterate over both lists
                    for(int i = 0; i< existing_detail_personnel_id.size(); i++)
                    {
                        if(!new_detail_personnel_id.contains(existing_detail_personnel_id.get(i)))
                        {
                            db.execSQL("UPDATE operation_personnel set d_id = null WHERE op_id = " + existing_detail_personnel_id.get(i));
                            db.execSQL("DELETE FROM personnel_ammunition WHERE op_id = " + existing_detail_personnel_id.get(i));
                        }
                    }
                        //remove their ammo as well
                    //add all people with detail -1 to current detail
                    ContentValues content2 = new ContentValues();
                    content2.put(TAG_D_ID, d_id);
                    db.update("operation_personnel", content2, "d_id = ?", new String[]{String.valueOf(-1)});

                } else if (d_id == -1) {

                    //update db to add detail name, id
                    db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);

                    ContentValues content = new ContentValues();

                    content.put(TAG_D_NAME, etDetailName.getText().toString());
                    content.put(TAG_O_ID, String.valueOf(o_id));
                    db.insert("detail", null, content);
                    db.close();

                    //draw new detail_id out of db
                    db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                    int new_detail_id = -1;
                    Cursor cursor = db.rawQuery("SELECT d_id FROM detail ORDER BY d_id DESC LIMIT 1 ", null);
                    while (cursor.moveToNext()) {
                        new_detail_id = cursor.getInt(0);
                    }

                    //update all personnel with d_id -1 to new detail id
                    ContentValues content2 = new ContentValues();
                    content2.put(TAG_D_ID, new_detail_id);
                    db.update("operation_personnel", content2, "d_id = ?", new String[]{String.valueOf(-1)});
                    db.close();


                }
                source.refreshPageAfterEditDetail(o_id, d_id);
                dismiss();
            }

        });

        Button btn_issue_to_all = (Button) v.findViewById(R.id.btn_issue_to_all_in_detail);
        btn_issue_to_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> arrList = new ArrayList<>();
                for(HashMap<String,String> entry : op_list)
                {
                    arrList.add(entry.get(TAG_OP_ID));
                }
                source.showAssignPersonnelAmmo(null, parseInt(o_id), arrList, true);
            }
        });

        btnSelectPersonnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                Cursor c1 = db.rawQuery("select op.op_id, p.p_rank, p.p_name from operation_personnel op, personnel p where op.d_id is null and p.p_id = op.p_id and op.o_id = " + o_id, null);
                ArrayList<HashMap<String, String>> personnelList = new ArrayList<HashMap<String, String>>();
                if(!c1.moveToFirst())
                {
                    showErrorAlertDialogNoPersonnel("No personnel found in operation");
                }
                else {
                    source.showSelectPersonnelDialog(o_id, d_id);
                }
            }
        });

        return v;
    }


    public void refreshData()
    {
        Context context = getContext();
        get_op_list();
        RecyclerView rv = v.findViewById(R.id.rv_edit_issuing_detail_personnel);
        rvAdapter = new adapter_Detail_Personnel(
                context,
                op_list,
                recyclerViewInterface
        );
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(context));

    }

    public void get_op_list()
    {
        Context context = getContext();
        //op_list = new ArrayList<HashMap<String, String>>();
        SQLiteDatabase db;
        db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("select op.op_id, p.p_name, p.p_nric from operation_personnel op, personnel p where op.p_id = p.p_id and (op.d_id = " + d_id + " or op.d_id = -1)", null);
        op_list = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);
            String line_nric = c1.getString(2);

            map.put(TAG_OP_ID, line_id);
            map.put(TAG_P_NAME, line_name);
            map.put(TAG_P_NRIC, line_nric);

            op_list.add(map);
        }
        db.close();
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

    @Override
    public void onItemClick(int position) {
        Log.i(String.valueOf(position), "pos");
        Log.i(op_list.get(position).get(TAG_OP_ID), o_id);
        source.showAssignPersonnelAmmo(op_list.get(position).get(TAG_OP_ID), parseInt(o_id), null, false);
    }

    @Override
    public void onLongItemClick(int position) {
        showErrorAlertDialog("Are you sure you want to remove this person", position);
    }

    public void showErrorAlertDialog(String message, int position)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                op_list.remove(position);
                rvAdapter.notifyItemRemoved(position);
            }});
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alert.show();
    }

    public void showErrorAlertDialogNoPersonnel(String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
                dismiss();
            }});
        alert.show();
    }



}