package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SearchView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    //ListView listView;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (SearchView) findViewById(R.id.searchview);
        recyclerView = (RecyclerView)findViewById(R.id.recycleview);
       // listView = (ListView) findViewById(R.id.lv_search);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference foodRef = rootRef.child("Restaurant_menu");

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}