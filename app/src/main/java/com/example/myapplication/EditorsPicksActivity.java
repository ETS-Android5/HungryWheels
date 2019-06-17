package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EditorsPicksActivity extends AppCompatActivity {
    ImageView img1;
    ImageView img2;
    ImageView img3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editors_picks);

        img1 = (ImageView)findViewById(R.id.hardrock);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditorsPicksActivity.this,Hardrockcafe.class);
                startActivity(i);
            }
        });

        img2 = (ImageView)findViewById(R.id.choclateroom);

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditorsPicksActivity.this,Chocolateroom.class);
                startActivity(i);
            }
        });
        img3 = (ImageView)findViewById(R.id.niniskitchen);

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditorsPicksActivity.this,NinisKitchen.class);
                startActivity(i);
            }
        });
    }
}
