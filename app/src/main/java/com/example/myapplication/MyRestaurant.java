package com.example.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRestaurant extends AppCompatActivity {

    ListView listviewRes;
    // String strLang[] = {"TacoTruck", "Creambell", "Punjabi", "Waffle"};
    // int imgLang[] = {R.drawable.foodtruck, R.drawable.foodtruck, R.drawable.foodtruck, R.drawable.foodtruck};

    ArrayList<RestModel> dataModelArrayList;
    private DatabaseReference reference;



    private String strcurrentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_restaurant);
        listviewRes = (ListView) findViewById(R.id.listview_res);
        dataModelArrayList = new ArrayList<RestModel>();


        Intent intent = getIntent();
        strcurrentUid = intent.getStringExtra("CUR_UID");
        Toast.makeText(this, strcurrentUid, Toast.LENGTH_SHORT).show();
        Log.e("LLLNNNN", "" + strcurrentUid);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Restaurant");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    RestModel restModel = dataSnapshot1.getValue(RestModel.class);

                    String strDatacatname = restModel.getStrUserUid();
                    if (strcurrentUid.equals(strDatacatname)) {

                        dataModelArrayList.add(restModel);
                    }

                }
                setdata(dataModelArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setdata(ArrayList<RestModel> dataModelArrayList) {


        MyBaseAdapater myBaseAdapter = new MyBaseAdapater(MyRestaurant.this, dataModelArrayList);
        listviewRes.setAdapter(myBaseAdapter);

    }
}
