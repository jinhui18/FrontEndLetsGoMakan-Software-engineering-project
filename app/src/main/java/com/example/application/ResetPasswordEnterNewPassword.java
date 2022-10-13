package com.example.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ResetPasswordEnterNewPassword extends AppCompatActivity {

     TextView textView1, textView2,textView3,textView4;
     ImageView imageView1,imageView2,imageView3;

     TextInputLayout textInputPassword1,textInputPassword2;
     TextInputEditText password1,password2;

     Button buttonResetPassword;


    @SuppressLint("CutPasteId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_enter_new_password);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);

        textInputPassword1 = findViewById(R.id.textInputLayoutPassword);
        textInputPassword2 = findViewById(R.id.textInputLayoutPassword1);

        password1 = findViewById(R.id.TextInputEditPassword);
        password2 = findViewById(R.id.TextInputEditPassword1);

        buttonResetPassword = findViewById(R.id.buttonResetPassword);

        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResetPasswordEnterNewPassword.this, MainActivity.class  );
                startActivity(i);
                if (validatePassword(password1.getText().toString()) && validatePassword(password2.getText().toString())){
                    if (password1.getText().toString().equals(password2.getText().toString())){
                        Intent intent = new Intent(ResetPasswordEnterNewPassword.this, MainActivity.class  );
                        startActivity(intent);
                    }
                }
                else{
                    textInputPassword1.setError("Please enter a password with the correct requirements");
                }

            }
        });

    }

    private boolean validatePassword(String password) {

        if (password.equals("")) {
            return false;
        } else {
            return true;
        }
    }
}
