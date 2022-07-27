package com.example.safacgsoca3app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itextpdf.kernel.geom.PageSize;

import java.util.ArrayList;
import java.util.HashMap;

public class adapter_add_operation_personnel extends RecyclerView.Adapter<adapter_add_operation_personnel.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> data;

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
    private static final String TAG_CHECKED = "tag_checked";

    private final RecyclerViewInterface recyclerViewInterface;

    public adapter_add_operation_personnel(Context context, ArrayList<HashMap<String, String>> data, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public adapter_add_operation_personnel.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_add_operation_personnel, parent, false);
        Log.i("inflating", "inflate");
        return new adapter_add_operation_personnel.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_add_operation_personnel.MyViewHolder holder, int position) {
        int gpos = position;
        Log.i("entering", "onbind");

        if(data.get(position).containsKey(TAG_P_ID)) {
            holder.tv_add_operation_personnel_list_p_id.setText(data.get(position).get(TAG_P_ID));
        }
        if(data.get(position).containsKey(TAG_P_NAME)) {
            holder.tv_add_operation_personnel_list_name.setText(data.get(position).get(TAG_P_NAME));
        }
        if(data.get(position).containsKey(TAG_P_NRIC)) {
            holder.tv_add_operation_personnel_nric.setText(data.get(position).get(TAG_P_NRIC));
        }
        if(data.get(position).containsKey(TAG_CHECKED))
        {
            if(Boolean.parseBoolean(data.get(position).get(TAG_CHECKED)))
            {
                holder.cardView.setCardBackgroundColor(holder.cardView.getContext().getResources().getColor(R.color.personnel_checklist_enabled));
            }
            else
            {
                holder.cardView.setCardBackgroundColor(holder.cardView.getContext().getResources().getColor(R.color.personnel_checklist_disabled));
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
        TextView tv_add_operation_personnel_list_p_id;
        TextView tv_add_operation_personnel_list_name;
        TextView tv_add_operation_personnel_nric;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //bind view object var
            tv_add_operation_personnel_list_p_id = itemView.findViewById(R.id.tv_add_operation_personnel_list_p_id);
            tv_add_operation_personnel_list_name = itemView.findViewById(R.id.tv_add_operation_personnel_list_name);
            tv_add_operation_personnel_nric = itemView.findViewById(R.id.tv_add_operation_personnel_nric);
            cardView = itemView.findViewById(R.id.cv_list_operation_nominal_roll);

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