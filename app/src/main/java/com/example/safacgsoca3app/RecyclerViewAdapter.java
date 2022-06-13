package com.example.safacgsoca3app;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> data;
    String[] mfrom;
    int[] mTo;

    public RecyclerViewAdapter(Context context, ArrayList<HashMap<String, String>> data)
    {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_assign_personnel_ammunition, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        //holder.tv_existing_pa_id.setText(data.get(position).get("key"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //declare view object var
        TextView tv_existing_pa_id;
        TextView tv_selected_ammo_id;
        Spinner ddl_assign_personnel_ammunition;
        EditText et_assign_personnel_ammunition_qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //bind view object var
        }
    }

}
