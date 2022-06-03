package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayPersonnelInfoActivity extends AppCompatActivity {

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



        //get data from db;
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Operation(o_id integer PRIMARY KEY NOT NULL, o_kah text NOT NULL)");

        ArrayList<HashMap<String, String>> Personnel_Ammo_List = new ArrayList<HashMap<String, String>>();
        //Defines an array of hashmaps. Each hashmap contains multiple values associated to one key each
        //Personnel_Ammo_List is the array of hashmaps

        for (int i = 0; i < 2; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            String Ammunition_Text = "Ammunition Type";
            String Issued_Quantity = "0";
            String Expended_Quantity = "0";
            String Returned_Quantity = "0";
            String Spoilt_Quantity = "0";

            //In reality, the number of entries is not defined by the for loop but by database entries
            //For this example, we will manually create 2 entries containing a hardcoded string

            map.put(TAG_ID_1, Ammunition_Text);
            map.put(TAG_ID_2, Issued_Quantity);
            map.put(TAG_ID_3, Expended_Quantity);
            map.put(TAG_ID_4, Returned_Quantity);
            map.put(TAG_ID_5, Spoilt_Quantity);
            //Puts key and data into hashmap
            Personnel_Ammo_List.add(map);
            //Adds a hashmap into the array
        }

        ListView lv = findViewById(R.id.lv_Personnel_Ammunition);
        ListAdapter adapter = new SimpleAdapter(
                DisplayPersonnelInfoActivity.this, //context
                Personnel_Ammo_List, //hashmapdata
                R.layout.list_display_personnel_ammunition, //layout of list
                new String[]{TAG_ID_1,TAG_ID_2,TAG_ID_3,TAG_ID_4,TAG_ID_5}, //from array
                new int[]{R.id.Personnel_Ammunition,R.id.Issued_Quantity,R.id.Expended_Quantity,R.id.Returned_Quantity,R.id.Spoilt_Quantity}); //toarray
        // updating listview
        lv.setAdapter(adapter);

    }
}