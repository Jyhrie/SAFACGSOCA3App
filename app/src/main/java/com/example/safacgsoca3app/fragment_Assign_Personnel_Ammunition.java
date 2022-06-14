package com.example.safacgsoca3app;

import android.content.ContentValues;
import android.content.Context;
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

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_assign_personnel_ammunition, container, false);
        Context context = getContext();

        Bundle args = getArguments();
        String o_id = args.getString(TAG_O_ID);
        String op_id = args.getString(TAG_OP_ID);

        RecyclerViewInterface recyclerViewInterface = this;
        RecyclerView rv_assign_personnel_ammunition;
        rv_assign_personnel_ammunition = (RecyclerView) v.findViewById(R.id.rv_assign_personnel_ammunition);

        SQLiteDatabase db;

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

                //get data from previous adapter
                //adapter_Personnel_Ammunition adapter = (adapter_Personnel_Ammunition) rv_assign_personnel_ammunition.getAdapter();
                //ArrayList<HashMap<String, String>> existing_data = adapter.getData();

                //initialize new entry for hashmap
                HashMap<String, String> newEntryHash = new HashMap<String, String>();
                newEntryHash.put(TAG_PA_ID, "-1");
                data.add(newEntryHash);
                assign_personnel_adapter.notifyDataSetChanged();

            }
        });

        Button btn_save_assigned_ammunition = (Button) v.findViewById(R.id.btn_save_assigned_ammunition);
        btn_save_assigned_ammunition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (HashMap<String, String> map : data) {
                    Log.i(map.get(TAG_A_ID), map.get(TAG_PA_ISSUE_QTY));

                    //get all data stored within adapter
                    ArrayList<HashMap<String, String>> existing_data = data;
                    for (HashMap<String, String> entry : existing_data) {
                        //check if TAG_PA_ID is new/old
                        if (entry.get(TAG_PA_ID).equals("-1")) {
                            //new entry
                            ContentValues content = new ContentValues();
                            content.put(TAG_OP_ID, String.valueOf(op_id));
                            content.put(TAG_A_ID, entry.get(TAG_A_ID));
                            content.put(TAG_PA_ISSUE_QTY, entry.get(TAG_PA_ISSUE_QTY));

                            SQLiteDatabase db;
                            db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                            db.insert("personnel_ammunition", null, content);
                            db.close();
                        } else {
                            ContentValues content = new ContentValues();
                            content.put(TAG_A_ID, entry.get(TAG_A_ID));
                            content.put(TAG_PA_ISSUE_QTY, entry.get(TAG_PA_ISSUE_QTY));

                            SQLiteDatabase db;
                            db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                            db.update("personnel_ammunition", content, "pa_id = ?", new String[]{entry.get(TAG_PA_ID)});
                            db.close();
                        }


                        //check if existing TAG_PA_IDs have been removed
                        Log.i(entry.get(TAG_A_ID) + "AID", entry.get(TAG_PA_ISSUE_QTY) + "AQTY");
                    }
                }
            }
        });


        return v;
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
                    Log.i(String.valueOf(data.size()), String.valueOf(position));
                    data.remove(position);
                    assign_personnel_adapter.notifyItemRemoved(position);
                    break;
                case ItemTouchHelper.RIGHT:
                    break;
            }
        }
    };

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

    @Override
    public void onItemClick(int position) {

    }
}
