package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private TextView textView1,textView2,textView3;
    private ImageView imageView1,imageView2,imageView3;

    private TextInputLayout textInputEmail;
    private TextInputEditText email;


    Button sendOtpButton;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);

        textInputEmail = findViewById(R.id.textInputLayoutEmailAddress);
        email =  findViewById(R.id.TextInputEditEmail);

        sendOtpButton = (Button) findViewById(R.id.buttonSendOTP);

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // need to check if email is valid and in the system
                FirebaseAuth.getInstance().sendPasswordResetEmail( email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetPassword.this, "Please check your email to reset your password", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetPassword.this, MainActivity.class  );
                            startActivity(intent);
                        }
                    }
                }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPassword.this, "Log in error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}
