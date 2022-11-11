package com.example.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsPageUI extends AppCompatActivity implements View.OnClickListener {

    private Button buttonChangePreferences, buttonSignOut, buttonContactUs, buttonTermsAndConditions;

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
