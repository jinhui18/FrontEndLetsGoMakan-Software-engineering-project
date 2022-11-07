package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.view.LoginUI;
import com.google.firebase.auth.FirebaseAuth;

public class CreateNewAccountVerifyEmail extends AppCompatActivity {

    private TextView textView1, textView2, textView3;
    private Button loginButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account_verify_email);

        loginButton = findViewById(R.id.button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CreateNewAccountVerifyEmail.this, LoginUI.class));
            }
        });

    }



}
