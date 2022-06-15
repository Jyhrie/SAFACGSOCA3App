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

public class adapter_Operation_Nominal_Roll extends RecyclerView.Adapter<adapter_Operation_Nominal_Roll.MyViewHolder>{

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

    private static final String TAG_DESIGNATION_NAME = "designation_name";
    private static final String TAG_DESIGNATION = "designation";

    RecyclerViewInterface recyclerViewInterface;
    ArrayList<HashMap<String,String>> data;
    Context context;

    public adapter_Operation_Nominal_Roll(Context context, ArrayList<HashMap<String, String>> data, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_operation_nominal_roll, parent, false);
        return new adapter_Operation_Nominal_Roll.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_personnel_o_id_list_operation_nominal_roll.setText(data.get(position).get(TAG_P_ID));
        holder.tv_personnel_name_list_operation_nominal_roll.setText(data.get(position).get(TAG_P_NAME));
        holder.tv_personnel_nric_list_operation_nominal_roll.setText(data.get(position).get(TAG_P_NRIC));
        holder.tv_personnel_detail_list_operation_nominal_roll.setText(data.get(position).get(TAG_D_NAME));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_personnel_o_id_list_operation_nominal_roll;
        TextView tv_personnel_name_list_operation_nominal_roll;
        TextView tv_personnel_nric_list_operation_nominal_roll;
        TextView tv_personnel_detail_list_operation_nominal_roll;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_personnel_o_id_list_operation_nominal_roll = itemView.findViewById(R.id.tv_personnel_o_id_list_operation_nominal_roll);
            tv_personnel_name_list_operation_nominal_roll = itemView.findViewById(R.id.tv_personnel_name_list_operation_nominal_roll);
            tv_personnel_nric_list_operation_nominal_roll = itemView.findViewById(R.id.tv_personnel_nric_list_operation_nominal_roll);
            tv_personnel_detail_list_operation_nominal_roll = itemView.findViewById(R.id.tv_personnel_detail_list_operation_nominal_roll);

            }
        }
}
