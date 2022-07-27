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

public class adapter_Nominal_Roll extends RecyclerView.Adapter<adapter_Nominal_Roll.MyViewHolder> {

    RecyclerViewInterface recyclerViewInterface;
    ArrayList<HashMap<String,String>> data;
    Context context;

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


    public adapter_Nominal_Roll(Context context, ArrayList<HashMap<String, String>> data, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_nominalroll, parent, false);
        Log.i("inflating", "inflate");
        return new adapter_Nominal_Roll.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.tvPersonnelId.setText(data.get(position).get(TAG_P_ID));
            holder.tvPersonnelName.setText(data.get(position).get(TAG_P_NAME));
            holder.tvPersonnelRemarks.setText(data.get(position).get(TAG_P_NRIC));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPersonnelId;
        TextView tvPersonnelName;
        TextView tvPersonnelRemarks;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPersonnelId = itemView.findViewById(R.id.tvPersonnelId);
            tvPersonnelName = itemView.findViewById(R.id.tvPersonnelName);
            tvPersonnelRemarks = itemView.findViewById(R.id.tvPersonnelRemarks);

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view)
                {
                    int pos = getAdapterPosition();
                    if(pos!=RecyclerView.NO_POSITION)
                    {
                        recyclerViewInterface.onLongItemClick(pos);
                    }
                    return true;
                }
            });
        }
    }
}
