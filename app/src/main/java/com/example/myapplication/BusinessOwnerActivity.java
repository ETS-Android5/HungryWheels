package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class BusinessOwnerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    FloatingActionButton floatingActionButton;
    FirebaseAuth mAuth;
    FirebaseDatabase mUserDatabase;
    DatabaseReference reference;
    String userId;
    TextView user_email,user_phoneno;
    ProgressBar pd;
    ImageView ownerimageprofileview;
    ImageView btnownerprofilepic;
    private ProgressDialog progressDialog;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    Uri imageHoldUri = null;
    StorageReference mStorage;
    String uriimg;
    private Uri file1 = null;
    private ResideMenu resideMenu;
    private Context mContext;
    private ResideMenuItem itemLogout,itemProfile,itemMenu,itemOrder;
    private ResideMenuItem itemShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_owner);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Register").child(userId);
        user_email = findViewById(R.id.email);
        user_phoneno = findViewById(R.id.contact);
        pd = findViewById(R.id.progress);
        ownerimageprofileview = (ImageView) findViewById(R.id.profile_image);
        btnownerprofilepic = (ImageView) findViewById(R.id.btn_owner_profile_pic);
        mAuth = FirebaseAuth .getInstance();
        mStorage = FirebaseStorage.getInstance().getReference("Register");
        progressDialog = new ProgressDialog(this);

        mContext = this;
        setUpMenu();

        Toast.makeText(mContext, userId, Toast.LENGTH_SHORT).show();

        btnownerprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(BusinessOwnerActivity.this);
                builder.setTitle("Add Photo!");

                //SET ITEMS AND THERE LISTENERS
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Take Photo")) {
                            cameraIntent();
                        } else if (items[item].equals("Choose from Library")) {
                            galleryIntent();
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent,GALLERY_INTENT);

            }


            private void cameraIntent() {

                if (ContextCompat.checkSelfPermission(BusinessOwnerActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    ActivityCompat.requestPermissions(BusinessOwnerActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
                }
            }
            private void galleryIntent() {

                //CHOOSE IMAGE FROM GALLERY
                Log.d("gola", "entered here");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            }
        });

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        floatingActionButton = findViewById(R.id.add_bussiness);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BusinessOwnerActivity.this,AddBusinessActivity.class));
            }
        });

        DrawerLayout drawer1 = (DrawerLayout) findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer1, toolbar1, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer1.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.bussiness_navigation);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
        final String strUid = sharedPreferences.getString("USER_UID_KEY", "");
        final String strfn = sharedPreferences.getString("USER_FN_KEY", "");
        final String strln = sharedPreferences.getString("USER_LN_KEY", "");

        collapsingToolbarLayout.setTitle(strfn+" "+strln);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = dataSnapshot.child("email").getValue().toString();
                String phone_no = dataSnapshot.child("mobileno").getValue().toString();
               String imagepath = dataSnapshot.child("imagepath").getValue().toString();

               if (!imagepath.equals("")){

                   Picasso.get().load(imagepath).placeholder(R.drawable.ic_account_circle_black_24dp).centerCrop().fit().into(ownerimageprofileview);

               }
                   user_email.setText(email);
                user_phoneno.setText(phone_no);
                pd.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.nav_name);
        nav_user.setText(
                strfn + "   " + strln);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer1 = (DrawerLayout) findViewById(R.id.drawer_layout1);
        if (drawer1.isDrawerOpen(GravityCompat.START)) {
            drawer1.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//        int id = menuItem.getItemId();

        switch (menuItem.getItemId()){

            case R.id.Business_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Business_restaurant:
                Intent i = new Intent(BusinessOwnerActivity.this,MyRestaurant.class);
                i.putExtra("CUR_UID",userId);
                startActivity(i);
                break;
            case R.id.Business_orders:
                Toast.makeText(this, "order", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Logout:
                SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("USER_ROLLETYPE",null);
                editor.commit();
                startActivity(new Intent(BusinessOwnerActivity.this,loginActivity.class));
                finish();
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
        }

//        if (id == R.id.Business_profile) {
//            Toast.makeText(this, "Business Home Activity", Toast.LENGTH_SHORT).show();
//            return true;
//        } else if (id == R.id.Business_menu) {
//            Toast.makeText(this, "Business Menu activity", Toast.LENGTH_SHORT).show();
//            return true;
//        } else if (id == R.id.Business_orders) {
//            Toast.makeText(this, "Business Order activity", Toast.LENGTH_SHORT).show();
//            return true;
//        }else if (id==R.id.Logout){
//            SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("USER_ROLLETYPE",null);
//            editor.commit();
//            startActivity(new Intent(BusinessOwnerActivity.this,loginActivity.class));
//            finish();
//        }

        DrawerLayout drawer1 = (DrawerLayout) findViewById(R.id.drawer_layout1);
                 drawer1.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //SAVE URI FROM GALLERY
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            final Uri imageUri = data.getData();

            progressDialog.setMessage("Uploading...");
            progressDialog.show();
//            imageHoldUri = result.getUri();


            final StorageReference filePath1 = mStorage.child(mAuth.getCurrentUser().getUid()+".jpg");
            filePath1.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            uriimg = uri.toString();

                            reference.child("imagepath")
                                    .setValue(uri.toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(BusinessOwnerActivity.this, "Image save succefully in database", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            } else {
                                                Toast.makeText(BusinessOwnerActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progress) + "% Uploading..");
                }
            });

//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(this);

        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
            //SAVE URI FROM CAMERA

//            Uri imageHoldUri = data.getData();

            progressDialog.setMessage("Uploading...");
            progressDialog.show();
//            imageHoldUri = result.getUri();


            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
          //  ownerimageprofileview.setImageBitmap(thumbnail);





            final StorageReference filePath1 = mStorage.child(mAuth.getCurrentUser().getUid()+".jpg");
            filePath1.putFile( Uri.fromFile(destination)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            uriimg = uri.toString();

                            reference.child("imagepath")
                                    .setValue(uriimg)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(BusinessOwnerActivity.this, "Image save succefully in database", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            } else {
                                                Toast.makeText(BusinessOwnerActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progress) + "% Uploading..");
                }
            });

//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(this);

        }
    }

    private void setUpMenu() {

        resideMenu = new ResideMenu(this);


       // resideMenu.setBackground(R.drawable.

        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        itemProfile = new ResideMenuItem(this,R.drawable.ic_person_black_24dp, "Profile");
        itemMenu = new ResideMenuItem(this,R.drawable.ic_assignment_black_24dp, "My Menu");
        itemOrder = new ResideMenuItem(this,R.drawable.ic_dining, "My order");
        itemLogout = new ResideMenuItem(this,R.drawable.ic_dining, "Logout");

        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemMenu, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemOrder, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemLogout, ResideMenu.DIRECTION_LEFT);

        itemProfile.setOnClickListener(this);
        itemMenu.setOnClickListener(this);
        itemOrder.setOnClickListener(this);
        itemLogout.setOnClickListener(this);



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

    @Override
    public void onClick(View view) {

        if (view == itemProfile){
            Toast.makeText(mContext, "Profile", Toast.LENGTH_SHORT).show();
        } else if (view == itemMenu){
            Toast.makeText(mContext, "Menu", Toast.LENGTH_SHORT).show();
        } else if (view == itemOrder){
            Toast.makeText(mContext, "oreder", Toast.LENGTH_SHORT).show();
        } else if (view == itemLogout){
            Toast.makeText(mContext, "LOGOUT", Toast.LENGTH_SHORT).show();
        }

    }
}
