package com.example.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * @author Jin Hui
 * This class display the setting page for our application for users to access the Terms of Service page, Contact Us page and to change their account preferences.
 * @version 1.0
 * @since 2022-11-11
 */
public class SettingsPageUI extends AppCompatActivity implements View.OnClickListener {

    private Button buttonChangePreferences, buttonSignOut, buttonContactUs, buttonTermsAndConditions;

    /**
     * This method is called after the activity has launched but before it starts running.
     * This method sets on click listener for all buttons in the setting page and make it clickable.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in #onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonContactUs = findViewById(R.id.buttonContactUs);
        buttonTermsAndConditions = findViewById(R.id.buttonTermsAndConditions);
        buttonChangePreferences = findViewById(R.id.buttonChangePreference);
        buttonSignOut = findViewById(R.id.buttonSignOut);

        buttonContactUs.setOnClickListener(this);
        buttonTermsAndConditions.setOnClickListener(this);
        buttonChangePreferences.setOnClickListener(this);
        buttonSignOut.setOnClickListener(this);



    }

    /**
     * This method is called when user clicks on the buttons on the setting page.
     * The buttons includes the "CHANGE PREFERENCES","CONTACT US", "TERMS OF SERVICE" and "SIGN OUT" button.
     * This method implements the logic when user clicks on the button.
     * When user clicks on "CHANGE PREFERENCES" button, they will be redirect to the ChangePreferencesUI class which starts another activity.
     * When user clicks on "CONTACT US" button, they will be redirect to the SettingsContactUsUI class which starts another activity.
     * When user clicks on "TERMS OF SERVICE" button, they will be redirect to the SettingTermsAndConditionsUI class which starts another activity.
     * When user clicks on "SIGN OUT" button, they will be redirect to the LoginUI class which starts another activity.
     * @param view is the ID of the button that user selects.
     */
    @Override
    public void onClick(View view) {
            switch(view.getId()){
                case R.id.buttonChangePreference: {
                    startActivity(new Intent(this, ChangePreferencesUI.class));
                    break;
                }
                case R.id.buttonSignOut: {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, LoginUI.class));
                    break;
                }
                case R.id.buttonContactUs: {
                    startActivity(new Intent(this, SettingsContactUsUI.class));
                    break;
                }
                case R.id.buttonTermsAndConditions: {
                    startActivity(new Intent(this, SettingTermsAndConditionsUI.class));
                }
            }
    }
}
