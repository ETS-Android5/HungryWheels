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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddBusinessActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference reference;

    RadioGroup service, owner_manager_group, opening_status, payment,foodtype;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    LinearLayout check_layout;
    EditText restaurant_name, city_name, restaurant_no, address, from_time, to_time, edtmoney,food_name,price,description,quantity;
    Spinner spinner;
    Button submit,upload_image;
    ProgressDialog progressDialog;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    FirebaseAuth mAuth;
    String uriImage;
    StorageReference mStorage;
    RestModel resmodel;
    String strRe_Key;


    String[] strLang = {"Select Category", "Food Trucks", "Cafes", "Late Night", "Buffets", "Street Food", "Tiffin Services"};

    private String resname;
    private String cityname;
    private String resnumber;
    private String resaddress;
    private String fromtime;
    private String totime;
    private String money;
    private String strUid;

    ArrayList<String> stringArrayList;
    private int radioOwnerId;
    private int radioServiceId;
    private int radioPaymentId;
    private int radioOpeningId;
    private int radioFoodtypeId;
    private RadioButton radioOpening;
    private RadioButton radioOwner;
    private RadioButton radioService;
    private RadioButton radioPayment;
    private RadioButton radioFoodtype;
    private String strdata;
    private DatabaseReference reference1;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);

        /*FirebaseApp.initializeApp(this);*/
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Restaurant");
        reference1 = database.getReference("Restaurant_menu");


        SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
        strUid = sharedPreferences.getString("USER_UID_KEY", "");
        Log.e("ACCC", "uid" + strUid);

        stringArrayList = new ArrayList<String>();
        service = findViewById(R.id.service);
        // seating = findViewById(R.id.seating);.
        //noseating = findViewById(R.id.noseating);
        // indoor = findViewById(R.id.indoor_seating);
        //outdoor = findViewById(R.id.outdoor_seating);
        check_layout = findViewById(R.id.checkbox_layout);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        restaurant_name = findViewById(R.id.restaurant_name);
        city_name = findViewById(R.id.city_name);
        restaurant_no = findViewById(R.id.restaurant_no);
        address = findViewById(R.id.address);
        spinner = findViewById(R.id.categories);
        from_time = findViewById(R.id.from_time);
        to_time = findViewById(R.id.to_time);
        edtmoney = findViewById(R.id.edt_money);
        // add_time = findViewById(R.id.add_time);
        owner_manager_group = findViewById(R.id.owner_manager_group);
        // opening_soon = findViewById(R.id.opening_soon);
        opening_status = findViewById(R.id.opening_status);
        payment = findViewById(R.id.payment);
        foodtype = findViewById(R.id.Foodtype);
        //owner_manager = findViewById(R.id.owner_manager);
        //not_owner_manager = findViewById(R.id.not_owner_manager);
        //already_open = findViewById(R.id.already_open);
        //card_cash = findViewById(R.id.card_cash);
        //cash_only = findViewById(R.id.cash_only);
        submit = findViewById(R.id.submit);
        food_name = findViewById(R.id.food_name);
        price = findViewById(R.id.price);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);
        upload_image = findViewById(R.id.upload_image);
        mAuth = FirebaseAuth .getInstance();
        mStorage = FirebaseStorage.getInstance().getReference("Restuarant");
        progressDialog = new ProgressDialog(this);
        resmodel = new RestModel();
        submit.setOnClickListener(this);

        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AddBusinessActivity.this);
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

                if (ContextCompat.checkSelfPermission(AddBusinessActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    ActivityCompat.requestPermissions(AddBusinessActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strLang);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    strdata = parent.getItemAtPosition(position).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }
/*

    private boolean validate() {
        if (resname.isEmpty()) {
            restaurant_name.setError("Enter Restaurant Name");
            return false;
        } else if (cityname.isEmpty()) {
            city_name.setError("Enter City Name");
            return false;
        } else if (resnumber.isEmpty()) {
            restaurant_no.setError("Enter Restaurant No.");
            return false;
        } else if (!owner_manager.isChecked() && !not_owner_manager.isChecked()) {
            Toast.makeText(AddRestaurant.this, "Please select a type", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!already_open.isChecked() && !opening_soon.isChecked()) {
            Toast.makeText(AddRestaurant.this, "Please Select opening status", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!seating.isChecked() && !noseating.isChecked()) {
            Toast.makeText(AddRestaurant.this, "Please select services", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!card_cash.isChecked() && !cash_only.isChecked()) {
            Toast.makeText(AddRestaurant.this, "Please select Payment method", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
*/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.submit:

                if (monday.isChecked()) {
                    stringArrayList.add(monday.getText().toString());
                }
                if (tuesday.isChecked()) {
                    stringArrayList.add(tuesday.getText().toString());
                }
                if (wednesday.isChecked()) {
                    stringArrayList.add(wednesday.getText().toString());
                }
                if (thursday.isChecked()) {
                    stringArrayList.add(thursday.getText().toString());
                }
                if (friday.isChecked()) {
                    stringArrayList.add(friday.getText().toString());
                }
                if (saturday.isChecked()) {
                    stringArrayList.add(saturday.getText().toString());
                }
                if (sunday.isChecked()) {
                    stringArrayList.add(sunday.getText().toString());
                }
              /*  if (resname.equals("") && resname.isEmpty()) {

                    restaurant_name.setError("Please enter valid name");
                }else if (cityname.equals("") && cityname.isEmpty()) {

                city_name.setError("Please enter valid name");
                }else if (resnumber.equals("") && resnumber.isEmpty()) {

                restaurant_no.setError("Please enter valid number");
                }else if (resaddress.equals("") && resaddress.isEmpty()) {

                address.setError("Please enter valid address");
                }else if (fromtime.equals("") && fromtime.isEmpty()) {

                from_time.setError("Please enter valid time");
                }else if (totime.equals("") && totime.isEmpty()) {

                to_time.setError("Please enter valid time");
                }else if (money.equals("") && money.isEmpty()) {

                edtmoney.setError("Please enter valid amount");
                }

*/

                radioOwnerId = owner_manager_group.getCheckedRadioButtonId();
                radioServiceId = service.getCheckedRadioButtonId();
                radioPaymentId = payment.getCheckedRadioButtonId();
                radioOpeningId = opening_status.getCheckedRadioButtonId();
                radioFoodtypeId = foodtype.getCheckedRadioButtonId();
                radioOwner = (RadioButton) findViewById(radioOwnerId);
                radioService = (RadioButton) findViewById(radioServiceId);
                radioPayment = (RadioButton) findViewById(radioPaymentId);
                radioOpening = (RadioButton) findViewById(radioOpeningId);
                radioFoodtype = (RadioButton)findViewById(radioFoodtypeId);

               /* resname = sharedPreferences.getString("USER_RESNAME_KEY","");
                cityname = sharedPreferences.getString("USER_CITY_KEY","");
                owner = sharedPreferences.getString("USER_OWNER_KEY","");
                notowner = sharedPreferences.getString("USER_NOTOWNER_KEY","");
                resnumber = sharedPreferences.getString("USER_RESNUMB_KEY","");
                placeopen = sharedPreferences.getString("USER_PLACEOPEN_KEY","");
                placeopensoon = sharedPreferences.getString("USER_NOTOPEN_KEY","");
                resaddress = sharedPreferences.getString("USER_RESADD_KEY","");
                seat = sharedPreferences.getString("USER_SEAT_KEY","");
                noseat = sharedPreferences.getString("USER_NOSEAT_KEY","");
                cashandcard = sharedPreferences.getString("USER_CASHCARD_KEY","");
                cashonly = sharedPreferences.getString("USER_CASHONLY_KEY","");
                category = sharedPreferences.getString("USER_CATEGORY_KEY","");
                mon = sharedPreferences.getString("USER_MON_KEY","");
                tue = sharedPreferences.getString("USER_TUE_KEY","");
                wed = sharedPreferences.getString("USER_WED_KEY","");
                thu = sharedPreferences.getString("USER_THU_KEY","");
                fri = sharedPreferences.getString("USER_FRI_KEY","");
                sat = sharedPreferences.getString("USER_SAT_KEY","");
                sun = sharedPreferences.getString("USER_SUN_KEY","");
                fromtime = sharedPreferences.getString("USER_FROM_KEY","");
                totime = sharedPreferences.getString("USER_TO_KEY","");
                btntime = sharedPreferences.getString("USER_TIME_KEY","");


*/



                resname = restaurant_name.getText().toString();
                cityname = city_name.getText().toString();
                // owner = owner_manager.getText().toString();
                //notowner = not_owner_manager.getText().toString();
                resnumber = restaurant_no.getText().toString();
                //open = already_open.getText().toString();
                //opensoon = opening_soon.getText().toString();
                resaddress = address.getText().toString();
                money = edtmoney.getText().toString();
                //seat = seating.getText().toString();
                //noseat= noseating.getText().toString();
                //cashcard = card_cash.getText().toString();
                //cashonly = cash_only.getText().toString();
                fromtime = from_time.getText().toString();
                totime = to_time.getText().toString();
                String strowner = radioOwner.getText().toString();
                String strservice = radioService.getText().toString();
                String strpayment = radioPayment.getText().toString();
                String stropening = radioOpening.getText().toString();
                String strfoodtype = radioFoodtype.getText().toString();

                String foodName = food_name.getText().toString();
                String foodPrice = price.getText().toString();
                String foodDescription = description.getText().toString();
                String foodQuantity = quantity.getText().toString();


                strRe_Key = reference.push().getKey();
                progressDialog.setMessage("Please Wait...");

                resmodel.setResname(resname);
                resmodel.setCityname(cityname);
                resmodel.setMoney(money);
                resmodel.setOwner(strowner);
                // resmodel.setNotowner(notowner);
                resmodel.setResnumber(resnumber);
                resmodel.setPlaceopen(stropening);
                //resmodel.setPlaceopensoon(opensoon);
                resmodel.setResaddress(resaddress);
                resmodel.setSeat(strservice);
                resmodel.setCategory(strdata);
                //resmodel.setNoseat(noseat);
                resmodel.setPayment(strpayment);
                // resmodel.setCashonly(cashonly);
                resmodel.setFoodType(strfoodtype);
                resmodel.setStringArrayList(stringArrayList);
                resmodel.setFromtime(fromtime);
                resmodel.setTotime(totime);
                resmodel.setRes_key(strRe_Key);
                resmodel.setStrUserUid(strUid);
               /* resmodel.setFoodName(foodName);
                resmodel.setFoodPrice(foodPrice);
                resmodel.setFoodDescription(foodDescription);
                resmodel.setFoodQuantity(foodQuantity);
               */
                resmodel.setResImage(uriImage);

                Res_MenuModel res_menuModel = new Res_MenuModel();
                String strMenu_key = reference1.push().getKey();
                res_menuModel.setMenu_id(strMenu_key);
                res_menuModel.setRes_id(strRe_Key);
                res_menuModel.setFood_name(foodName);
                res_menuModel.setFood_des(foodDescription);
                res_menuModel.setFood_price(foodPrice);
                res_menuModel.setFood_qua(foodQuantity);
                reference1.child(strMenu_key).setValue(res_menuModel);



                reference.child(strRe_Key).setValue(resmodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddBusinessActivity.this, "Restaurant has been registered", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(AddBusinessActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//                Toast.makeText(this, "Restaurant has been registered", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddBusinessActivity.this,BusinessOwnerActivity.class));
                finish();
                break;

        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = data.getData();

        //SAVE URI FROM GALLERY
        if(requestCode == SELECT_FILE)
        {

            progressDialog.setMessage("Uploading...");
            progressDialog.show();
//            imageHoldUri = result.getUri();


            final StorageReference filePath1 = mStorage.child(imageUri.getLastPathSegment()+".jpg");
            filePath1.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            uriImage = uri.toString();

                            progressDialog.dismiss();


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

            final Uri imageUri = data.getData();

//            progressDialog.setMessage("Uploading...");
//            progressDialog.show();
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





            final StorageReference filePath1 = mStorage.child(imageUri.getLastPathSegment()+".jpg");
            filePath1.putFile( Uri.fromFile(destination)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            uriImage = uri.toString();
                            progressDialog.dismiss();
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
}
