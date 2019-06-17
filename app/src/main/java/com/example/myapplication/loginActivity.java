package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {
    private Button btnSignup, btnLogin;
    EditText edtLogin, edtPassword;
    private FirebaseAuth mAuth;
    private String TAG = "loginActivity";
    private DatabaseReference myRef;
    String strUID;
    String userType;
    ProgressDialog progressDialog;
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Register");
        textView = findViewById(R.id.forgotpassword);

        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        edtLogin = (EditText) findViewById(R.id.login_id);
        edtPassword = (EditText) findViewById(R.id.pass_id);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this,forgotpassword.class));
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(loginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btn_login:

                        String email = edtLogin.getText().toString();
                        String password = edtPassword.getText().toString();

                        if (email.equals("") && email.isEmpty()) {
                            edtLogin.setError("Please enter email");
                        } else if (password.equals("") && password.isEmpty()) {
                            edtPassword.setError("Please enter password");
                        } else {

                            final ProgressDialog progressDialog = new ProgressDialog(loginActivity.this);
                            progressDialog.setMessage("Loading..");
                             progressDialog.show();

                            mAuth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(loginActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d("loginActivity", "signInWithEmail:success");
                                                FirebaseUser user1 = mAuth.getCurrentUser();

                                                String strUID = user1.getUid();
                                                Log.e("login", "****" + strUID);

                                                logindata(strUID);


                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                Toast.makeText(loginActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });


                        }
                }
                ;

            }

            private void logindata(final String strUID) {
                if (!strUID.equals("")) {
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                UserModel userModel = dataSnapshot1.getValue(UserModel.class);

                                String strname = userModel.getUserKey();

                                if (strUID.equals(strname)){

                                    String strfn = userModel.getFn();
                                    String strln = userModel.getLn();
                                    String strRolletype = userModel.getUserType();


                                    if (strRolletype.equals("Business owner")){

                                        SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("USER_UID_KEY", strname);
                                        editor.putString("USER_FN_KEY", strfn);
                                        editor.putString("USER_LN_KEY", strln);
                                        editor.putString("USER_ROLLETYPE", strRolletype);
                                        editor.commit();

                                        startActivity(new Intent(loginActivity.this, BusinessOwnerActivity.class));

                                    }else if (strRolletype.equals("Customer")){


                                        SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("USER_UID_KEY", strname);
                                        editor.putString("USER_FN_KEY", strfn);
                                        editor.putString("USER_LN_KEY", strln);
                                        editor.putString("USER_ROLLETYPE", strRolletype);
                                        editor.commit();

                                        startActivity(new Intent(loginActivity.this, NavigationActivity.class));

                                    }
//                                    else {
//
//                                        startActivity(new Intent(loginActivity.this, NavigationActivity.class));
//
//                                    }


                                }


/*
                                if (strname.equals(strUID)) {
                                    Log.e("login", "----" + strname);
                                    SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("USER_UID_KEY", strname);
                                    editor.putString("USER_FN_KEY", strfn);
                                    editor.putString("USER_LN_KEY", strln);
                                    editor.commit();
                                    Intent i = new Intent(loginActivity.this, NavigationActivity.class);
                                    startActivity(i);

                                }*/


                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }


        });
    }
}


/*
 *
 *
 * */