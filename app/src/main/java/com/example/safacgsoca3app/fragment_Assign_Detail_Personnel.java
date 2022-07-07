package com.example.safacgsoca3app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class fragment_Assign_Detail_Personnel extends DialogFragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_assign_detail_personnel, container, false);
        Context context = getContext();
        ViewOperationActivity source = (ViewOperationActivity) getActivity();

        Bundle args = getArguments();


        int d_id = args.getInt(TAG_D_ID);
        String o_id = args.getString(TAG_O_ID);
        boolean reset = args.getBoolean("reset");

        SQLiteDatabase db;
        db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);

        Cursor c1 = db.rawQuery("select op.op_id, p.p_rank, p.p_name from operation_personnel op, personnel p where op.d_id is null and p.p_id = op.p_id and op.o_id = " + o_id, null);
        ArrayList<HashMap<String, String>> personnelList = new ArrayList<HashMap<String, String>>();
        while (c1.moveToNext()) {
            Log.i("data found", "test");
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);

            map.put(TAG_OP_ID, line_id);
            map.put(TAG_P_NAME, line_rank + " " + line_name);

            personnelList.add(map);
        }
        db.close();

        ListView lv = v.findViewById(R.id.lv_assign_personnel_to_detail);
        ListAdapter adapter = new SimpleAdapter(
                context, //context
                personnelList, //hashmapdata
                R.layout.list_assign_detail_personnel, //layout of list
                new String[]{TAG_OP_ID, TAG_P_NAME}, //from array
                new int[]{R.id.tv_assign_detail_personnel_id, R.id.tv_assign_detail_personnel_name}); //toarray
        // updating listview
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvListPersonnelId = (TextView) view.findViewById(R.id.tv_assign_detail_personnel_id);
                //toggle and add id to array;
                CardView layout = (CardView) view.findViewById(R.id.cv_assign_detail_list);
                CheckBox cb = view.findViewById(R.id.cb_assign_detail_personnel_bool);
                cb.setChecked(!cb.isChecked());
                if (cb.isChecked()) {
                    layout.setCardBackgroundColor(layout.getContext().getResources().getColor(R.color.personnel_checklist_enabled));
                } else {
                    layout.setCardBackgroundColor(layout.getContext().getResources().getColor(R.color.personnel_checklist_disabled));
                }

            }
        });

        Button btn_assign_detail_personnel_submit;
        btn_assign_detail_personnel_submit = v.findViewById(R.id.btn_assign_detail_personnel_submit);
        btn_assign_detail_personnel_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db;
                db = context.openOrCreateDatabase("A3App.db", Context.MODE_PRIVATE, null);

                for (int i = 0; i < lv.getCount(); i++) {
                    View v = lv.getChildAt(i);
                    if (((CheckBox) v.findViewById(R.id.cb_assign_detail_personnel_bool)).isChecked()) {
                        String op_id = ((TextView) v.findViewById(R.id.tv_assign_detail_personnel_id)).getText().toString();

                        ContentValues content = new ContentValues();
                        content.put(TAG_D_ID, -1);

                        //update detail
                        //assign d_id as -1 as temp value
                        db.update("operation_personnel", content, TAG_OP_ID + " = ?", new String[]{op_id});
                    }
                }
                db.close();

                //update prev detail dialog
                source.refreshFragmentData();
                //source.showAddEditDetailDialog(d_id, o_id, false);
                dismiss();
            }
        });

        return v;
    }
}
