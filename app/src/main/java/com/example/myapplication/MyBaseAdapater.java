package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyBaseAdapater extends BaseAdapter {

    Context context;
    ArrayList<RestModel> dataModelArrayList;
    public MyBaseAdapater(Context context ,ArrayList<RestModel> dataModelArrayList) {

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        convertView = layoutInflater.inflate(R.layout.activity_raw__listview,null);

        TextView tvFoodname = (TextView) convertView.findViewById(R.id.tv_foodname);
        TextView tvCategory = (TextView) convertView.findViewById(R.id.tv_category);
        TextView tvLocation = (TextView) convertView.findViewById(R.id.tv_location);
        TextView tvMoney = (TextView) convertView.findViewById(R.id.tv_money);
        ImageView imgFoodname = (ImageView) convertView.findViewById(R.id.img_foodimag);
        Button btn_add_menu = convertView.findViewById(R.id.btn_add_menu);

        tvFoodname.setText(dataModelArrayList.get(position).getResname());
        tvCategory.setText(dataModelArrayList.get(position).getCategory());
        tvLocation.setText(dataModelArrayList.get(position).getCityname());
        tvMoney.setText(dataModelArrayList.get(position).getMoney());
        Picasso.get().load(dataModelArrayList.get(position).getResImage()).placeholder(R.drawable.hw).fit().into(imgFoodname);

        btn_add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Click event....", Toast.LENGTH_SHORT).show();
//                Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
//                    final int position = (int) view.getTag();
                String strResId = dataModelArrayList.get(position).getRes_key();
                Intent intent = new Intent(context,add_menu_res.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("RES_ID",strResId);
                context.startActivity(intent);
//                mViewClickListener.onItemClick(position);
            }
        });

        // imgFoodname.setImageResource(dataModelArrayList.get(position).getImgData());

        //Log.e("BASE","lang"+dataModelArrayList.get(position).getStrData());

/*
        tvData.setText(dataModelArrayList.get(position).getStrLang());
        imgData.setImageResource(dataModelArrayList.get(position).getImgLang());
*/
        return convertView;
    }



}
