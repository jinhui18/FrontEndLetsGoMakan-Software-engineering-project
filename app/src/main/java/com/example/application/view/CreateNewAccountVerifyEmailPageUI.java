package com.example.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.google.firebase.auth.FirebaseAuth;


/**
 * @author Jin Hui
 * This class display the page after user has successfully created an account.
 * It informs the user to click on the verification email sent to their registered email address before they can log in to our applciation.
 * It allows the user to return to login page.
 * @version 1.0
 * @since 2022-11-11
 */
public class CreateNewAccountVerifyEmailPageUI extends AppCompatActivity implements View.OnClickListener {

    private TextView textView1, textView2, textView3;
    private Button loginButton;

    /**
     * This method is called after the activity has launched but before it starts running.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in #onSaveInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account_verify_email);

        loginButton = findViewById(R.id.button);

        loginButton.setOnClickListener(this);
    }

    /**
     * This method is called when user clicks on the "LOGIN" button.
     * User will be redirected to the LoginUI class.
     * @param view is the ID of the button that user selects.
     */
    @Override
    public void onClick(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(CreateNewAccountVerifyEmailPageUI.this, LoginUI.class));
    }
}
