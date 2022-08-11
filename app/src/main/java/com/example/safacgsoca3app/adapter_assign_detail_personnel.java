package com.example.safacgsoca3app;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class adapter_assign_detail_personnel extends RecyclerView.Adapter<adapter_assign_detail_personnel.MyViewHolder>{
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

    private static final String TAG_ID = "o_id";
    private static final String TAG_PID = "p_id";
    private static final String TAG_NAME = "e_name";
    private static final String TAG_PNAME = "p_name";
    private static final String TAG_PRANK = "p_rank";
    private static final String TAG_KAH = "o_kah";
    private static final String TAG_ANAME = "a_name";
    private static final String TAG_OPID = "op_id";
    private static final String TAG_ENABLED = "enabled";

    Context context;
    ArrayList<HashMap<String, String>> data;

    private final RecyclerViewInterface recyclerViewInterface;

    public adapter_assign_detail_personnel(Context context, ArrayList<HashMap<String, String>> data, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public adapter_assign_detail_personnel.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_assign_detail_personnel, parent, false);
        Log.i("inflating", "inflate");
        return new adapter_assign_detail_personnel.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull adapter_assign_detail_personnel.MyViewHolder holder, int position) {

        if(data.get(position).containsKey(TAG_OP_ID)) {
            holder.tv_assign_detail_personnel_id.setText(data.get(position).get(TAG_OP_ID));
        }
        if(data.get(position).containsKey(TAG_P_NAME)) {
            holder.tv_assign_detail_personnel_name.setText(data.get(position).get(TAG_P_NAME));
        }
        if(data.get(position).containsKey(TAG_ENABLED)) {
            if(data.get(position).get(TAG_ENABLED).equals("1"))
            {
                holder.cv_assign_detail_list.setCardBackgroundColor(holder.cv_assign_detail_list.getContext().getResources().getColor(R.color.personnel_checklist_enabled));
            }
            else if(data.get(position).get(TAG_ENABLED).equals("0"))
            {
                int nightModeFlags =
                        context.getResources().getConfiguration().uiMode &
                                Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        holder.cv_assign_detail_list.setCardBackgroundColor(holder.cv_assign_detail_list.getContext().getResources().getColor(R.color.personnel_checklist_disabledLight));
                        break;

                    case Configuration.UI_MODE_NIGHT_YES:
                        holder.cv_assign_detail_list.setCardBackgroundColor(holder.cv_assign_detail_list.getContext().getResources().getColor(R.color.personnel_checklist_disabledDark));
                        break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void filterList(ArrayList<HashMap<String, String>> filteredList)
    {
        data = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        //declare view object var

        TextView tv_assign_detail_personnel_id;
        TextView tv_assign_detail_personnel_name;
        CardView cv_assign_detail_list;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //bind view object var
            tv_assign_detail_personnel_id = itemView.findViewById(R.id.tv_assign_detail_personnel_id);
            tv_assign_detail_personnel_name= itemView.findViewById(R.id.tv_assign_detail_personnel_name);
            cv_assign_detail_list = itemView.findViewById(R.id.cv_assign_detail_list);

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

