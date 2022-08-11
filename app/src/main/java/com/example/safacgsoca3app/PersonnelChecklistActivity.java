package com.example.safacgsoca3app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class PersonnelChecklistActivity extends AppCompatActivity implements RecyclerViewInterface {

    public static final String TAG_ID = "op_id";
    public static final String TAG_RANK = "p_rank";
    public static final String TAG_NAME = "p_name";
    public static final String TAG_REMARKS = "p_remarks";
    private static final String TAG_ENABLED = "enabled";
    private static final String TAG_OP_ID = "op_id";
    int initialisationvalue = 0;

    ArrayList<HashMap<String, String>> data;
    adapter_personnel_checklist rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel_checklist);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String o_id = intent.getStringExtra("o_id");
        String d_id = intent.getStringExtra("d_id");
        String d_name = intent.getStringExtra("d_name");

        TextView tv_D_ID;
        TextView tv_Personnel_Checklist_Detail_Name;

        tv_D_ID = findViewById(R.id.tv_D_ID);
        tv_Personnel_Checklist_Detail_Name = findViewById(R.id.tv_Personnel_Checklist_Detail_Name);

        tv_D_ID.setText(d_id);
        tv_Personnel_Checklist_Detail_Name.setText(d_name);

        data = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> PersonnelSelectionMap = new HashMap<String, String>();

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS personnel (p_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, p_rank varchar(255) NOT NULL, p_name varchar(255) NOT NULL, p_nric text)");
        Cursor c1 = db.rawQuery("select op.op_id, p.p_rank, p.p_name, p.p_nric from personnel p, operation_personnel op where op.p_id = p.p_id and op.o_id = " + o_id + " and op.d_id = " + d_id, null);


        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String line_id = c1.getString(0);
            String line_rank = c1.getString(1);
            String line_name = c1.getString(2);
            String line_remarks = c1.getString(3);

            map.put(TAG_ID, line_id);
            map.put(TAG_RANK, line_rank);
            map.put(TAG_NAME, line_name);
            map.put(TAG_REMARKS, line_remarks);
            map.put(TAG_ENABLED, "0");

            data.add(map);
        }
        db.close();

        RecyclerView rv = findViewById(R.id.rv_Personnel_Checklist);
        rvAdapter = new adapter_personnel_checklist(
                this,
                data,
                this
        );

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        SearchView sv_operation_nominal_roll = (SearchView) findViewById(R.id.sv_personnel_checklist);
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

        Button btnProceed = (Button) findViewById(R.id.btn_Proceed_Declare_Issue_Expend);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ArrayList<HashMap<String, String>> PersonnelListAppended = new ArrayList<HashMap<String, String>>();
                for(HashMap<String,String> line : data)
                {
                    if(line.get(TAG_ENABLED).equals("1"))
                    {
                        HashMap<String,String> map = new HashMap<String,String>();
                        map.put(TAG_OP_ID, line.get(TAG_ID));
                        PersonnelListAppended.add(map);
                    }
                }
                if(PersonnelListAppended.size()==0)
                {
                    showErrorAlertDialog(v, "Please select at least 1 personnel");
                }
                else {
                    Intent intent = new Intent(PersonnelChecklistActivity.this, DeclareIssueReturnReceiveInfoActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("o_id", o_id);
                    intent.putExtra("d_id", d_id);
                    intent.putExtra("PersonnelList", PersonnelListAppended);
                    PersonnelChecklistActivity.this.startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void filter(String text)
    {
        ArrayList<HashMap<String,String>> filteredList = new ArrayList<>();
        for(HashMap<String,String> item : data)
        {
            if(item.get(TAG_NAME).toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }
        rvAdapter.filterList(filteredList);
    }

    public void showErrorAlertDialog(View v, String message)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
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
