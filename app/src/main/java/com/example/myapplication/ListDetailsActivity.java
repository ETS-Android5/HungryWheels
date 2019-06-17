package com.example.myapplication;
//customer side menu

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

public class ListDetailsActivity extends AppCompatActivity {
    ListView listview;
   // String strLang[] = {"TacoTruck", "Creambell", "Punjabi", "Waffle"};
   // int imgLang[] = {R.drawable.foodtruck, R.drawable.foodtruck, R.drawable.foodtruck, R.drawable.foodtruck};

    ArrayList<RestModel> dataModelArrayList;
    private DatabaseReference reference;
    private String strcatname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listdetails);
        listview = (ListView) findViewById(R.id.listview_ln);
        dataModelArrayList = new ArrayList<RestModel>();


        Intent intent = getIntent();
        strcatname = intent.getStringExtra("CAT_NAME");
        Log.e("LLLNNNN",""+strcatname);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Restaurant");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    RestModel restModel = dataSnapshot1.getValue(RestModel.class);

                    String strDatacatname = restModel.getCategory();
                    if (strcatname.equals(strDatacatname)) {

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


        customer_side_res customer_side_res = new customer_side_res(ListDetailsActivity.this, dataModelArrayList);
        listview.setAdapter(customer_side_res);


    }
}
