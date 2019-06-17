package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    TextView tvSearch;
    ImageView imgpicks;
    ImageView imgft;
    ImageView imgcafe;
    ImageView imgln;
    ImageView imgbuffet;
    ImageView imgsf;
    ImageView imgtiffin;

    @Nullable

    ArrayList<DataModel> dataModelArrayList;


    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        tvSearch = (TextView) rootview.findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),SearchActivity.class);
                getContext().startActivity(i);
            }
        });
        imgpicks = (ImageView)rootview.findViewById(R.id.img_picks);
        imgpicks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),EditorsPicksActivity.class);
                startActivity(i);
            }
        });
        imgft = (ImageView)rootview.findViewById(R.id.card_ft);
        imgft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ListDetailsActivity.class);
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading Restaurants");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.dismiss();
                i.putExtra("CAT_NAME","Food Trucks");
                startActivity(i);
            }
        });
        imgcafe = (ImageView)rootview.findViewById(R.id.card_cafe);
        imgcafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),ListDetailsActivity.class);
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading Restaurants");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.dismiss();
                i.putExtra("CAT_NAME","Cafes");
                startActivity(i);
            }
        });
        imgln = (ImageView)rootview.findViewById(R.id.card_ln);
        imgln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ListDetailsActivity.class);
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading Restaurants");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.dismiss();
                i.putExtra("CAT_NAME","Late Night");
                startActivity(i);
            }
        });
        imgbuffet = (ImageView)rootview.findViewById(R.id.card_buffet);
        imgbuffet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),ListDetailsActivity.class);
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading Restaurants");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.dismiss();
                i.putExtra("CAT_NAME","Buffets");
                startActivity(i);
            }
        });
        imgsf = (ImageView)rootview.findViewById(R.id.card_sf);
        imgsf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),ListDetailsActivity.class);
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading Restaurants");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.dismiss();
                i.putExtra("CAT_NAME","Street Food");
                startActivity(i);
            }
        });
        imgtiffin = (ImageView)rootview.findViewById(R.id.card_tiffin);
        imgtiffin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),ListDetailsActivity.class);
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Loading Restaurants");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.dismiss();
                i.putExtra("CAT_NAME","Tiffin Services");
                startActivity(i);
            }
        });

       /* FloatingActionButton fab = (FloatingActionButton)rootview. findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=an+address+city"));
                startActivity(intent);*/
               /* Intent i = new Intent(getActivity(),MapsActivity.class);
                startActivity(i);
*/
                /*Snackbar.make(view, "Search Restaurants", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            /*}
        });
*/

        return rootview;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");
    }


}

