package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyGridAdapter extends BaseAdapter {

    Context context;
    ArrayList<DataModel> dataModelArrayList;

    public MyGridAdapter(Context context, ArrayList<DataModel> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
    }


    @Override
    public int getCount() {
        return dataModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = layoutInflater.inflate(R.layout.raw_grid,null);

        TextView tvData = (TextView)convertView.findViewById(R.id.tv_data);
        ImageView imgData = (ImageView)convertView.findViewById(R.id.img_data);

        DataModel dataModel =(DataModel)getItem(position);

        tvData.setText(dataModel.getStrLang());
        imgData.setImageResource(dataModel.getImgLang());

        return convertView;
    }
}