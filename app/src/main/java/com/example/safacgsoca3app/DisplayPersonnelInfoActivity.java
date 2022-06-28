package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayPersonnelInfoActivity extends AppCompatActivity {

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

    private static final String TAG_ID = "o_id";
    private static final String TAG_PID ="p_id";
    private static final String TAG_NAME = "e_name";
    private static final String TAG_PNAME = "p_name";
    private static final String TAG_PRANK = "p_rank";
    private static final String TAG_KAH = "o_kah";
    private static final String TAG_ANAME = "a_name";
    private static final String TAG_OPID = "op_id";


    public static final String TAG_ID_1 = "a_id";
    public static final String TAG_ID_2 = "b_id";
    public static final String TAG_ID_3 = "c_id";
    public static final String TAG_ID_4 = "d_id";
    public static final String TAG_ID_5 = "e_id";

    //Defines a key for the data. TAG_ID variable is for ease of changing the key
    //In hashmap, key is column name, index is row
    //Rest of app references key as TAG_ID. Key is currently "a_id"
    //To change the key, change "a_id"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_personnel_info);


        Intent intent = getIntent();
        String op_id = intent.getStringExtra("op_id");

        TextView Display_Personnel_Info_Ops_Name;
        TextView Display_Personnel_Info_Detail_Name;
        TextView Display_Personnel_Info_Personnel_Name;
        TextView Display_Personnel_Info_Remarks;
        TextView Display_Personnel_Info_Issue_Remark;
        TextView Display_Personnel_Info_Return_Remark;


        Display_Personnel_Info_Ops_Name = (TextView) findViewById(R.id.tv_Display_Personnel_Info_Ops_Name);
        Display_Personnel_Info_Detail_Name = (TextView) findViewById(R.id.tv_Display_Personnel_Info_Detail_Name);
        Display_Personnel_Info_Personnel_Name = (TextView) findViewById(R.id.tv_Display_Personnel_Info_Personnel_Name);
        Display_Personnel_Info_Remarks = (TextView) findViewById(R.id.tv_Display_Personnel_Info_Remarks);
        Display_Personnel_Info_Issue_Remark = (TextView) findViewById(R.id.tv_Display_Personnel_Info_Issue_Remark);
        Display_Personnel_Info_Return_Remark = (TextView) findViewById(R.id.tv_Display_Personnel_Info_Return_Remark);


        String Ops_Name = "Ops Name";
        String Detail_Name = "Detail Name";
        String Personnel_Name = "Personnel Name";
        String Personnel_Remarks = "Remarks";
        String Issue_Remark = "Issue Remarks";
        String Return_Remark = "Return Remarks";


        Display_Personnel_Info_Ops_Name.setText(Ops_Name);
        Display_Personnel_Info_Detail_Name.setText(Detail_Name);
        Display_Personnel_Info_Personnel_Name.setText(Personnel_Name);
        Display_Personnel_Info_Remarks.setText(Personnel_Remarks);
        Display_Personnel_Info_Issue_Remark.setText(Issue_Remark);
        Display_Personnel_Info_Return_Remark.setText(Return_Remark);





        ArrayList<HashMap<String, String>> Personnel_Ammo_List = new ArrayList<HashMap<String, String>>();
        //Defines an array of hashmaps. Each hashmap contains multiple values associated to one key each
        //Personnel_Ammo_List is the array of hashmaps

        //get data from db
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("SELECT a.a_name, pa.pa_id, pa.pa_issued, pa.pa_expended, pa.pa_returned, pa.pa_spoiled FROM personnel_ammunition pa, ammunition a WHERE a.a_id = pa.a_id and pa.op_id = "+op_id, null);

        while(c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<String, String>();

            String Ammunition_Text = c1.getString(0);
            String pa_id = c1.getString(1);
            String Issued_Quantity = c1.getString(2);
            String Expended_Quantity = c1.getString(3);
            String Returned_Quantity = c1.getString(4);
            String Spoiled_Quantity = c1.getString(5);

            //In reality, the number of entries is not defined by the for loop but by database entries
            //For this example, we will manually create 2 entries containing a hardcoded string

            map.put(TAG_A_NAME, Ammunition_Text);
            map.put(TAG_PA_ID, pa_id);
            map.put(TAG_PA_ISSUED, Issued_Quantity);
            map.put(TAG_PA_EXPENEDED, Expended_Quantity);
            map.put(TAG_PA_RETURNED, Returned_Quantity);
            map.put(TAG_PA_SPOILED, Spoiled_Quantity);
            //Puts key and data into hashmap
            Personnel_Ammo_List.add(map);
            //Adds a hashmap into the array
        }

        db.execSQL("CREATE TABLE IF NOT EXISTS detail (d_id integer NOT NULL PRIMARY KEY AUTOINCREMENT, d_name text NOT NULL, o_id integer NOT NULL)");
        c1 = db.rawQuery("select d.d_name, p.p_name, p.p_nric from detail d, personnel p, operation_personnel op where d.d_id = op.d_id and p.p_id = op.p_id and op.op_id = "+op_id, null);
        while (c1.moveToNext()) {
            Display_Personnel_Info_Detail_Name.setText(c1.getString(0));
            Display_Personnel_Info_Personnel_Name.setText(c1.getString(1));
            Display_Personnel_Info_Remarks.setText(c1.getString(2));
        }

        c1 = db.rawQuery("select o_name from operation o, operation_personnel op where o.o_id = op.o_id and op.op_id = "+op_id, null);
        while (c1.moveToNext()) {
            Display_Personnel_Info_Ops_Name.setText(c1.getString(0));
        }

        db.close();

        ListView lv = findViewById(R.id.lv_Personnel_Ammunition);
        ListAdapter adapter = new SimpleAdapter(
                DisplayPersonnelInfoActivity.this, //context
                Personnel_Ammo_List, //hashmapdata
                R.layout.list_display_personnel_ammunition, //layout of list
                new String[]{TAG_A_NAME,TAG_PA_ID,TAG_PA_ISSUED,TAG_PA_EXPENEDED,TAG_PA_RETURNED,TAG_PA_SPOILED}, //from array
                new int[]{R.id.Personnel_Ammunition,R.id.pa_id,R.id.Issued_Quantity,R.id.Expended_Quantity,R.id.Returned_Quantity,R.id.Spoiled_Quantity}); //toarray
        // updating listview
        lv.setAdapter(adapter);
    }
}