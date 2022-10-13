package com.example.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class CreateNewAccount extends AppCompatActivity {

    ImageView imageView1,imageView2,imageView3,imageView4;
    TextView textView1,textView2,textView3,textView4;



    TextInputLayout textInputName;
    TextInputEditText name;


    TextInputLayout textInputPassword;
    Button confirmButton;
    TextInputEditText password;
    TextInputLayout textInputEmail;
    TextInputEditText email;



    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account);

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

        textInputName = findViewById(R.id.textInputLayoutName);
        name = findViewById(R.id.TextInputEditName);

        textInputEmail = findViewById(R.id.textInputLayoutEmailAddress);
        email =  findViewById(R.id.TextInputEditEmail);

        textInputPassword = findViewById(R.id.textInputLayoutPassword);
        password = findViewById(R.id.TextInputEditPassword);
        confirmButton = (Button) findViewById(R.id.confirmButton);

        // need validate password and email address to fit the requirement
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean validateEmail,validatePassword;
                validateEmail = validateEmail();
                validatePassword = validatePassword();
                if (validateEmail && validatePassword  ){
                    Intent intent = new Intent(CreateNewAccount.this, HomePage.class  );
                    startActivity(intent);
                }
            }
        });



    }
    private boolean validateEmail() {
        String emailInput = email.getText().toString();

        if (emailInput.equals("")) {
            textInputEmail.setError("Field can't be empty");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }


    private boolean validatePassword() {
        String passwordInput = password.getText().toString();

        if (passwordInput.equals("")) {
            textInputPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }





}
