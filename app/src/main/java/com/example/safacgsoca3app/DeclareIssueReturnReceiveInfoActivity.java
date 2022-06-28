package com.example.safacgsoca3app;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class DeclareIssueReturnReceiveInfoActivity extends AppCompatActivity implements RecyclerViewInterface {

    private static final String TAG_DYNAMIC_ISSUE = "dynamic_issue";

    private static final String TAG_TD_ID = "td_id";
    private static final String TAG_DOC_NUMBER = "doc_id";
    private static final String TAG_TD_O_NAME = "td_o_name";
    private static final String TAG_TD_O_KAH = "td_o_kah";
    private static final String TAG_TD_O_UNIT = "td_o_unit";
    private static final String TAG_TD_D_NAME = "td_d_name";
    private static final String TAG_TD_AMMO_NAME = "td_a_name";
    private static final String TAG_TD_PERSONNEL_NAME = "td_p_name";
    private static final String TAG_TD_ISSUED = "td_issued";
    private static final String TAG_TD_RETURNED = "td_returned";
    private static final String TAG_TD_EXPENDED = "td_expended";
    private static final String TAG_TD_SPOILED = "td_spoiled";
    private static final String TAG_TD_ISSUEDATETIME = "td_issuedatetime";
    private static final String TAG_TD_ISSUESIGNATURE = "td_issuesignature";
    private static final String TAG_TD_RETURNDATETIME = "td_returndatetime";
    private static final String TAG_TD_RETURNSIGNATURE = "td_returnsignature";
    private static final String TAG_TD_EXPORTED = "td_exported";


    private static final String TAG_P_ID = "p_id";
    private static final String TAG_P_NAME = "p_name";
    private static final String TAG_P_NRIC = "p_nric";

    private static final String TAG_O_ID = "o_id";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_KAH = "o_kah";
    private static final String TAG_O_UNIT = "o_unit";

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

    private static final String TAG_NEW_ENTRY = "new_entry";

    ArrayList<HashMap<String, String>> rvData = new ArrayList<HashMap<String, String>>();
    adapter_Issue_Ammunition rvAdapter;
    ArrayList<HashMap<String, String>> PersonnelList;
    String type;
    String o_id;
    String d_id;
    String accessed_doc_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declare_issue_return_receive_info);
        accessed_doc_number = "-1";
        Intent intent = getIntent();

        //initalization
        type = intent.getStringExtra("type");
        Log.i(type, "type");
        o_id = intent.getStringExtra("o_id");
        d_id = intent.getStringExtra("d_id");
        PersonnelList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("PersonnelList");

        TextView tv_Issue_Return_Receive;
        Button btn_ClearPad;
        Button btn_Validate;
        SignaturePad Signature_Pad;
        ImageView imageView;

        tv_Issue_Return_Receive = (TextView) findViewById(R.id.tv_Issue_Return_Receive);
        btn_ClearPad = (Button) findViewById(R.id.btn_ClearPad);
        btn_Validate = (Button) findViewById(R.id.btn_Validate);
        Signature_Pad = findViewById(R.id.Signature_Pad);
        imageView = findViewById(R.id.imageView);

        //init end


        if (Integer.valueOf(type) == 1)//ammo unissued
        {
            tv_Issue_Return_Receive.setText("ISSUING");
        } else if (Integer.valueOf(type) == 2) {
            tv_Issue_Return_Receive.setText("RETURN");
        } else if (Integer.valueOf(type) == 3) {
            tv_Issue_Return_Receive.setText("RECIEVE");
        }
        populateNextGuy();
    }

    public void populateNextGuy()
    {
        String current_op_id = PersonnelList.remove(0).get(TAG_OP_ID);

        TextView tv_Issue_Return_Receive;
        TextView tv_personnel_name;
        TextView tv_detail_name;
        Button btn_ClearPad;
        Button btn_Validate;
        SignaturePad Signature_Pad;
        ImageView imageView;

        tv_Issue_Return_Receive = (TextView) findViewById(R.id.tv_Issue_Return_Receive);
        tv_personnel_name = (TextView) findViewById(R.id.tv_Personnel_Name);
        tv_detail_name = (TextView) findViewById(R.id.tv_Detail_Name);
        btn_ClearPad = (Button) findViewById(R.id.btn_ClearPad);
        btn_Validate = (Button) findViewById(R.id.btn_Validate);
        Signature_Pad = findViewById(R.id.Signature_Pad);
        imageView = findViewById(R.id.imageView);

        //clear signature pad
        Signature_Pad.clear();



        //query for all pa_id entries with op_id
        SQLiteDatabase db;
        db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);
        Cursor c1 = null;
        //get & set personnel name
        String personnel_name = null;
        String personnel_rank = null;
        String detail_name = null;
        c1 = db.rawQuery("select p.p_rank, p.p_name, d.d_name from personnel p, operation_personnel op, detail d where op.d_id = d.d_id and op.p_id = p.p_id and op.op_id = ?", new String[]{current_op_id});
        if(c1.moveToFirst())
        {
            personnel_rank = c1.getString(0);
            personnel_name = c1.getString(1);
            detail_name = c1.getString(2);
            tv_personnel_name.setText(personnel_rank+" "+personnel_name);
            tv_detail_name.setText(detail_name);
        }
        //add into arraylist of new or opened
        //get data based on type

        if(Integer.valueOf(type) == 1)
        {
            //query for pa_id, new entry, if td_id is existing
            //select pa.pa_id, 1, -1 from personnel_ammunition pa left join transaction_data on pa.pa_id = td.pa_id where pa.op_id = ? and td_exported = 0
            c1 = db.rawQuery("select pa.pa_id, 1 createnew, -1 td_id from personnel_ammunition pa where pa.op_id = ? and pa.pa_id not in (select pa_id from transaction_data where td_exported = 0)", new String[]{current_op_id});
        }
        else if(Integer.valueOf(type) == 2)
        {
            c1 = db.rawQuery("select td.pa_id, 0 createnew, td.td_id from transaction_data td, personnel_ammunition pa where td.pa_id = pa.pa_id and pa.op_id = ? and td.td_exported = 0", new String[]{current_op_id});
        }

        //c1 = db.rawQuery("select pa.pa_id, iif(td.td_exported is null, 1, td.td_exported) new_entry, iif(td.td_id is null, -1, td.td_id) td_id  from personnel_ammunition pa left join transaction_data td on pa.pa_id = td.pa_id where pa.op_id = " + current_op_id, null);
        ArrayList<HashMap<String, String>> all_pa_id = new ArrayList<>();
        while (c1.moveToNext()) {
            HashMap<String, String> line_map = new HashMap<String, String>();
            line_map.put(TAG_PA_ID, c1.getString(0));
            line_map.put(TAG_NEW_ENTRY, c1.getString(1));
            line_map.put(TAG_TD_ID, c1.getString(2));
            all_pa_id.add(line_map);
        }


        if(all_pa_id.size() == 0)
        {
            //display pop up dialog with person has no ammunition to left to draw
            Dialog alertDialog = new Dialog(DeclareIssueReturnReceiveInfoActivity.this, android.R.style.Theme_Black_NoTitleBar);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
            alertDialog.setContentView(R.layout.dialog_warning);
            alertDialog.setCancelable(true);
            alertDialog.show();

            Button warning_proceed = (Button) alertDialog.findViewById(R.id.btn_warning_proceed);
            Button warning_abort = (Button) alertDialog.findViewById(R.id.btn_warning_back);
            TextView warning_message = (TextView) alertDialog.findViewById(R.id.tv_warning_message);

            warning_proceed.setText("Proceed");
            warning_abort.setText("Abort");
            warning_message.setText(personnel_rank + " " + personnel_name + " does not have ammunition to draw");

            warning_abort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            warning_proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    if(PersonnelList.size() > 0)
                    {
                        populateNextGuy();
                    }
                    else{
                        onPersonnelListEnd(Integer.valueOf(type), accessed_doc_number);
                    }

                }
            });
        }

        //form rvData
        rvData = new ArrayList<>();
        for (HashMap<String, String> entries : all_pa_id) {

            HashMap<String, String> map = new HashMap<String, String>();
            int current_pa_id = Integer.valueOf(entries.get(TAG_PA_ID));
            int current_is_new_entry = Integer.valueOf(entries.get(TAG_NEW_ENTRY));
            String current_td_id = entries.get(TAG_TD_ID);

            //TAG_DYNAMIC_ISSUE = true if new entry, false if existing entry.
            //TAG_PA_ID = taken from above query.
            //TAG_A_NAME = raw text taken from transaction details if old entry, referenced from ammunition table if old entry.
            //TAG_PA_ISSUE_QTY = text taken from personnel_ammunition if entry is new, else take issued value from transaction detail.
            //TAG_TD_RETURNED = text taken only from transaction detail. automatically set this value to issued quantity in event of ops.
            //TAG_TD_EXPENDED = text taken only from transaction detail. automatically set this value to issued quantity in event of range.
            //TAG_TD_SPOILED = text taken only from transaction detail.

            if (current_is_new_entry != 0) {
                map.put(TAG_DYNAMIC_ISSUE, "1");
                map.put(TAG_PA_ID, String.valueOf(current_pa_id));
                map.put(TAG_TD_ID, current_td_id);
                c1 = db.rawQuery("select a.a_name, pa.pa_issue_qty from personnel_ammunition pa, ammunition a where a.a_id = pa.a_id and pa.pa_id = " + current_pa_id, null);
                if (c1.moveToFirst()) {
                    map.put(TAG_A_NAME, c1.getString(0));
                    map.put(TAG_PA_ISSUE_QTY, c1.getString(1));
                }
            } else {
                map.put(TAG_DYNAMIC_ISSUE, "0");
                map.put(TAG_TD_ID, current_td_id);
                map.put(TAG_PA_ID, String.valueOf(current_pa_id));

                //query for the rest of the data in transaction_details
                c1 = db.rawQuery("select td_a_name, td_issued from transaction_data where td_id = " + current_td_id, null);
                if (c1.moveToFirst()) {
                    map.put(TAG_A_NAME, c1.getString(0));
                    map.put(TAG_PA_ISSUE_QTY, c1.getString(1));
                }
            }
            rvData.add(map);
        }


        Log.i(rvData.toString(), String.valueOf(rvData.size()));
        RecyclerView rv;
        rv = findViewById(R.id.rv_Issue_Ammunition);

        rvAdapter = new adapter_Issue_Ammunition(
                this,
                rvData,
                this
        );

        rv.setAdapter(rvAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);

        btn_ClearPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signature_Pad.clear();
            }
        });


        btn_Validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = Signature_Pad.getSignatureBitmap();
                imageView.setImageBitmap(bitmap);

                int doc = -1;
                int pa_id = -1;
                //open database
                SQLiteDatabase db;
                db = openOrCreateDatabase("A3App.db", MODE_PRIVATE, null);


                String doc_o_name = null;
                String doc_o_unit = null;
                String doc_d_name = null;
                //get o_name, kah, unit date from o_id
                Cursor c1 = db.rawQuery("select o_name, o_unit from operation where o_id = " + o_id, null);
                if (c1.moveToFirst()) {
                    doc_o_name = c1.getString(0);
                    doc_o_unit = c1.getString(1);
                }
                c1 = db.rawQuery("select d.d_name from detail d, operation_personnel op where d.d_id = op.d_id and op.op_id = " + current_op_id, null);
                if (c1.moveToFirst()) {
                    doc_d_name = c1.getString(0);
                }
                Log.i("bindvalues", doc_o_name + doc_o_unit + doc_d_name);
                //check if document already exists (CHECK ALL STRING PARAMS IN CASE OF DETAIL CHANGE, THUS CREATE NEW DOC IF CHANGE OCCURS)
                //c1 = db.query("document", new String[]{"o_name", "o_unit", "d_name"}, "doc_id", new String[]{doc_o_name, doc_o_unit, doc_d_name},null, null, null);
                Log.i("select doc_id from document where o_name = ? and o_unit = ? and d_name = ? and doc_closed = 0", String.valueOf(new String[]{doc_o_name, doc_o_unit, doc_d_name}));
                c1 = db.rawQuery("select doc_id from document where o_name = ? and o_unit = ? and d_name = ? and doc_closed = 0", new String[]{doc_o_name, doc_o_unit, doc_d_name});
                accessed_doc_number = "-1";
                if (c1.moveToNext()) {
                    accessed_doc_number = c1.getString(0);
                }else//if document is not opened, create new doc according to data
                    {
                    ContentValues content = new ContentValues();
                    content.put(TAG_D_NAME, doc_d_name);
                    content.put(TAG_O_NAME, doc_o_name);
                    content.put(TAG_O_UNIT, doc_o_unit);
                    Log.i("inserting", content.toString());
                    db.insert("document", null, content);
                    c1 = db.rawQuery("select doc_id from document order by doc_id DESC LIMIT 1", null);
                    if (c1.moveToFirst()) {
                        accessed_doc_number = c1.getString(0);
                    }
                }

                //check entry type
                //issuing
                if (Integer.valueOf(type) == 1) {
                    //iterate through dataset
                    for (HashMap<String, String> data : rvData) {
                        //this will change td_issued in transaction_data

                        ContentValues cv = new ContentValues();
                        //form the initial entry

                        String ammo_name = null;
                        String personnel_name = null;
                        //get td_ammo name from a_id
                        c1 = db.rawQuery("select a_name from ammunition a, personnel_ammunition pa where pa.a_id = a.a_id and pa_id = " + data.get(TAG_PA_ID), null);
                        if (c1.moveToFirst()) {
                            ammo_name = c1.getString(0);
                        }
                        //get td_personnel_name from pa_id ref p_id
                        c1 = db.rawQuery("select p.p_name from personnel p, personnel_ammunition pa, operation_personnel op where op.p_id = p.p_id and op.op_id = pa.op_id and pa.pa_id = " + data.get(TAG_PA_ID), null);
                        if (c1.moveToFirst()) {
                            personnel_name = c1.getString(0);
                        }
                        //get transaction date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY @HHmm", Locale.getDefault());
                        String currentDateTime = dateFormat.format(new Date());

                        Log.i("id", data.toString());
                        if (Integer.valueOf(data.get(TAG_TD_ID)) == -1) { //dataset doesnt exist yet
                            //form dataset
                            cv.put(TAG_PA_ID, data.get(TAG_PA_ID));
                            cv.put(TAG_DOC_NUMBER, accessed_doc_number);
                            cv.put(TAG_TD_AMMO_NAME, ammo_name);
                            cv.put(TAG_TD_PERSONNEL_NAME, personnel_name);
                            cv.put(TAG_TD_ISSUED, data.get(TAG_PA_ISSUE_QTY));
                            cv.put(TAG_TD_ISSUEDATETIME, currentDateTime);
                            cv.put(TAG_TD_ISSUESIGNATURE, getBitmapAsByteArray(Signature_Pad.getSignatureBitmap()));

                            //commit into db
                            db.insert("transaction_data", null, cv);
                        } else
                        {

                        }

                    }
                }
                //returning
                else if (Integer.valueOf(type) == 2) {
                    //this will change td_returned/expended/
                    for (HashMap<String, String> data : rvData) {
                        //update based on new values

                        //get transaction date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY @HHmm", Locale.getDefault());
                        String currentDateTime = dateFormat.format(new Date());

                        ContentValues cv = new ContentValues();
                        cv.put(TAG_TD_RETURNED, data.get(TAG_TD_RETURNED));
                        cv.put(TAG_TD_EXPENDED, data.get(TAG_TD_EXPENDED));
                        cv.put(TAG_TD_SPOILED, data.get(TAG_TD_SPOILED));
                        cv.put(TAG_TD_RETURNDATETIME, currentDateTime);
                        cv.put(TAG_TD_RETURNSIGNATURE, getBitmapAsByteArray(Signature_Pad.getSignatureBitmap()));
                        cv.put(TAG_TD_EXPORTED, 1);
                        db.update("transaction_data", cv, "td_id = ?", new String[]{data.get(TAG_TD_ID)});

                    }
                }
                //check if document can close


                //



                //close database
                db.close();

                if(PersonnelList.size() > 0)
                {
                    populateNextGuy();
                }
                else{
                    onPersonnelListEnd(Integer.valueOf(type), accessed_doc_number);
                }

            }

            //check if document can be closed
        });
    }

    public void onPersonnelListEnd(int type, String doc_number)
    {


        if(type == 1 || Integer.valueOf(doc_number) == -1)
        {
            finish();
        }
        else if(type == 2)
        {
            Intent i = new Intent(getApplicationContext(), GenerateDocumentsActivity.class);
            i.putExtra(TAG_DOC_NUMBER, doc_number);
            startActivity(i);
            //start new intent allowing ammo ic to sign, followed by generate document.
            finish();
        }
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    rvData.remove(position);
                    rvAdapter.notifyItemRemoved(position);
                    break;
                case ItemTouchHelper.RIGHT:
                    break;
            }
        }
    };

    @Override
    public void onItemClick(int position) {
        Dialog EditIssueDialog = new Dialog(DeclareIssueReturnReceiveInfoActivity.this, android.R.style.Theme_Black_NoTitleBar);
        EditIssueDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        EditIssueDialog.setContentView(R.layout.dialog_edit_issue_return_receive_ammunition);
        EditIssueDialog.setCancelable(true);
        EditIssueDialog.show();

        Button btn_EditQty;

        EditText et_ToIssueOrIssued_Qty;
        EditText et_Expended_Qty;
        EditText et_Returned_Qty;
        EditText et_Spoilt_Qty;
        TextView tv_Ammunition_Name;
        TextView tv_ToIssueOrIssued_Text;

        et_ToIssueOrIssued_Qty = (EditText) EditIssueDialog.findViewById(R.id.et_ToIssueOrIssued_Qty);
        et_Expended_Qty = (EditText) EditIssueDialog.findViewById(R.id.et_Expended_Qty);
        et_Returned_Qty = (EditText) EditIssueDialog.findViewById(R.id.et_Returned_Qty);
        et_Spoilt_Qty = (EditText) EditIssueDialog.findViewById(R.id.et_Spoilt_Qty);
        tv_Ammunition_Name = (TextView) EditIssueDialog.findViewById(R.id.tv_Ammunition_Name);
        tv_ToIssueOrIssued_Text = (TextView) EditIssueDialog.findViewById(R.id.tv_ToIssueOrIssued_Text);

        //TAG_DYNAMIC_ISSUE = true if new entry, false if existing entry
        //TAG_PA_ID = taken from above query
        //TAG_A_NAME = raw text taken from transaction details if old entry, referenced from ammunition table if old entry
        //TAG_PA_ISSUE_QTY = text taken from personnel_ammunition if entry is new, else take issued value from transaction detail
        //TAG_TD_RETURNED = text taken only from transaction detail. automatically set this value to issued quantity in event of ops
        //TAG_TD_EXPENDED = text taken only from transaction detail. automatically set this value to issued quantity in event of range
        //TAG_TD_SPOILED = text taken only from transaction detail.

        //change dataset
        HashMap<String, String> dataset;
        dataset = rvData.get(position);
        String is_ammo_issued = dataset.get(TAG_DYNAMIC_ISSUE);

        //dump data and enable/disable buttons
        if (Integer.valueOf(is_ammo_issued) == 1)//havent issue yet
        {
            //disable all unnecessary buttons
            et_ToIssueOrIssued_Qty.setEnabled(true);
            et_Expended_Qty.setEnabled(false);
            et_Spoilt_Qty.setEnabled(false);
            et_Returned_Qty.setEnabled(false);

            et_ToIssueOrIssued_Qty.setText(dataset.get(TAG_PA_ISSUE_QTY));


        } else //issued already
        {
            et_ToIssueOrIssued_Qty.setEnabled(false);
            et_Expended_Qty.setEnabled(true);
            et_Spoilt_Qty.setEnabled(true);
            et_Returned_Qty.setEnabled(true);

            et_ToIssueOrIssued_Qty.setText(dataset.get(TAG_PA_ISSUE_QTY));
        }

        btn_EditQty = (Button) EditIssueDialog.findViewById(R.id.btn_Confirm);
        btn_EditQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save transaction details
                dataset.put(TAG_PA_ISSUE_QTY, et_ToIssueOrIssued_Qty.getText().toString());
                dataset.put(TAG_TD_EXPENDED, et_Expended_Qty.getText().toString());
                dataset.put(TAG_TD_RETURNED, et_Returned_Qty.getText().toString());
                dataset.put(TAG_TD_SPOILED, et_Spoilt_Qty.getText().toString());

                rvAdapter.notifyItemChanged(position);
                EditIssueDialog.dismiss();
            }
        });
        //EditIssueDialog.dismiss();
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}