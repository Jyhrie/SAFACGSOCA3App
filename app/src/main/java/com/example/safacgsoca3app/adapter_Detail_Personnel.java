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

public class adapter_Detail_Personnel extends RecyclerView.Adapter<adapter_Detail_Personnel.MyViewHolder> {

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

    public adapter_Detail_Personnel(Context context, ArrayList<HashMap<String, String>> data, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public adapter_Detail_Personnel.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_view_detail_personnel, parent, false);
        Log.i("inflating", "inflate");
        return new adapter_Detail_Personnel.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_Detail_Personnel.MyViewHolder holder, int position) {
        int gpos = position;
        Log.i("entering", "onbind");
        if(data.get(position).containsKey(TAG_OP_ID)) {
            holder.tv_edit_detail_personnel_id.setText(data.get(position).get(TAG_OP_ID));
        }
        if(data.get(position).containsKey(TAG_P_NAME)) {
            holder.tv_edit_detail_personnel_name.setText(data.get(position).get(TAG_P_NAME));
        }
        if(data.get(position).containsKey(TAG_P_NRIC)) {
            holder.tv_edit_detail_personnel_nric.setText(data.get(position).get(TAG_P_NRIC));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        //declare view object var
        TextView tv_edit_detail_personnel_id;
        TextView tv_edit_detail_personnel_name;
        TextView tv_edit_detail_personnel_nric;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //bind view object var
            tv_edit_detail_personnel_id = itemView.findViewById(R.id.tv_edit_detail_personnel_id);
            tv_edit_detail_personnel_name = itemView.findViewById(R.id.tv_edit_detail_personnel_name);
            tv_edit_detail_personnel_nric = itemView.findViewById(R.id.tv_edit_detail_personnel_nric);

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
