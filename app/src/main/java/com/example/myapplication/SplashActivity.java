package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.BusinessOwnerActivity;
import com.example.myapplication.NavigationActivity;
import com.example.myapplication.R;
import com.example.myapplication.loginActivity;

public class SplashActivity extends AppCompatActivity {
    int splashtime = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
        final String strUid = sharedPreferences.getString("USER_UID_KEY", "");
        final String strRolle = sharedPreferences.getString("USER_ROLLETYPE", "");


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (!strUid.equals("") && !strUid.isEmpty()){

                    if (strRolle.equals("Business owner")){

                        Intent i = new Intent(SplashActivity.this, BusinessOwnerActivity.class);
                        startActivity(i);
                        finish();
                    }else {

                        Intent i = new Intent(SplashActivity.this, NavigationActivity.class);
                        startActivity(i);
                        finish();
                    }
                }else {
                    Intent i = new Intent(SplashActivity.this, loginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, splashtime);
    }
}
