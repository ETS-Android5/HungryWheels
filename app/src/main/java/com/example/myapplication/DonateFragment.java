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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class DonateFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    EditText description;
    RadioGroup foodtype;
    RadioButton dry, gravy;
    CheckBox gms1, gms2, gms3;
    Button donate, camera;
    StorageReference mStorage;
    DatabaseReference reference;
    String uriImage;
    Uri imageUri;


    ArrayList<String> stringArrayList;

    ProgressDialog progressDialog;
    int radioFoodtypeId;
    private RadioButton radioFood;
    String desc;
    private String strDonate_key;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_donate, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Donate");
        mStorage = FirebaseStorage.getInstance().getReference("Donate");

        // SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
        //strUid = sharedPreferences.getString("USER_UID_KEY", "");
        //Log.e("ACCC", "uid" + strUid);

        stringArrayList = new ArrayList<String>();

        foodtype = (RadioGroup) rootview.findViewById(R.id.radiogrp);
        dry = (RadioButton) rootview.findViewById(R.id.radio_dry);
        gravy = (RadioButton) rootview.findViewById(R.id.radio_gravy);
        description = (EditText) rootview.findViewById(R.id.edt_description);
        gms1 = (CheckBox) rootview.findViewById(R.id.gms1);
        gms2 = (CheckBox) rootview.findViewById(R.id.gms2);
        gms3 = (CheckBox) rootview.findViewById(R.id.gms3);
        donate = (Button) rootview.findViewById(R.id.donate);
        camera = (Button) rootview.findViewById(R.id.btn_camera);


        progressDialog = new ProgressDialog(getContext());

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0);
                }
            }
            private void galleryIntent() {
                Log.d("gola", "entered here");
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FILE);
            }
        });

        donate.setOnClickListener(this);

        return rootview;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.donate:
                if (gms1.isChecked()) {
                    stringArrayList.add(gms1.getText().toString());
                }
                if (gms2.isChecked()) {
                    stringArrayList.add(gms2.getText().toString());
                }
                if (gms3.isChecked()) {
                    stringArrayList.add(gms3.getText().toString());
                }

                radioFoodtypeId = foodtype.getCheckedRadioButtonId();
                radioFood = foodtype.findViewById(radioFoodtypeId);
               String strFoodtype = radioFood.getText().toString();
               desc = description.getText().toString();


               // strRe_Key = reference.push().getKey();
                progressDialog.setMessage("Please Wait...");

                DonateModel donateModel = new DonateModel();
                strDonate_key = reference.push().getKey();
                donateModel.setDonate_id(strDonate_key);
                donateModel.setDescription(description);
                donateModel.setFoodType(strFoodtype);
                donateModel.setFood_img(uriImage);

                // reference.child(strDonate_key).setValue(donateModel);

                reference.child(strDonate_key).setValue(donateModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Thankyou for donation", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

//                Toast.makeText(this, "Restaurant has been registered", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(),HomeFragment.class));
                break;

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ) {
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


            final StorageReference filePath1 = mStorage.child(imageUri.getLastPathSegment() + ".jpg");
            filePath1.putFile(Uri.fromFile(destination)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
        }

        }
}
