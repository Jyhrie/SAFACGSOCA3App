package com.example.safacgsoca3app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class adapter_Issue_Ammunition extends RecyclerView.Adapter<adapter_Issue_Ammunition.MyViewHolder> {

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<HashMap<String, String>> data;

    private static final String TAG_DYNAMIC_ISSUE = "dynamic_issue";

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
    private static final String TAG_PID ="p_id";
    private static final String TAG_NAME = "e_name";
    private static final String TAG_PNAME = "p_name";
    private static final String TAG_PRANK = "p_rank";
    private static final String TAG_KAH = "o_kah";
    private static final String TAG_ANAME = "a_name";
    private static final String TAG_OPID = "op_id";

    public adapter_Issue_Ammunition(Context context, ArrayList<HashMap<String, String>> data, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public adapter_Issue_Ammunition.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_issue_return_receive_ammunition, parent, false);
        Log.i("inflating", "inflate");
        return new adapter_Issue_Ammunition.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_Issue_Ammunition.MyViewHolder holder, int position) {
        if(data.get(position).containsKey(TAG_DYNAMIC_ISSUE)) {
            holder.tv_dynamic_issue.setText(data.get(position).get(TAG_DYNAMIC_ISSUE));
        }
        if(data.get(position).containsKey(TAG_PA_ID)) {
            holder.tv_issuing_pa_id.setText(data.get(position).get(TAG_PA_ID));
        }
        if(data.get(position).containsKey(TAG_A_NAME)) {
            holder.tv_issuing_a_name.setText(data.get(position).get(TAG_A_NAME));
        }
        if(data.get(position).containsKey(TAG_PA_ISSUE_QTY)) {
            holder.tv_issue_or_issued_qty.setText(data.get(position).get(TAG_PA_ISSUE_QTY));
        }
        if(data.get(position).containsKey(TAG_TD_RETURNED)) {
            holder.tv_returned_qty.setText(data.get(position).get(TAG_TD_RETURNED));
        }
        if(data.get(position).containsKey(TAG_TD_EXPENDED)) {
            holder.tv_expended_qty.setText(data.get(position).get(TAG_TD_EXPENDED));
        }
        if(data.get(position).containsKey(TAG_TD_SPOILED)) {
            holder.tv_spoiled_qty.setText(data.get(position).get(TAG_TD_SPOILED));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_dynamic_issue;
        TextView tv_issuing_pa_id;
        TextView tv_issuing_a_name;
        TextView tv_issue_or_issued_qty;
        TextView tv_expended_qty;
        TextView tv_returned_qty;
        TextView tv_spoiled_qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //bind view object var
            tv_dynamic_issue = itemView.findViewById(R.id.tv_dynamic_issue);
            tv_issuing_pa_id = itemView.findViewById(R.id.tv_issuing_pa_id);
            tv_issuing_a_name = itemView.findViewById(R.id.tv_issuing_a_name);
            tv_issue_or_issued_qty = itemView.findViewById(R.id.tv_issue_or_issued_qty);
            tv_expended_qty = itemView.findViewById(R.id.tv_expended_qty);
            tv_returned_qty = itemView.findViewById(R.id.tv_returned_qty);
            tv_spoiled_qty = itemView.findViewById(R.id.tv_spoilt_qty);

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
