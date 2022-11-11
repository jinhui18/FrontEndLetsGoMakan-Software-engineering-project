package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.view.LoginUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordLinkSent extends AppCompatActivity implements View.OnClickListener {

    Button buttonReturnLoginPage;
    TextView textViewResendLink;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_link_sent);

        buttonReturnLoginPage = findViewById(R.id.buttonReturnLoginPage);
        textViewResendLink = findViewById(R.id.textViewResendLink);

        Intent intent = getIntent();
        email = intent.getExtras().getString("email");
        buttonReturnLoginPage.setOnClickListener(this);
        textViewResendLink.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonReturnLoginPage: {
                startActivity(new Intent(this, LoginUI.class));
                break;
            }
            case R.id.textViewResendLink: {

                // need to check if email is valid and in the system
                FirebaseAuth.getInstance().sendPasswordResetEmail( email).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful()){
                         Toast.makeText(ResetPasswordLinkSent.this, "Password reset link has been resent.", Toast.LENGTH_SHORT).show();
                     }
                 }
             }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPasswordLinkSent.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
}
