package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class cart_adapter extends BaseAdapter {

    private  DatabaseReference reference;
    Context context;
    ArrayList<CartModel> dataModelArrayList;

    public cart_adapter(Context context, ArrayList<CartModel> dataModelArrayList) {
        this.context = context;
        this.dataModelArrayList = dataModelArrayList;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Cart");


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
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        convertView = layoutInflater.inflate(R.layout.activity_cart_adapter, null);

        TextView foodname = (TextView) convertView.findViewById(R.id.cart_name);
        TextView foodprice = (TextView) convertView.findViewById(R.id.cart_price);
        ImageView foodimage = (ImageView) convertView.findViewById(R.id.cart_img);
        ImageView remove = (ImageView) convertView.findViewById(R.id.img_remove);

        foodname.setText(dataModelArrayList.get(position).getFood_name());
        foodprice.setText(dataModelArrayList.get(position).getFood_price());
        // Picasso.get().load(dataModelArrayList.get(position).getResImage()).placeholder(R.drawable.hw).fit().into(imgFoodname);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strKey = dataModelArrayList.get(position).getCart_id();
                Log.e("Adapter", "****" + strKey);

               reference.child(strKey).removeValue();

               /* reference.orderByKey().equalTo(strKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postsnapshot :dataSnapshot.getChildren()) {

                            String key = postsnapshot.getKey();
                              Log.e("Adapter", "****" + key);

                            dataSnapshot.getRef().removeValue();
                            Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
          */    /*  reference.child("Cart").orderByKey().equalTo(strKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postsnapshot :dataSnapshot.getChildren()) {

                            String key = postsnapshot.getKey();
                            dataSnapshot.getRef().removeValue();

                        }*/
            }
        });

      /*  convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strKey = dataModelArrayList.get(position).getCart_id();
                Log.e("Adapter", "****" + strKey);
                reference.child(strKey).removeValue();
                Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();

            }
        });
*/
        return convertView;
    }
}
