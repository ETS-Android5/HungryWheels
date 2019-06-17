package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import static com.example.myapplication.R.id.nav_home;
import static com.example.myapplication.R.id.nav_orders;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Object HomeFragment;
    private NavigationView navigationView;
    FirebaseAuth auth;
    private ResideMenu resideMenu;
    private Context mContext;
    private ResideMenuItem itemHome,itemDonate,itemOrder,itemAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;
        setUpMenu();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NavigationActivity.this,MapsActivity.class);
                startActivity(i);

                /*Snackbar.make(view, "Search Restaurants", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
        final String strUid = sharedPreferences.getString("USER_UID_KEY", "");
        final String strfn = sharedPreferences.getString("USER_FN_KEY", "");
        final String strln = sharedPreferences.getString("USER_LN_KEY", "");


        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.nav_name);
        nav_user.setText(strfn + "   " + strln);
        loadHomepage();
    }

    private void loadHomepage() {
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.add(R.id.frame, homeFragment);
        ft.replace(R.id.frame,homeFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(NavigationActivity.this,SettingsActivity.class);
            startActivity(i);

            return true;
        }else if (id == R.id.action_drawer_cart) {

            Intent i = new Intent(NavigationActivity.this, CartActivity.class);
            startActivity(i);

            return true;
        }

            return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_donate) {
            fragment = new DonateFragment();
        } else if (id == nav_orders) {
            fragment = new OrdersFragment();
        } else if (id == R.id.nav_feedback) {
            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.theopen.android"));
            startActivity(i);
           // fragment = new FeedbackFragment();
        } else if (id == R.id.nav_aboutus) {
            fragment = new AboutusFragment();
        } else if (id == R.id.nav_account) {
            fragment = new AccountFragment();
        } else if (id == R.id.nav_logout) {
//            auth.signOut();
            SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USER_UID_KEY", null);
            editor.commit();
            Intent i = new Intent(NavigationActivity.this, loginActivity.class);
            startActivity(i);
            finish();
        }


        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame, fragment);
            ft.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(this);


        resideMenu.setBackground(R.drawable.back_img);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        itemHome= new ResideMenuItem(this,R.drawable.ic_person_black_24dp, "Home");

        itemDonate = new ResideMenuItem(this,R.drawable.ic_assignment_black_24dp, "Donate");
        itemOrder = new ResideMenuItem(this,R.drawable.ic_dining, "Orders");
        itemAccount = new ResideMenuItem(this,R.drawable.ic_dining, "Account");

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemDonate, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemOrder, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemAccount, ResideMenu.DIRECTION_LEFT);


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return resideMenu.dispatchTouchEvent(ev);
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

            Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }


}