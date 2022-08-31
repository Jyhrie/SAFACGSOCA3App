package com.example.safacgsoca3app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;




public class adapter_ddl_add_ammunition extends BaseAdapter {

    private static final String TAG_DDL_AMMO_NAME = "ddl_ammo_name";

    Context context;
    ArrayList<HashMap<String,String>> data;
    LayoutInflater inflter;

    public adapter_ddl_add_ammunition(Context applicationContext, ArrayList<HashMap<String,String>> data ) {
        this.context = applicationContext;
        this.data = data;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void filterList(ArrayList<HashMap<String, String>> filteredList)
    {
        data = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_add_ammunition, null);
        TextView names = (TextView) view.findViewById(R.id.tv_text);
        names.setText(data.get(i).get(TAG_DDL_AMMO_NAME));
        return view;
    }




}
