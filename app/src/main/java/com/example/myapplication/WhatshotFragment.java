package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WhatshotFragment extends Fragment {


    @Nullable

    ViewPager viewPager;
    int images[] = {R.drawable.burger, R.drawable.coupons,
            R.drawable.discounts, R.drawable.dominos};

    private Object Viewpager_deals;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_whatshot,container,false);

        viewPager = (ViewPager) rootview.findViewById(R.id.view_pager);
        Viewpager_deals = new Viewpager_deals(getActivity(), images);
        viewPager.setAdapter((PagerAdapter) Viewpager_deals);

        return rootview;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("WhatsHot");
    }
}

