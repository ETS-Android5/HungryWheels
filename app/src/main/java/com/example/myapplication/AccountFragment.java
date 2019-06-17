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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment implements View.OnClickListener {

    DataSnapshot dataSnapshot;
    FirebaseAuth mAuth;
    DatabaseReference reference;

    String userId;
    TextView user_email, user_phoneno;
    ProgressBar pd;
    ImageView ownerimageprofileview;
    ImageView btnownerprofilepic;
    //ProgressDialog progressDialog;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    //  Uri imageHoldUri = null;
    StorageReference mStorage;
    String uriimg;
    private Uri file1 = null;
    private String strUid;
    private Toolbar supportActionBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_account,container,false);
        Toolbar toolbar1 = (Toolbar) rootview.findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar1);


        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference("Register");
        //  progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Register").child(userId);

        user_email = rootview.findViewById(R.id.email);
        user_phoneno = rootview.findViewById(R.id.contact);
        //  pd =rootview. findViewById(R.id.progress);
        ownerimageprofileview = (ImageView) rootview.findViewById(R.id.profile_image);
        btnownerprofilepic = (ImageView) rootview.findViewById(R.id.btn_owner_profile_pic);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) rootview.findViewById(R.id.collapsing_toolbar);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
        strUid = sharedPreferences.getString("USER_UID_KEY", "");

        String strfn = sharedPreferences.getString("USER_FN_KEY", "");
        String strln = sharedPreferences.getString("USER_LN_KEY", "");
        String stremail = sharedPreferences.getString("USER_EMAIL_KEY", "");
        String strphn = sharedPreferences.getString("USER_PHONE_KEY", "");

        collapsingToolbarLayout.setTitle(strfn + " " + strln);

        user_email.setText(stremail);
        user_phoneno.setText(strphn);


        btnownerprofilepic.setOnClickListener(this);

        return rootview;
    }
    private void setSupportActionBar(Toolbar toolbar1) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Account");
    }



    //reference.addValueEventListener(new ValueEventListener() {

    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String email = dataSnapshot.child("email").getValue().toString();
        String phone_no = dataSnapshot.child("mobileno").getValue().toString();
        String imagepath = dataSnapshot.child("imagepath").getValue().toString();

        if (!imagepath.equals("")) {

            Picasso.get().load(imagepath).placeholder(R.drawable.ic_person).centerCrop().fit().into(ownerimageprofileview);

        }
        user_email.setText(email);
        user_phoneno.setText(phone_no);
        pd.setVisibility(View.GONE);
    }



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

        if (ContextCompat.checkSelfPermission(getContext(),
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //SAVE URI FROM GALLERY
        if (requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            ProgressDialog progressDialog = new ProgressDialog(getActivity());

            progressDialog.setMessage("Uploading...");
            progressDialog.show();
//            imageHoldUri = result.getUri();


            final StorageReference filePath1 = mStorage.child(mAuth.getCurrentUser().getUid() + ".jpg");
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
                                                Toast.makeText(getActivity(), "Image save succefully in database", Toast.LENGTH_SHORT).show();

                                                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                                progressDialog.dismiss();
                                            } else {
                                                Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
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

                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage(((int) progress) + "% Uploading..");
                }
            });


//            CropImage.activity(imageUri)
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1,1)
//                    .start(this);

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            //SAVE URI FROM CAMERA

//            Uri imageHoldUri = data.getData();

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
            ownerimageprofileview.setImageBitmap(thumbnail);


            final StorageReference filePath1 = mStorage.child(mAuth.getCurrentUser().getUid() + ".jpg");
            filePath1.putFile(Uri.fromFile(destination)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                                                Toast.makeText(getActivity(), "Image save succefully in database", Toast.LENGTH_SHORT).show();

                                                ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                                progressDialog.dismiss();
                                            } else {
                                                Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
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

                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage(((int) progress) + "% Uploading..");
                }
            });
        }


    }
}


