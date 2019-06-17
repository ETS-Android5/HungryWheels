package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

ListView listview;

    ArrayList<CartModel> dataModelArrayList;
    private DatabaseReference reference;
    private String strcart;
    private String strUid;
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

    listview = (ListView) findViewById(R.id.lv_cart);
    dataModelArrayList = new ArrayList<CartModel>();

        Button btnpay = (Button)findViewById(R.id.btn_payment);
        btnpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPayment();
            }
        });

/*
    Intent intent = getIntent();
    strcart = intent.getStringExtra("CART");
    Log.e("LLLNNNN",""+strcart);*/
    SharedPreferences sharedPreferences = getSharedPreferences("HUNGRY_WHEELS", Context.MODE_PRIVATE);
    strUid = sharedPreferences.getString("USER_UID_KEY", "");
    Log.e("ACCC", "uid" + strUid);


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    reference = database.getReference("Cart");
    reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                CartModel cartModel = dataSnapshot1.getValue(CartModel.class);

                String strDatacart = cartModel.getUser_cart_id();
                if (strUid.equals(strDatacart)) {

                    dataModelArrayList.add(cartModel);

                }

            }
            setdata(dataModelArrayList);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

}

    private void getPayment() {

        //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf("10")), "USD", "Simplified Coding Fee",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    private void setdata(ArrayList<CartModel> dataModelArrayList) {


        cart_adapter cart_adapter = new cart_adapter(CartActivity.this, dataModelArrayList);
        listview.setAdapter(cart_adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);

                        //Starting a new activity for the payment details and also putting the payment details with intent
                        startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", "paymentAmount"));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
    //In the above code you can see the string paymentDetails. It is in json format as follows. It contains the payment detail with a uni
}