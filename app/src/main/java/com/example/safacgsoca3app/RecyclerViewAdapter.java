package com.example.safacgsoca3app;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> data;

    public RecyclerViewAdapter(Context context, ArrayList<HashMap<String, String>> data)
    {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //View view = inflater.inflate(R.layout, parent, false)
        //return new RecyclerViewAdapter.MyViewHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        //holder.text.setText(data.get(position).get(key))
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //declare view object var

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //bind view object var
        }
    }
}
