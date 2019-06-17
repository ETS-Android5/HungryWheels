package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BuffetGridAdapter extends BaseAdapter {
    Context context;
    ArrayList<DataModel> dataModelArrayList;

    public BuffetGridAdapter(Context context, ArrayList<DataModel> dataModelArrayList){

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
        return position;    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        convertView = layoutInflater.inflate(R.layout.activity_ft_grid_adapter,null);

        TextView tvData = (TextView)convertView.findViewById(R.id.tv_buffetdata);
        ImageView imgData = (ImageView)convertView.findViewById(R.id.img_buffetdata);

        final DataModel dataModel =(DataModel)getItem(position);

        tvData.setText(dataModel.getStrData());
        imgData.setImageResource(dataModel.getImgData());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context ,BuffetsActivity.class);
                context.startActivity(i);

                /*               Toast.makeText(context, "Item is "+dataModel.getStrData(), Toast.LENGTH_SHORT).show();*/
            }
        });


        return convertView;
    }


}


