package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ResetPasswordVerifyOTP extends AppCompatActivity {

    TextView textView1,textView2,textView3,textView4,textView5,textView6;
    ImageView imageView1,imageView2,imageView3,imageView4;

    TextInputLayout textInputEmail;
    TextInputEditText email;

    TextInputLayout textInputOTP;
    TextInputEditText otp;

    Button resetPasswordButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_verifyotp);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView6 = (TextView) findViewById(R.id.textView6);


        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);

        textInputEmail = findViewById(R.id.textInputLayoutEmailAddress);
        email =  findViewById(R.id.TextInputEditEmail);

        textInputOTP = findViewById(R.id.textInputLayoutOTP);
        otp = findViewById(R.id.TextInputEditOTP);
        resetPasswordButton = (Button) findViewById(R.id.buttonResetPassword);


        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPasswordVerifyOTP.this, ResetPasswordEnterNewPassword.class  );
                startActivity(intent);
            }
        });


    }
}
