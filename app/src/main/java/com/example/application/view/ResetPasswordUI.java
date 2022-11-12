package com.example.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.backend.control.others.FormatChecker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Jin Hui
 * This class display the Reset Password page for our application for users to enter their password to reset their password.
 * @version 1.0
 * @since 2022-11-11
 */
public class ResetPasswordUI extends AppCompatActivity implements View.OnClickListener {

    private TextView textView1,textView2,textView3;
    private ImageView imageView1,imageView2,imageView3;

    private TextInputLayout textInputEmail;
    private TextInputEditText email;

    private Button sendOtpButton;

    /**
     * This method is called after the activity has launched but before it starts running.
     * This method sets on click listener for "SEND RESET PASSWORD LINK" in the reset password page and make it clickable.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in #onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialising all widgets
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);

        textInputEmail = findViewById(R.id.textInputLayoutEmailAddress);
        email =  findViewById(R.id.TextInputEditEmail);

        sendOtpButton = (Button) findViewById(R.id.buttonSendOTP);

        sendOtpButton.setOnClickListener(this);
    }

    /**
     * This method is called when user clicks on the "SEND RESET PASSWORD LINK" on the setting page.
     * This method implements the logic when user clicks on the button.
     * This method will first check if the email the user enter is a valid email a not, it means that the email must have a valid domain and have more than 1 character.
     * If it is not true, error message will be displayed.
     * This method will then check with our DataBase (FireBase) to see if the email is registered in our DataBase.
     * If it is not true, error message will be displayed.
     * If it is true, an email to reset their password will be sent to the email address they entered and the user will be redirect to ResetPasswordLinkSentUI class and another activity will be shown.
     * @param view is the ID of the button that user selects.
     */
    @Override
    public void onClick(View view) {
        // need to check if email is valid and in the system
        if (FormatChecker.isValidEmail(email.getText().toString(), textInputEmail)){
            FirebaseAuth.getInstance().sendPasswordResetEmail( email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
             @Override
             public void onComplete(@NonNull Task<Void> task) {
                 if (task.isSuccessful()){
                     Toast.makeText(ResetPasswordUI.this, "Please check your email to reset your password", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(ResetPasswordUI.this, ResetPasswordLinkSentUI.class  );
                     intent.putExtra("email", email.getText().toString());
                     startActivity(intent);
                 }
             }
        }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ResetPasswordUI.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        }
    }
}
