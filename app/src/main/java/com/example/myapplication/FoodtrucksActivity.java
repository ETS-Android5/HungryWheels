package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.myapplication.MyBaseAdapater;

import java.util.ArrayList;

public class FoodtrucksActivity extends AppCompatActivity {
    ListView listview;
    String strLang[] = {"TacoTruck","Creambell","Punjabi","Waffle"};
    int imgLang[] = {R.drawable.foodtruck,R.drawable.foodtruck,R.drawable.foodtruck,R.drawable.foodtruck};

    ArrayList<DataModel> dataModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodtrucks);
        listview = (ListView)findViewById(R.id.listview);
        dataModelArrayList = new ArrayList<DataModel>();
        for (int i=0; i< strLang.length; i++)
        {
            DataModel dataModel = new DataModel(strLang[i],imgLang[i]);
            dataModelArrayList.add(dataModel);
            MyGridAdapter myGridAdapter = new MyGridAdapter(this,dataModelArrayList);
//            MyBaseAdapater myBaseAdapter = new MyBaseAdapater(this,dataModelArrayList);
            listview.setAdapter(myGridAdapter);
        }


    }
}

