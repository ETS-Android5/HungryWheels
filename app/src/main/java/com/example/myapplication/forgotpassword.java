package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {

    EditText forgotemail;
    Button forgotbtn;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        forgotemail = findViewById(R.id.etEmail);
        forgotbtn = findViewById(R.id.btn_forgotpassword);

        firebaseAuth = FirebaseAuth.getInstance();

        forgotbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String email = forgotemail.getText().toString().trim();

                if (email.equals("")){
                    Snackbar.make(v,"Please Enter Your Email Id", Snackbar.LENGTH_LONG).show();
                }else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(forgotpassword.this, "Password reset sent your email id", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgotpassword.this,loginActivity.class));
                            }else {
                                Snackbar.make(v,"Error in sendng Password", Snackbar.LENGTH_LONG).setAction("Action",null).show();
                            }
                        }
                    });
                }
            }
        });
    }
}