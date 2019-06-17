package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnRegister;
    EditText fname, lname, email, password, mobileno;
    RadioGroup signuptype;
    RadioButton rdbtn1,rdbtn2,radioButton;
    int radioButtonId;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private String stremail;
    private String strfn;
    private String strln;
    private String strmb;
    private String pass;
    private String rdbtn;
    StorageReference mStorage;
    private ProgressDialog pd;
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;
    Uri imageHoldUri = null;
    TextView uploadPhoto;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Write a message to the database
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Register");
        mStorage = FirebaseStorage.getInstance().getReference("Register");
        pd = new ProgressDialog(this);


        btnRegister = (Button) findViewById(R.id.btn_signup);
        btnRegister.setOnClickListener(this);
        fname = (EditText) findViewById(R.id.edt_Fname);
        lname = (EditText) findViewById(R.id.edt_Lname);
        email = (EditText) findViewById(R.id.edt_email);
        password = (EditText) findViewById(R.id.edt_password);
        mobileno = (EditText) findViewById(R.id.edt_number);
        signuptype = findViewById(R.id.signup_type);
        rdbtn1 = findViewById(R.id.rdbtn1);
        rdbtn2 = findViewById(R.id.rdbtn2);


    }

    public void checked(View v){
        radioButtonId = signuptype.getCheckedRadioButtonId();
        radioButton = findViewById(radioButtonId);
        rdbtn = radioButton.getText().toString();

        Toast.makeText(this, "Your choice is..."+rdbtn, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_signup:

                stremail = email.getText().toString();
                strfn = fname.getText().toString();
                strln = lname.getText().toString();
                strmb = mobileno.getText().toString();
                pass = password.getText().toString();

                if (strfn.equals("") && strfn.isEmpty()) {

                    fname.setError("Please enter valid name");
                } else if (strfn.length() < 2) {
                    fname.setError("Please enter valid name ");

                } else if (strln.equals("") && strln.isEmpty()) {

                    lname.setError("Please enter valid name");
                } else if (strln.length() < 2) {
                    lname.setError("Please enter valid name ");

                  }else if (strmb.equals("") && strmb.isEmpty()) {

                        mobileno.setError("Please enter valid number");
                    } else if (strmb.length() != 10) {
                        mobileno.setError("Please enter valid number");


                    } else if (!isValidEmail(stremail)) {
                        email.setError("Invalid Email");
                    } else if (!isValidPassword(pass)) {
                        password.setError("Invalid Password");
                    } else if (!rdbtn1.isChecked() && !rdbtn2.isChecked()) {
                    Toast.makeText(this, "Please Select SignUp Type...", Toast.LENGTH_SHORT).show();
                }else
                 {
                         final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                         progressDialog.setMessage("Loading..");
                         progressDialog.show();

                         mAuth.createUserWithEmailAndPassword(stremail, pass)
                                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                     @Override
                                     public void onComplete(@NonNull Task<AuthResult> task) {
                                         if (task.isSuccessful()) {
                                             // Sign in success, update UI with the signed-in user's information
                                             Log.d("RegisterActivity", "createUserWithEmail:success");
                                             FirebaseUser user = mAuth.getCurrentUser();
                                             String userUid = user.getUid();
                                             //  Toast.makeText(RegisterActivity.this, "uid"+userUid, Toast.LENGTH_SHORT).show();
                                             //updateUI(user);
                                             //  String userkey = myRef.push().getKey();

                                             UserModel usermodel = new UserModel();
                                             usermodel.setFn(strfn);
                                             usermodel.setLn(strln);
                                             usermodel.setEmail(stremail);
                                             usermodel.setMobileno(strmb);
                                             usermodel.setPassword(pass);
                                             usermodel.setUserKey(userUid);
                                             usermodel.setUserType(rdbtn);
                                             usermodel.setImagepath("");

                                             myRef.child(userUid).setValue(usermodel);
                                             Toast.makeText(RegisterActivity.this, "Signup done!!", Toast.LENGTH_SHORT).show();
                                             startActivity(new Intent(RegisterActivity.this,BusinessOwnerActivity.class));

                                             if (progressDialog.isShowing()) {
                                                 progressDialog.dismiss();
                                             }

                                             Intent i = new Intent(RegisterActivity.this, loginActivity.class);
                                           startActivity(i);
                                             finish();
                                         } else {
                                             // If sign in fails, display a message to the user.
                                             Log.w("RegisterActivity", "createUserWithEmail:failure", task.getException());
                                             Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                                     Toast.LENGTH_SHORT).show();
                                             progressDialog.dismiss();
                                             //  updateUI(null);
                                         }

                                         // ...
                                     }
                                 });

                         /*  */
                    }
                    break;
        }



}

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this,loginActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }

}