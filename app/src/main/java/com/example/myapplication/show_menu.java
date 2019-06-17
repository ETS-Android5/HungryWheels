package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class show_menu extends AppCompatActivity{

    ListView listview;
    // String strLang[] = {"TacoTruck", "Creambell", "Punjabi", "Waffle"};
    // int imgLang[] = {R.drawable.foodtruck, R.drawable.foodtruck, R.drawable.foodtruck, R.drawable.foodtruck};

    ArrayList<Res_MenuModel> dataModelArrayList;
    private DatabaseReference reference;
    private String strcatname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_menu);
        listview = (ListView) findViewById(R.id.listview_menu);
        dataModelArrayList = new ArrayList<Res_MenuModel>();


        Intent intent = getIntent();
        strcatname = intent.getStringExtra("RES_ID");
        Log.e("LLLNNNN",""+strcatname);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Restaurant_menu");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Res_MenuModel res_menuModel = dataSnapshot1.getValue(Res_MenuModel.class);

                    String strDatacatname = res_menuModel.getRes_id();
                    if (strcatname.equals(strDatacatname)) {

                        dataModelArrayList.add(res_menuModel);
                        setdata(dataModelArrayList);

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setdata(ArrayList<Res_MenuModel> dataModelArrayList) {


        menu_adapter menu_adapter = new menu_adapter(show_menu.this, dataModelArrayList);
        listview.setAdapter(menu_adapter);


    }
}