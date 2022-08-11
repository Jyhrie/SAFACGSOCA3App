package com.example.safacgsoca3app;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class fragment_Assign_Detail_Personnel extends DialogFragment implements RecyclerViewInterface{

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
    private static final String TAG_ENABLED = "enabled";

    ArrayList<HashMap<String, String>> data;
    adapter_assign_detail_personnel rvAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_assign_detail_personnel, container, false);
        Context context = getContext();
        ViewOperationActivity source = (ViewOperationActivity) getActivity();
        Bundle args = getArguments();


        String o_id = args.getString(TAG_O_ID);

        SQLiteDatabase db;
        db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);

        Cursor c1 = db.rawQuery("select op.op_id, p.p_rank, p.p_name from operation_personnel op, personnel p where op.d_id is null and p.p_id = op.p_id and op.o_id = " + o_id, null);
        data = new ArrayList<HashMap<String, String>>();

        while (c1.moveToNext()) {
            Log.i("data found", "test");
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);

            map.put(TAG_OP_ID, line_id);
            map.put(TAG_P_NAME, line_rank + " " + line_name);
            map.put(TAG_ENABLED, "0");

            data.add(map);
        }
        db.close();

        RecyclerView rv = v.findViewById(R.id.rv_assign_personnel_to_detail);
        rvAdapter = new adapter_assign_detail_personnel(
                context,
                data,
                this
        );
        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(context));

        SearchView sv_operation_nominal_roll = (SearchView) v.findViewById(R.id.sv_assign_detail_personnel);
        sv_operation_nominal_roll.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });

        Button btn_assign_detail_personnel_submit;
        btn_assign_detail_personnel_submit = v.findViewById(R.id.btn_assign_detail_personnel_submit);
        btn_assign_detail_personnel_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db;
                db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                for(HashMap<String,String> item : data)
                {
                    if(item.get(TAG_ENABLED).equals("1"))
                    {
                        ContentValues content = new ContentValues();
                        content.put(TAG_D_ID, -1);

                        //update detail
                        //assign d_id as -1 as temp value
                        db.update("operation_personnel", content, TAG_OP_ID + " = ?", new String[]{item.get(TAG_OP_ID)});
                    }
                }
                db.close();
                source.refreshFragmentData();
                dismiss();
            }
        });

        return v;
    }

    private void filter(String text)
    {
        ArrayList<HashMap<String,String>> filteredList = new ArrayList<>();
        for(HashMap<String,String> item : data)
        {
            if(item.get(TAG_P_NAME).toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }
        rvAdapter.filterList(filteredList);
    }

    @Override
    public void onItemClick(int position) {
        Log.i("clickinggggg", String.valueOf(position));
        if(rvAdapter.data.get(position).get(TAG_ENABLED).equals("1"))
        {
            rvAdapter.data.get(position).put(TAG_ENABLED, "0");
        }
        else if(rvAdapter.data.get(position).get(TAG_ENABLED).equals("0"))
        {
            rvAdapter.data.get(position).put(TAG_ENABLED, "1");
        }
        rvAdapter.notifyItemChanged(position);
    }

    @Override
    public void onLongItemClick(int position) {

    }
}
