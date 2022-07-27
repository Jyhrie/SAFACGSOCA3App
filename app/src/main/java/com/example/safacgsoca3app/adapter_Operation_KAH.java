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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class adapter_Operation_KAH extends RecyclerView.Adapter<adapter_Operation_KAH.MyViewHolder> {
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

    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<HashMap<String,String>> data;


    public adapter_Operation_KAH(Context context, ArrayList<HashMap<String, String>> data, RecyclerViewInterface recyclerViewInterface)
    {
        this.recyclerViewInterface = recyclerViewInterface;
        this.context = context;
        this.data = data;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_operation_params, parent, false);
        Log.i("inflating", "inflate");
        return new adapter_Operation_KAH.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int gpos = position;
        Log.i("entering", "onbind");
        if(data.get(position).containsKey(TAG_OP_ID)) {
            holder.et_op_param_designation.setText(data.get(position).get(TAG_OP_ID));
        }
        if(data.get(position).containsKey(TAG_P_NAME)) {
            holder.et_op_param_name.setText(data.get(position).get(TAG_P_NAME));
        }


        holder.et_op_param_designation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.get(gpos).put(TAG_DESIGNATION, holder.et_op_param_designation.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.et_op_param_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                data.get(gpos).put(TAG_DESIGNATION_NAME, holder.et_op_param_name.getText().toString());
                Log.i(String.valueOf(data),"data");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        //declare view object var
        EditText et_op_param_designation;
        EditText et_op_param_name;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //bind view object var
            et_op_param_designation = itemView.findViewById(R.id.et_op_param_designation);
            et_op_param_name = itemView.findViewById(R.id.et_op_param_name);


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
