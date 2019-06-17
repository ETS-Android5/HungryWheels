package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

public class NinisKitchen extends AppCompatActivity {
    GridView gridView;

    String strLang[] = {"Cheese Pizza","Veg Pizza","Burger","Cheese Burger","Fries","Cheese Fries"};
    int imgLang[] = {R.drawable.pizza1,R.drawable.pizza2
            ,R.drawable.fries1,R.drawable.fries2,
            R.drawable.burger1,R.drawable.burger2};

    ArrayList<DataModel> dataModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninis_kitchen);

        gridView = (GridView) findViewById(R.id.gridview2);

        dataModelArrayList = new ArrayList<DataModel>();
        for (int i = 0; i<strLang.length; i++){

            DataModel dataModel = new DataModel(strLang[i], imgLang[i]);
            dataModelArrayList.add(dataModel);

        }

        MyGridAdapter myGridAdapter =new MyGridAdapter(this,dataModelArrayList);
        gridView.setAdapter(myGridAdapter);

    }
}
