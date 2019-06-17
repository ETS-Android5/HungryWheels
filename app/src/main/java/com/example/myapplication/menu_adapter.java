package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class menu_adapter extends BaseAdapter {

    private String strUid;
    StorageReference mStorage;
    Context context;
    ArrayList<Res_MenuModel> dataModelArrayList;

    TextView tvFoodname;
    TextView tvFoodprice;

    ArrayList<String> stringArrayList;
    String foodName;
    String foodPrice;

    FirebaseDatabase database;
    DatabaseReference foodList;
    String strRe_Key;


    public menu_adapter(Context context, ArrayList<Res_MenuModel> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Cart");

        stringArrayList = new ArrayList<String>();

        SharedPreferences sharedPreferences = context.getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
        strUid = sharedPreferences.getString("USER_UID_KEY", "");
        Log.e("ACCC", "uid" + strUid);

        mStorage = FirebaseStorage.getInstance().getReference("Restaurant_menu");

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

        convertView = layoutInflater.inflate(R.layout.activity_menu_adapter,null);

        tvFoodname = (TextView) convertView.findViewById(R.id.tv_foodname1);
        TextView description = (TextView) convertView.findViewById(R.id.tv_desc);
        TextView quantity = (TextView) convertView.findViewById(R.id.tv_qua);
        tvFoodprice = (TextView) convertView.findViewById(R.id.tv_price);
        ImageView imgFoodname = (ImageView) convertView.findViewById(R.id.menu_foodimag);
        Button btn_add = convertView.findViewById(R.id.btn_addtocart);

        tvFoodname.setText(dataModelArrayList.get(position).getFood_name());
        description.setText(dataModelArrayList.get(position).getFood_des());
        quantity.setText(dataModelArrayList.get(position).getFood_qua());
        tvFoodprice.setText(dataModelArrayList.get(position).getFood_price());
        Picasso.get().load(dataModelArrayList.get(position).getFood_img()).placeholder(R.drawable.hwtrans).fit().into(imgFoodname);

        btn_add.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {

                                           foodName = dataModelArrayList.get(position).getFood_name();
                                           foodPrice = dataModelArrayList.get(position).getFood_price();


                                           strRe_Key = foodList.push().getKey();
                                          // String strCart_key = foodList.push().getKey();
                                           CartModel cartModel = new CartModel();
                                           cartModel.setCart_id(strRe_Key);
                                           cartModel.setFood_name(foodName);
                                           cartModel.setFood_price(foodPrice);
                                           cartModel.setUser_cart_id(strUid);
                                          // foodList.child(strCart_key).setValue(strUid);

                                           foodList.child(strRe_Key).setValue(cartModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   Toast.makeText(context, "Item added to cart", Toast.LENGTH_SHORT).show();
                                               }
                                           });

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

