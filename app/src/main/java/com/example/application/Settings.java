package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.application.view.ChangePreferencesUI;
import com.example.application.view.LoginUI;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    private Button buttonChangePreferences, buttonSignOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buttonChangePreferences = findViewById(R.id.buttonChangePreference);
        buttonSignOut = findViewById(R.id.buttonSignOut);

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
                }
            }
    }
}
