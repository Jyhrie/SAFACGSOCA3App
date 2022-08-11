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

public class adapter_personnel_checklist extends RecyclerView.Adapter<adapter_personnel_checklist.MyViewHolder>{

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

    public static final String TAG_ID = "op_id";
    public static final String TAG_RANK = "p_rank";
    public static final String TAG_NAME = "p_name";
    public static final String TAG_REMARKS = "p_remarks";
    private static final String TAG_ENABLED = "enabled";

    private final RecyclerViewInterface recyclerViewInterface;

    public adapter_personnel_checklist(Context context, ArrayList<HashMap<String, String>> data, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public adapter_personnel_checklist.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_personnel_checklist, parent, false);
        Log.i("inflating", "inflate");
        return new adapter_personnel_checklist.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_personnel_checklist.MyViewHolder holder, int position) {
        if(data.get(position).containsKey(TAG_ID)) {
            holder.tv_Personnel_ID_Checklist.setText(data.get(position).get(TAG_ID));
        }
        if(data.get(position).containsKey(TAG_RANK)) {
            holder.tv_Personnel_Rank_Checklist.setText(data.get(position).get(TAG_RANK));
        }
        if(data.get(position).containsKey(TAG_NAME)) {
            holder.tv_Personnel_Name_Checklist.setText(data.get(position).get(TAG_NAME));
        }
        if(data.get(position).containsKey(TAG_REMARKS)) {
            holder.tv_Personnel_NRIC_Checklist.setText(data.get(position).get(TAG_REMARKS));
        }
        if(data.get(position).containsKey(TAG_ENABLED))
        {
            if(data.get(position).get(TAG_ENABLED).equals("1"))
            {
                holder.cardView.setCardBackgroundColor(holder.cardView.getContext().getResources().getColor(R.color.personnel_checklist_enabled));
            }
            else if(data.get(position).get(TAG_ENABLED).equals("0"))
            {
                int nightModeFlags =
                        context.getResources().getConfiguration().uiMode &
                                Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags) {
                    case Configuration.UI_MODE_NIGHT_NO:
                        holder.cardView.setCardBackgroundColor(holder.cardView.getContext().getResources().getColor(R.color.personnel_checklist_disabledLight));
                        break;

                    case Configuration.UI_MODE_NIGHT_YES:
                        holder.cardView.setCardBackgroundColor(holder.cardView.getContext().getResources().getColor(R.color.personnel_checklist_disabledDark));
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
        TextView tv_Personnel_ID_Checklist;
        TextView tv_Personnel_Rank_Checklist;
        TextView tv_Personnel_Name_Checklist;
        TextView tv_Personnel_NRIC_Checklist;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //bind view object var
            tv_Personnel_ID_Checklist = itemView.findViewById(R.id.tv_Personnel_ID_Checklist);
            tv_Personnel_Rank_Checklist = itemView.findViewById(R.id.tv_Personnel_Rank_Checklist);
            tv_Personnel_Name_Checklist = itemView.findViewById(R.id.tv_Personnel_Name_Checklist);
            tv_Personnel_NRIC_Checklist = itemView.findViewById(R.id.tv_Personnel_NRIC_Checklist);
            cardView = itemView.findViewById(R.id.cv_list_personnel_checklist);

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

