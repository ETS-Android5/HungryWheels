package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class FtGridAdapter extends BaseAdapter {
    Context context;
    ArrayList<DataModel> dataModelArrayList;

    public FtGridAdapter(Context context, ArrayList<DataModel> dataModelArrayList){

        this.context = context;
        this.dataModelArrayList = dataModelArrayList;

    }

    @Override
    public int getCount() {
        return dataModelArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataModelArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



        view = layoutInflater.inflate(R.layout.activity_ft_grid_adapter,null);

        TextView tvData = (TextView)view.findViewById(R.id.tv_ftdata);
        ImageView imgData = (ImageView)view.findViewById(R.id.img_ftdata);

        final DataModel dataModel =(DataModel)getItem(i);

        tvData.setText(dataModel.getStrData());
        imgData.setImageResource(dataModel.getImgData());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent i = new Intent(context ,FoodtrucksActivity.class);
        context.startActivity(i);

 /*               Toast.makeText(context, "Item is "+dataModel.getStrData(), Toast.LENGTH_SHORT).show();*/
            }
        });


        return view;
    }


}

