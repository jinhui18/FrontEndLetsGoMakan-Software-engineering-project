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

public class ResetPassword extends AppCompatActivity {

    TextView textView1,textView2,textView3,textView4;
    ImageView imageView1,imageView2,imageView3,imageView4;

    TextInputLayout textInputEmail;
    TextInputEditText email;

    TextInputLayout textInputOTP;
    TextInputEditText otp;

    Button sendOtpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);

        textInputEmail = findViewById(R.id.textInputLayoutEmailAddress);
        email =  findViewById(R.id.TextInputEditEmail);

        textInputOTP = findViewById(R.id.textInputLayoutOTP);
        otp = findViewById(R.id.TextInputEditOTP);
        sendOtpButton = (Button) findViewById(R.id.buttonSendOTP);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need to check if email is valid and in the system
                Intent intent = new Intent(ResetPassword.this, ResetPasswordVerifyOTP.class  );
                startActivity(intent);
            }
        });
    }
}
