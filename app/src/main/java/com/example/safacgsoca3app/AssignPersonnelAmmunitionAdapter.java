package com.example.safacgsoca3app;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class AssignPersonnelAmmunitionAdapter extends RecyclerView.Adapter<AssignPersonnelAmmunitionAdapter.MyViewHolder> {

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

    public AssignPersonnelAmmunitionAdapter(Context context, ArrayList<HashMap<String, String>> data, ArrayList<HashMap<String,String>> ddlData, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
        this.ddlData = ddlData;
    }

    @NonNull
    @Override
    public AssignPersonnelAmmunitionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_assign_personnel_ammunition, parent, false);
        return new AssignPersonnelAmmunitionAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignPersonnelAmmunitionAdapter.MyViewHolder holder, int position) {
        int gpos = position;
        holder.tv_existing_pa_id.setText(data.get(position).get(TAG_PA_ID));
        holder.tv_selected_ammo_id.setText(data.get(position).get(TAG_A_ID));

        //form ddl adapter
        SimpleAdapter ddlAdapter = new SimpleAdapter(context,
                ddlData,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{TAG_A_ID},
                new int[]{android.R.id.text1});
        holder.ddl_assign_personnel_ammunition.setAdapter(ddlAdapter);

        //get selection position;
        for(int i = 0; i< ddlData.size(); i++)
        {
            if(ddlData.get(i).get(TAG_A_ID).equals(data.get(position).get(TAG_A_ID)))
            {
                int pos = parseInt(data.get(position).get(TAG_A_ID));
                holder.ddl_assign_personnel_ammunition.setSelection(pos);
                break;
            }
        }
        holder.et_assign_personnel_ammunition_qty.setText(data.get(position).get(TAG_PA_ISSUE_QTY));


        //reactive functions

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

                String line_ammo_qty = holder.et_assign_personnel_ammunition_qty.getText().toString();
                HashMap<String, String> prevData = new HashMap<String,String>(data.get(gpos));
                prevData.replace(TAG_PA_ISSUE_QTY, line_ammo_qty);
                data.set(gpos, prevData);
                Log.i("TEXT CHANGED", String.valueOf(gpos));
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<HashMap<String, String>> getData()
    {
        return data;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        //declare view object var
        TextView tv_existing_pa_id;
        TextView tv_selected_ammo_id;
        Spinner ddl_assign_personnel_ammunition;
        EditText et_assign_personnel_ammunition_qty;

        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
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
