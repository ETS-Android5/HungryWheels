package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackFragment extends Fragment implements View.OnClickListener{

RatingBar ratingBar;
Button btnSubmit;
EditText review;
    private DatabaseReference reference;
    private String strUid;
    private FirebaseAuth mAuth;

    private String strreview,rating;
    private DataSnapshot ratingRef;
    private String strRe_Key;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_feedback,container,false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("Restaurant");

        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
        strUid = sharedPreferences.getString("USER_UID_KEY", "");
        Log.e("ACCC", "uid" + strUid);

        mAuth = FirebaseAuth.getInstance();

        ratingBar = (RatingBar) rootview.findViewById(R.id.ratingbar);
        btnSubmit = (Button)rootview.findViewById(R.id.btn_submit);
        review = (EditText) rootview.findViewById(R.id.edt_review);

        btnSubmit.setOnClickListener(this);
                Toast.makeText(getContext(), "Thank you for your feedback", Toast.LENGTH_LONG).show();
                /*Intent i = new Intent(context,HomeFragment.class);
                context.startActivity(i);*/
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getActivity().setTitle("Feedback");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_submit:

                if (ratingBar.getRating()==0.0){

                    Toast.makeText(getContext(), "ThankYou for rating us", Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getContext(), "Please rate", Toast.LENGTH_SHORT).show();
                }

                ratingBar.getRating();

                strRe_Key = reference.push().getKey();

                RestModel restModel = new RestModel();
                restModel.setRating(rating);

                reference.child(strRe_Key).setValue(restModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Feedback has been registered", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                break;


        }
    }
}

