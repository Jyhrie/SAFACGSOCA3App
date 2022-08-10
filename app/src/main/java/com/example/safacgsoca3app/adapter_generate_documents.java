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

public class adapter_generate_documents extends RecyclerView.Adapter<adapter_generate_documents.MyViewHolder>{

    private static final String TAG_AMMO_NAME = "ammo_name";
    private static final String TAG_PERSONNEL_NAME = "personnel_name";
    private static final String TAG_TD_ISSUED = "td_issued";
    private static final String TAG_TD_RETURNED = "td_returned";
    private static final String TAG_TD_EXPENDED = "td_expended";
    private static final String TAG_TD_SPOILED = "td_spoiled";
    private static final String TAG_ISSUE_DATETIME = "td_issuedatetime";
    private static final String TAG_RETURN_DATETIME = "td_returndatetime";
    private static final String TAG_ISSUE_SIGNATURE = "td_issuesignaturerecieving";
    private static final String TAG_RETURN_SIGNATURE = "td_returnsignaturerecieving";
    private static final String TAG_TD_ID = "td_id";

    private static final String TAG_D_NAME = "d_name";
    private static final String TAG_O_NAME = "o_name";
    private static final String TAG_O_UNIT = "o_unit";

    private static final String TAG_DOC_NUMBER = "doc_id";

    Context context;
    ArrayList<HashMap<String, String>> data;

private final RecyclerViewInterface recyclerViewInterface;

public adapter_generate_documents(Context context, ArrayList<HashMap<String, String>> data, RecyclerViewInterface recyclerViewInterface)
        {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
        }

    public adapter_generate_documents(RecyclerViewInterface recyclerViewInterface) {
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
@Override
public adapter_generate_documents.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_generate_document_check, parent, false);
        Log.i("inflating", "inflate");
        return new adapter_generate_documents.MyViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull adapter_generate_documents.MyViewHolder holder, int position) {
        int gpos = position;
        Log.i("entering", "onbind");

        if(data.get(position).containsKey(TAG_AMMO_NAME)) {
            Log.i(data.get(position).get(TAG_AMMO_NAME), "Test");
        holder.tv_gen_doc_ammo.setText(data.get(position).get(TAG_AMMO_NAME));
        }
        if(data.get(position).containsKey(TAG_PERSONNEL_NAME)) {
        holder.tv_gen_doc_personnel.setText(data.get(position).get(TAG_PERSONNEL_NAME));
        }
        if(data.get(position).containsKey(TAG_TD_ISSUED)) {
        holder.tv_gen_doc_issued.setText(data.get(position).get(TAG_TD_ISSUED));
        }
        if(data.get(position).containsKey(TAG_TD_RETURNED)) {
            holder.tv_gen_doc_returned.setText(data.get(position).get(TAG_TD_RETURNED));
        }
        if(data.get(position).containsKey(TAG_TD_EXPENDED)) {
            holder.tv_gen_doc_expended.setText(data.get(position).get(TAG_TD_EXPENDED));
        }
        }

@Override
public int getItemCount() {
        return data.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder{

    //declare view object var

    TextView tv_gen_doc_ammo;
    TextView tv_gen_doc_personnel;
    TextView tv_gen_doc_issued;
    TextView tv_gen_doc_returned;
    TextView tv_gen_doc_expended;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        //bind view object var
        tv_gen_doc_ammo = itemView.findViewById(R.id.tv_gen_doc_ammo);
        tv_gen_doc_personnel = itemView.findViewById(R.id.tv_gen_doc_personnel);
        tv_gen_doc_issued = itemView.findViewById(R.id.tv_gen_doc_issued);
        tv_gen_doc_returned = itemView.findViewById(R.id.tv_gen_doc_returned);
        tv_gen_doc_expended = itemView.findViewById(R.id.tv_gen_doc_expended);


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
