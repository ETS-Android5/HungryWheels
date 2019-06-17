package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ViewpagerCustom extends PagerAdapter {

    FragmentActivity activity;
    int[] images;

    public ViewpagerCustom(FragmentActivity activity, int[] images) {

        this.activity = activity;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
////        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
////        View itemView = layoutInflater.inflate(R.layout.viewpager, container, false);
//
//        ImageView imageView = itemView.findViewById(R.id.imageview);
//        imageView.setImageResource(images[position]);
//
//        container.addView(itemView);
//
//        return itemView;
        return container;
    }
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

