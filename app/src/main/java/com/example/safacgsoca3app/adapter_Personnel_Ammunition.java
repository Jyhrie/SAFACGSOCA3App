package com.example.safacgsoca3app;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class adapter_Personnel_Ammunition extends RecyclerView.Adapter<adapter_Personnel_Ammunition.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> data;
    ArrayList<HashMap<String, String>> ddlData;

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

    private final RecyclerViewInterface recyclerViewInterface;

    public adapter_Personnel_Ammunition(Context context, ArrayList<HashMap<String, String>> data, ArrayList<HashMap<String,String>> ddlData, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
        this.ddlData = ddlData;
    }

    @NonNull
    @Override
    public adapter_Personnel_Ammunition.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_assign_personnel_ammunition, parent, false);
        return new adapter_Personnel_Ammunition.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_Personnel_Ammunition.MyViewHolder holder, int position) {
        int gpos = position;
        if(!data.get(position).containsKey(TAG_PA_ID)) {
            holder.tv_existing_pa_id.setText(data.get(position).get(TAG_PA_ID));
        }
        if(!data.get(position).containsKey(TAG_A_ID)) {
            holder.tv_selected_ammo_id.setText(data.get(position).get(TAG_A_ID));
        }

        Log.i("data", String.valueOf(ddlData.size()));
        //form ddl adapter
        SimpleAdapter ddlAdapter = new SimpleAdapter(context,
                ddlData,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{TAG_A_NAME},
                new int[]{android.R.id.text1});
        holder.ddl_assign_personnel_ammunition.setAdapter(ddlAdapter);

        //get selection position;
        for(int i = 0; i< ddlData.size(); i++)
        {
            if(ddlData.get(i).get(TAG_A_ID).equals(data.get(gpos).get(TAG_A_ID)))
            {
                holder.ddl_assign_personnel_ammunition.setSelection(i);
                break;
            }
        }

        if(data.get(position).containsKey(TAG_PA_ISSUE_QTY)) {
            Log.i("has issue qty", data.get(position).get(TAG_PA_ISSUE_QTY));
            holder.et_assign_personnel_ammunition_qty.setText(data.get(position).get(TAG_PA_ISSUE_QTY));
        }

        //reactive functions

        holder.ddl_assign_personnel_ammunition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //set selected ammo id tv to ammo id

                Log.i("gpos", String.valueOf(gpos));

                if(data.get(gpos).containsKey(TAG_A_ID)) {
                    data.get(gpos).replace(TAG_A_ID, ddlData.get(holder.ddl_assign_personnel_ammunition.getSelectedItemPosition()).get(TAG_A_ID));
                }else
                {
                    Log.i("NO AID EXISTS", String.valueOf(gpos));
                    data.get(gpos).put(TAG_A_ID, ddlData.get(holder.ddl_assign_personnel_ammunition.getSelectedItemPosition()).get(TAG_A_ID));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        holder.et_assign_personnel_ammunition_qty.addTextChangedListener(new TextWatcher() {

            //unused
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (data.get(gpos).containsKey(TAG_PA_ISSUE_QTY)) {
                    data.get(gpos).replace(TAG_PA_ISSUE_QTY, holder.et_assign_personnel_ammunition_qty.getText().toString());
                }
                else
                {
                    data.get(gpos).put(TAG_PA_ISSUE_QTY, holder.et_assign_personnel_ammunition_qty.getText().toString());
                }
                Log.i("TEXT CHANGED", String.valueOf(gpos));
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        //declare view object var
        TextView tv_existing_pa_id;
        TextView tv_selected_ammo_id;
        Spinner ddl_assign_personnel_ammunition;
        EditText et_assign_personnel_ammunition_qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //bind view object var
            tv_existing_pa_id = itemView.findViewById(R.id.tv_existing_pa_id);
            tv_selected_ammo_id = itemView.findViewById(R.id.tv_selected_ammo_id);
            ddl_assign_personnel_ammunition = itemView.findViewById(R.id.ddl_assign_personnel_ammunition);
            et_assign_personnel_ammunition_qty = itemView.findViewById(R.id.et_assign_personnel_ammunition_qty);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null)
                    {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });



        }
    }


}
