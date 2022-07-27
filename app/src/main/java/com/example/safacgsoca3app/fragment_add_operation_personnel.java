package com.example.safacgsoca3app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.VolumeShaper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class fragment_add_operation_personnel extends DialogFragment implements RecyclerViewInterface {

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
    private static final String TAG_CHECKED = "tag_checked";


    ArrayList<HashMap<String,String>> data;
    adapter_add_operation_personnel rvAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_add_operation_personnel, container, false);
        Context context = getContext();
        OperationNominalRollActivity source = (OperationNominalRollActivity) getActivity();
        Bundle args = getArguments();

        String o_id = args.getString(TAG_O_ID);


        RecyclerViewInterface recyclerViewInterface = this;
        data = new ArrayList<HashMap<String,String>>();

        SQLiteDatabase db;
        db = context.openOrCreateDatabase("A3App.db", context.MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT p.p_id, p.p_rank, p.p_name, p.p_nric FROM personnel p WHERE p.p_id NOT IN (SELECT p.p_id FROM personnel p, operation_personnel op WHERE op.p_id = p.p_id AND op.o_id = "+ o_id+")", null);
        while (c1.moveToNext()) {

            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_nric = c1.getString(3);

            map.put(TAG_P_ID, line_id);
            map.put(TAG_P_NAME, line_rank + " " + line_name);
            map.put(TAG_P_NRIC, line_nric);
            map.put(TAG_CHECKED, String.valueOf(false));

            data.add(map);
            //Adds a hashmap into the array
        }

        SearchView sv_operation_nominal_roll = (SearchView) v.findViewById(R.id.sv_add_operation_personnel);
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


        RecyclerView rv;
        rv = v.findViewById(R.id.rv_add_operation_personnel);
        rvAdapter = new adapter_add_operation_personnel(
                context,
                data,
                recyclerViewInterface
        );

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(context));



        Button btn_add_operation_personnel_submit;
        btn_add_operation_personnel_submit = v.findViewById(R.id.btn_add_operation_personnel_submit);
        btn_add_operation_personnel_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db;
                db = context.openOrCreateDatabase("A3App.db", context.MODE_PRIVATE, null);
                for(HashMap<String,String> entry: data)
                {
                    if(entry.get(TAG_CHECKED) == String.valueOf(true))
                    {
                        ContentValues content = new ContentValues();
                        content.put(TAG_P_ID, entry.get(TAG_P_ID));
                        content.put(TAG_O_ID, o_id);
                        db.insert("operation_personnel", null, content);
                    }
                }
                db.close();
                source.refreshData(o_id);
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
        Log.i("onitemclick", data.get(position).get(TAG_CHECKED));
        data.get(position).put(TAG_CHECKED, !Boolean.parseBoolean(data.get(position).get(TAG_CHECKED)) ? String.valueOf(true): String.valueOf(false) );
        rvAdapter.notifyItemChanged(position);
    }

    @Override
    public void onLongItemClick(int position) {

    }
}
