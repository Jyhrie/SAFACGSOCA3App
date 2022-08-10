package com.example.safacgsoca3app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewPastDocumentsActivity extends AppCompatActivity {

    private static final String TAG_DOC_ID = "doc_id";
    private static final String TAG_D_NAME = "d_name";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_UNIT = "o_unit";
    private static final String TAG_PAST_DOC = "past_doc";

    ArrayList<HashMap<String,String>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_documents);

        ListView lv_all_docs = (ListView) findViewById(R.id.lv_all_docs);
        lv_all_docs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), GenerateDocumentsActivity.class);
                intent.putExtra(TAG_DOC_ID, ((TextView) view.findViewById(R.id.tv_list_doc_id)).getText().toString());
                intent.putExtra(TAG_PAST_DOC, "1");
                startActivity(intent);
                //start new intent allowing ammo ic to sign, followed by generate document.
                finish();
            }
        });

        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = db.rawQuery("select doc_id, d_name, o_name, o_unit from document where doc_closed = 0", null);

        data = new ArrayList<>();
        while (c1.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put(TAG_DOC_ID, c1.getString(0));
            map.put(TAG_D_NAME, c1.getString(1));
            map.put(TAG_O_NAME, c1.getString(2));
            map.put(TAG_O_UNIT, c1.getString(3));

            data.add(map);
        }
        db.close();
        SimpleAdapter adapter = new SimpleAdapter(
                ViewPastDocumentsActivity.this,
                data, //hashmapdata
                R.layout.list_view_all_documents, //layout of list
                new String[]{TAG_DOC_ID, TAG_O_NAME, TAG_O_UNIT, TAG_D_NAME}, //from array
                new int[]{R.id.tv_list_doc_id,
                        R.id.tv_list_o_name,
                        R.id.tv_list_o_unit,
                        R.id.tv_list_d_name});
        lv_all_docs.setAdapter(adapter);

        Switch sw_check_opened_doc;
        sw_check_opened_doc = findViewById(R.id.switch_check_open_doc);
        sw_check_opened_doc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true) {
                    SQLiteDatabase db;
                    db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                    Cursor c1 = db.rawQuery("select doc_id, d_name, o_name, o_unit from document", null);

                    data = new ArrayList<>();
                    while (c1.moveToNext()) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TAG_DOC_ID, c1.getString(0));
                        map.put(TAG_D_NAME, c1.getString(1));
                        map.put(TAG_O_NAME, c1.getString(2));
                        map.put(TAG_O_UNIT, c1.getString(3));

                        data.add(map);
                    }
                    db.close();

                    SimpleAdapter adapter = new SimpleAdapter(
                            ViewPastDocumentsActivity.this,
                            data, //hashmapdata
                            R.layout.list_view_all_documents, //layout of list
                            new String[]{TAG_DOC_ID, TAG_O_NAME, TAG_O_UNIT, TAG_D_NAME}, //from array
                            new int[]{R.id.tv_list_doc_id,
                                    R.id.tv_list_o_name,
                                    R.id.tv_list_o_unit,
                                    R.id.tv_list_d_name});
                    lv_all_docs.setAdapter(adapter);
                }
                else
                {
                    SQLiteDatabase db;
                    db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
                    Cursor c1 = db.rawQuery("select doc_id, d_name, o_name, o_unit from document where doc_closed = 0", null);

                    data = new ArrayList<>();
                    while (c1.moveToNext()) {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TAG_DOC_ID, c1.getString(0));
                        map.put(TAG_D_NAME, c1.getString(1));
                        map.put(TAG_O_NAME, c1.getString(2));
                        map.put(TAG_O_UNIT, c1.getString(3));

                        data.add(map);
                    }
                    db.close();
                    SimpleAdapter adapter = new SimpleAdapter(
                            ViewPastDocumentsActivity.this,
                            data, //hashmapdata
                            R.layout.list_view_all_documents, //layout of list
                            new String[]{TAG_DOC_ID, TAG_O_NAME, TAG_O_UNIT, TAG_D_NAME}, //from array
                            new int[]{R.id.tv_list_doc_id,
                                    R.id.tv_list_o_name,
                                    R.id.tv_list_o_unit,
                                    R.id.tv_list_d_name});
                    lv_all_docs.setAdapter(adapter);
                }

            }
        });


    }
}