package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class add_menu_res extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    EditText foodName,foodPrice,description,quantity;
    Button foodImage,submit;
    DatabaseReference reference2;
    private String name,price,foodDescription,foodQuantity,strRefood_Key;
    ProgressDialog progressDialog;
    String uriImage;
    Uri imageUri;

    StorageReference mStorage;
    String strRe_Key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_res);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference2 = database.getReference("Restaurant_menu");
        mStorage = FirebaseStorage.getInstance().getReference("Restuarant_menu");

        Intent i = getIntent();
        strRe_Key = i.getStringExtra("RES_ID");

        foodName = findViewById(R.id.food_name);
        foodPrice = findViewById(R.id.price);
        description = findViewById(R.id.description);
        quantity = findViewById(R.id.quantity);
        foodImage = findViewById(R.id.upload_image1);
        submit = findViewById(R.id.submit1);
        progressDialog = new ProgressDialog(this);

        foodImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(add_menu_res.this);
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
                                         }

                                         private void cameraIntent() {
                                             if (ContextCompat.checkSelfPermission(add_menu_res.this,
                                                     Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                                                 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                 startActivityForResult(intent, REQUEST_CAMERA);
                                             } else {
                                                 ActivityCompat.requestPermissions(add_menu_res.this, new String[]{Manifest.permission.CAMERA}, 0);
                                             }
                                         }
                                         private void galleryIntent() {
                                             Log.d("gola", "entered here");
                                             Intent intent = new Intent(Intent.ACTION_PICK);
                                             intent.setType("image/*");
                                             startActivityForResult(intent, SELECT_FILE);
                                         }
                                     });



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = foodName.getText().toString();
                price = foodPrice.getText().toString();
                foodDescription = description.getText().toString();
                foodQuantity = quantity.getText().toString();

               // RestModel restModel = new RestModel();

                progressDialog.setMessage("Please Wait...");
              //  progressDialog.show();

                Res_MenuModel res_menuModel = new Res_MenuModel();
                strRefood_Key = reference2.push().getKey();
                res_menuModel.setMenu_id(strRefood_Key);
                res_menuModel.setFood_name(name);
                res_menuModel.setFood_price(price);
                res_menuModel.setFood_des(foodDescription);
                res_menuModel.setFood_qua(foodQuantity);

                res_menuModel.setFood_img(uriImage);

              //  reference2.child(strMenu_key).setValue(res_menuModel);

                reference2.child(strRefood_Key).setValue(res_menuModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(add_menu_res.this, "Menu is added....", Toast.LENGTH_SHORT).show();
                            // startActivity(new Intent(add_menu_res.this, MyRestaurant.class));
                            progressDialog.dismiss();
                        }else {
                            Toast.makeText(add_menu_res.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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














     /*   if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            final Uri imageUri1 = data.getData();

            Log.e("image","****"+imageUri1);

            progressDialog.setMessage("Uploading...");
            progressDialog.show();
//            imageHoldUri = result.getUri();


            final StorageReference filePath2 = mStorage1.child(imageUri1.getLastPathSegment()+".jpg");
            filePath2.putFile(imageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    filePath2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            uriImage1 = uri.toString();

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

            final Uri imageUri1 = data.getData();

 //           progressDialog.setMessage("Uploading...");
   //         progressDialog.show();
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



            final StorageReference filePath2 = mStorage1.child(imageUri1.getLastPathSegment()+".jpg");
            filePath2.putFile( Uri.fromFile(destination)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filePath2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            uriImage1 = uri.toString();
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
*/
//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(this);

        }
    }
}


