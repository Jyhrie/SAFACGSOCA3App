package com.example.safacgsoca3app;

import static java.lang.Integer.parseInt;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
import java.util.HashMap;

public class fragment_Add_Edit_Detail extends DialogFragment {
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

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_add_edit_detail, container, false);
        Context context = getContext();
        ViewOperationActivity source = (ViewOperationActivity) getActivity();

        Bundle args = getArguments();

        int d_id = args.getInt(TAG_D_ID);
        String o_id = args.getString(TAG_O_ID);
        boolean reset = args.getBoolean("reset");

        EditText etDetailName;
        Button btnSaveDetail;
        Button btnSelectPersonnel;

        etDetailName = (EditText) v.findViewById(R.id.etDetailName);
        btnSaveDetail = (Button) v.findViewById(R.id.btnCreateDetail);
        btnSelectPersonnel = (Button) v.findViewById(R.id.btnSelectPersonnel);

        String DetailName = String.valueOf(etDetailName);

        ListView lv_add_issuing_detail_personnel;
        lv_add_issuing_detail_personnel = (ListView) v.findViewById(R.id.lv_edit_issuing_detail_personnel);


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

        Cursor c1 = db.rawQuery("select op.op_id, p.p_name from operation_personnel op, personnel p where op.p_id = p.p_id and (op.d_id = " + d_id + " or op.d_id = -1)", null);
        ArrayList<HashMap<String, String>> op_list = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_name = c1.getString(1);

            map.put(TAG_OP_ID, line_id);
            map.put(TAG_P_NAME, line_name);

            op_list.add(map);
        }
        db.close();


        ListView lv = v.findViewById(R.id.lv_edit_issuing_detail_personnel);
        ListAdapter adapter = new SimpleAdapter(
                context, //context
                op_list, //hashmapdata
                R.layout.list_view_detail_personnel, //layout of list
                new String[] {TAG_OP_ID, TAG_P_NAME}, //from array
                new int[] {R.id.tv_edit_detail_personnel_id, R.id.tv_edit_detail_personnel_name}); //toarray
        // updating listview
        lv.setAdapter(adapter);


        //bring up assign ammo dialog
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                source.showAssignPersonnelAmmo(((TextView) view.findViewById(R.id.tv_edit_detail_personnel_id)).getText().toString(), parseInt(o_id));
            }
        });


        btnSaveDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (d_id != -1) {
                    //update detail name
                    ContentValues content = new ContentValues();
                    content.put(TAG_D_NAME, etDetailName.getText().toString());

                    SQLiteDatabase db;
                    db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);
                    db.update("detail", content, "d_id = ?", new String[]{String.valueOf(d_id)});

                    //add all people with detail -1 to current detail
                    ContentValues content2 = new ContentValues();
                    content2.put(TAG_D_ID, d_id);
                    db.update("operation_personnel", content2, "d_id = ?", new String[]{String.valueOf(-1)});

                    /*
                    //add personnel into detail/remove from detail
                        //query into database check all op_ids currently in detail
                    Cursor c1 = db.rawQuery("SELECT op_id FROM operation_personnel WHERE d_id = " + d_id, null);
                    while(c1.moveToNext())
                    {

                    }
                        //compare current list of op_ids in listview to op_ids in detail
                        //delete from db all op_ids that do not exist in listview
                        //remove all ammo assigned to that personnel
                            //delete from personnel ammunition where op_id = deleted op_id
                        //add all from listview to op_id*/

                } else if (d_id == -1) {

                    //update db to add detail name, id
                    SQLiteDatabase db;
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
            }

        });

        btnSelectPersonnel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                source.showSelectPersonnelDialog(o_id, d_id);
            }
        });



        return v;
    }


}