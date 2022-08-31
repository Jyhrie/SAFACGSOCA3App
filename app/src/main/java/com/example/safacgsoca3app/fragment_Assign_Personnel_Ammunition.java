package com.example.safacgsoca3app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.itextpdf.layout.element.Paragraph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class fragment_Assign_Personnel_Ammunition extends DialogFragment implements RecyclerViewInterface {

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
    private static final String TAG_PID = "p_id";
    private static final String TAG_NAME = "e_name";
    private static final String TAG_PNAME = "p_name";
    private static final String TAG_PRANK = "p_rank";
    private static final String TAG_KAH = "o_kah";
    private static final String TAG_ANAME = "a_name";
    private static final String TAG_OPID = "op_id";

    private adapter_Personnel_Ammunition assign_personnel_adapter;

    private ArrayList<HashMap<String, String>> data;
    private ArrayList<String> removedData;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_assign_personnel_ammunition, container, false);
        Context context = getContext();
        ViewOperationActivity source = (ViewOperationActivity) getActivity();


        removedData = new ArrayList<>();
        Bundle args = getArguments();
        String o_id = args.getString(TAG_O_ID);
        String op_id = args.getString(TAG_OP_ID);
        boolean all = args.getBoolean("WHOLEDETAIL");
        ArrayList<String> op_id_list = args.getStringArrayList("OP_ID_LIST");

        RecyclerViewInterface recyclerViewInterface = this;
        RecyclerView rv_assign_personnel_ammunition;
        rv_assign_personnel_ammunition = (RecyclerView) v.findViewById(R.id.rv_assign_personnel_ammunition);

        SQLiteDatabase db;

        //populate top textview
        TextView tv_assigned_ammo_personnel = v.findViewById(R.id.tv_assigned_ammo_personnel);
        if(all == true)
        {
            tv_assigned_ammo_personnel.setText("ASSIGN TO ENTIRE DETAIL");
        }
        else
        {
            //get personnel_name
            db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
            Cursor c1 = db.rawQuery("select p.p_rank, p.p_name from personnel p, operation_personnel op where op.p_id = p.p_id and op.op_id = ?", new String[]{op_id});
            if(c1.moveToFirst()) {
                tv_assigned_ammo_personnel.setText(c1.getString(0) + " " + c1.getString(1));
            }

        }


        //get ammo list
        ArrayList<HashMap<String,String>> ammo_list = getAmmoList(o_id);

        //get all existing ammo personnel is assigned to
        data = getPersonnelAmmo(op_id);
        assign_personnel_adapter = new adapter_Personnel_Ammunition(context,
                data,
                ammo_list,
                recyclerViewInterface
        );

        rv_assign_personnel_ammunition.setAdapter(assign_personnel_adapter);
        rv_assign_personnel_ammunition.setLayoutManager(new LinearLayoutManager(context));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv_assign_personnel_ammunition);


        Button btn_assign_personnel_ammunition_add_entry;
        btn_assign_personnel_ammunition_add_entry = (Button) v.findViewById(R.id.btn_assign_personnel_ammunition_add_entry);
        btn_assign_personnel_ammunition_add_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initialize new entry for hashmap
                HashMap<String, String> newEntryHash = new HashMap<String, String>();
                newEntryHash.put(TAG_PA_ID, "-1");
                data.add(newEntryHash);
                assign_personnel_adapter.notifyItemInserted(assign_personnel_adapter.getItemCount());

            }
        });

        Button btn_save_assigned_ammunition = (Button) v.findViewById(R.id.btn_save_assigned_ammunition);
        btn_save_assigned_ammunition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //iterate through data arraylist;
                boolean _dataValidationPass = true;
                String _errorMessage = "there is no empty field, something went wrong";

                for (HashMap<String, String> entry : data) {
                    if (entry.containsKey(TAG_PA_ISSUE_QTY)) {
                        if (TAG_PA_ISSUE_QTY.isEmpty()) {
                            _dataValidationPass = false;
                            _errorMessage = "Please input a ammo issue quantity";
                        }
                    } else {
                        _dataValidationPass = false;
                    }

                    if (_dataValidationPass == false) {
                        showErrorAlertDialog(view, _errorMessage);
                        return;
                    }
                }

                //get all data stored within adapter
                ArrayList<HashMap<String, String>> existing_data = data;
                ArrayList databaseInteraction = new ArrayList<>();
                boolean isOverflow = false;
                String message = "error";
                if (all == true) {

                    for(String op_id_from_arr : op_id_list) {
                        for (HashMap<String, String> entry : existing_data) {
                            if(isOverflow == true)
                            {
                                break;
                            }
                            if (entry.get(TAG_PA_ID).equals("-1")) {

                                HashMap databaseEntry = new HashMap<>();
                                //do a check if updated ammo exceeds the allocated ammo
                                SQLiteDatabase db;
                                db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                                Cursor c1 = db.rawQuery("select case when (select sum(a_qty) from ammunition where a_id = ?) > (select sum(pa_issue_qty) from personnel_ammunition where a_id = ?) + ? then 'false' else 'true' end as overflowCheck", new String[]{entry.get(TAG_A_ID), entry.get(TAG_A_ID),String.valueOf(Float.parseFloat(entry.get(TAG_PA_ISSUE_QTY))* op_id_list.size())});
                                if(c1.moveToFirst())
                                {
                                    Cursor c2 = db.rawQuery("select a_name from ammunition where a_id = ?", new String[]{entry.get(TAG_A_ID)});
                                    if(c2.moveToFirst())
                                    {
                                        message = "Quantity of " + c2.getString(0) + " exceeds allocated amount";
                                    }
                                    isOverflow = Boolean.parseBoolean(c1.getString(0));
                                }
                                db.close();
                                //new entry
                                ContentValues content = new ContentValues();
                                content.put(TAG_OP_ID, op_id_from_arr);
                                content.put(TAG_A_ID, entry.get(TAG_A_ID));
                                content.put(TAG_PA_ISSUE_QTY, entry.get(TAG_PA_ISSUE_QTY));

                                databaseEntry.put("isNew", true);
                                databaseEntry.put("content", content);

                                databaseInteraction.add(databaseEntry);
                            }
                        }
                    }
                } else {
                    for (HashMap<String, String> entry : existing_data) {
                        if(isOverflow == true)
                        {
                            break;
                        }
                        //check if TAG_PA_ID is new/old

                        Log.i(entry.get(TAG_PA_ID), "paid");
                        if (entry.get(TAG_PA_ID).equals("-1")) {

                            //do a check if updated ammo exceeds the allocated ammo
                            HashMap databaseEntry = new HashMap<>();
                            //new entry
                            ContentValues content = new ContentValues();
                            content.put(TAG_OP_ID, String.valueOf(op_id));
                            content.put(TAG_A_ID, entry.get(TAG_A_ID));
                            content.put(TAG_PA_ISSUE_QTY, entry.get(TAG_PA_ISSUE_QTY));

                            SQLiteDatabase db;
                            //do a check if updated ammo exceeds the allocated ammo
                            db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                            Cursor c1 = db.rawQuery("select case when (select sum(a_qty) from ammunition where a_id = ?) > (select sum(pa_issue_qty) from personnel_ammunition where a_id = ?) + ? then 'false' else 'true' end as overflowCheck", new String[]{entry.get(TAG_A_ID), entry.get(TAG_A_ID),entry.get(TAG_PA_ISSUE_QTY)});
                            Log.i(entry.get(TAG_A_ID),entry.get(TAG_PA_ISSUE_QTY));
                            if(c1.moveToFirst())
                            {
                                Cursor c2 = db.rawQuery("select a_name from ammunition where a_id = ?", new String[]{entry.get(TAG_A_ID)});
                                if(c2.moveToFirst())
                                {
                                    message = "Quantity of " + c2.getString(0) + " exceeds allocated amount";
                                }
                                isOverflow = Boolean.parseBoolean(c1.getString(0));
                            }
                            db.close();


                            databaseEntry.put("isNew", true);
                            databaseEntry.put("content", content);

                            databaseInteraction.add(databaseEntry);

                            /*db.insert("personnel_ammunition", null, content);*/
                        } else {
                            ContentValues content = new ContentValues();
                            content.put(TAG_A_ID, entry.get(TAG_A_ID));
                            content.put(TAG_PA_ISSUE_QTY, entry.get(TAG_PA_ISSUE_QTY));

                            HashMap databaseEntry = new HashMap<>();

                            SQLiteDatabase db;
                            //do a check if updated ammo exceeds the allocated ammo
                            db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                            Cursor c1 = db.rawQuery("select case when (select sum(a_qty) from ammunition where a_id = ?) > (select sum(pa_issue_qty) from personnel_ammunition where a_id = ? and pa_id != ?) + ? then 'false' else 'true' end as overflowCheck", new String[]{entry.get(TAG_A_ID), entry.get(TAG_A_ID), entry.get(TAG_PA_ID),entry.get(TAG_PA_ISSUE_QTY)});
                            Log.i(entry.get(TAG_A_ID),entry.get(TAG_PA_ISSUE_QTY));
                            if(c1.moveToFirst())
                            {
                                Cursor c2 = db.rawQuery("select a_name from ammunition where a_id = ?", new String[]{entry.get(TAG_A_ID)});
                                if(c2.moveToFirst())
                                {
                                    message = "Quantity of " + c2.getString(0) + " exceeds allocated amount";
                                }
                                isOverflow = Boolean.parseBoolean(c1.getString(0));
                            }
                            db.close();
                            databaseEntry.put("isNew", false);
                            databaseEntry.put("content", content);
                            databaseEntry.put("pa_id", entry.get(TAG_PA_ID));

                            databaseInteraction.add(databaseEntry);
                        }

                        //check if existing TAG_PA_IDs have been removed
                        Log.i(entry.get(TAG_A_ID) + "AID", entry.get(TAG_PA_ISSUE_QTY) + "AQTY");
                    }
                }
                if(isOverflow != true) {
                    SQLiteDatabase db;
                    //do a check if updated ammo exceeds the allocated ammo
                    db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                    for(int i = 0; i < databaseInteraction.size(); i++)
                    {
                        HashMap databaseEntry = (HashMap) databaseInteraction.get(i);
                        ContentValues content = (ContentValues) databaseEntry.get("content");
                        if(!(Boolean) databaseEntry.get("isNew"))
                        {
                            String pa_id = (String) databaseEntry.get("pa_id");
                            db.update("personnel_ammunition", content, "pa_id = ?", new String[]{pa_id});
                        }
                        else
                        {
                            db.insert("personnel_ammunition", null, content);
                        }
                    }
                    //do check if any entry removed
                    for(String removed : removedData)
                    {
                        db.delete("personnel_ammunition", "pa_id = ?", new String[]{removed});
                    }

                    dismiss();
                }
                else
                {
                    showOverflowAlertDialog(message);
                    Log.i("CHECK FAILED", "CHECK FAILED");
                    //prompt user.
                }
            }
        });
        return v;
    }


    private ArrayList<HashMap<String,String>> getAmmoList(String o_id)
    {
        SQLiteDatabase db;
        Context context = getContext();
        db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("select a_id, a_name, a_qty from ammunition where o_id = " + o_id, null);
        ArrayList<HashMap<String, String>> ammo_list = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);
            String line_qty = c1.getString(1);

            map.put(TAG_A_ID, line_id);
            map.put(TAG_A_NAME, line_name);
            map.put(TAG_A_QTY, line_qty);

            ammo_list.add(map);
        }
        db.close();

        return ammo_list;
    }

    private ArrayList<HashMap<String,String>> getPersonnelAmmo(String op_id)
    {
        SQLiteDatabase db;
        Context context = getContext();
        ArrayList<HashMap<String, String>> personnel_ammo_list = new ArrayList<HashMap<String, String>>();
        db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
        Cursor c2 = db.rawQuery("select pa_id, a_id, pa_issue_qty from personnel_ammunition where op_id = " + op_id, null);
        while (c2.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_pa_id = c2.getString(0);
            String line_a_id = c2.getString(1);
            String line_pa_issue_qty = c2.getString(2);

            map.put(TAG_PA_ID, line_pa_id);
            map.put(TAG_A_ID, line_a_id);
            map.put(TAG_PA_ISSUE_QTY, line_pa_issue_qty);

            personnel_ammo_list.add(map);
        }
        return personnel_ammo_list;
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
                    showErrorAlertDialog("Are you sure you want to remove this ammunition", position);
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
        showErrorAlertDialog("Are you sure you want to remove this ammunition", position);
    }

    public void showErrorAlertDialog(String message, int position)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                removedData.add(data.get(position).get(TAG_PA_ID));
                data.remove(position);
                assign_personnel_adapter.notifyItemRemoved(position);
            }});
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                assign_personnel_adapter.notifyItemChanged(position);
                dialogInterface.dismiss();
            }
        });
        alert.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
                assign_personnel_adapter.notifyItemChanged(position);
            }
        });
        alert.show();
    }

    public void showOverflowAlertDialog(String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                dialogInterface.dismiss();
            }});
        alert.show();
    }


}
