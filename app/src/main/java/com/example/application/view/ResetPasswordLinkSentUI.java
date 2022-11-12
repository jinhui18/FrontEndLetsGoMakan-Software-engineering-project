package com.example.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Jin Hui
 * This class display the page after our system has sent the resent password link to user.
 * It will inform user that the link has been sent and allow the user to return to login page or to resend the reset password link.
 * @version 1.0
 * @since 2022-11-11
 */
public class ResetPasswordLinkSentUI extends AppCompatActivity implements View.OnClickListener {

    Button buttonReturnLoginPage;
    TextView textViewResendLink;
    String email;

    /**
     * This method is called after the activity has launched but before it starts running.
     * This method sets on click listener for "LOGIN" button and "Resend Link" text and make it clickable.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in #onSaveInstanceState
     */
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

    /**
     * This method is called when user clicks on the "LOGIN" button or "Resend Link" text.
     * When the "LOGIN" button is clicked, user will be redirect to the LoginUI class.
     * When the "Resend Link" text is clicked, our system will resend the reset email password link to the user to the email address they previously entered and a text message saying the email has been resent will be displayed.
     * @param view is the ID of the button that user selects.
     */
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
                         Toast.makeText(ResetPasswordLinkSentUI.this, "Password reset link has been resent", Toast.LENGTH_SHORT).show();
                     }
                 }
             }
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPasswordLinkSentUI.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    }
}
