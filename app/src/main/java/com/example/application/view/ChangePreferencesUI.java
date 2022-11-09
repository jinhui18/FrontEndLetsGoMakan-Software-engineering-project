package com.example.application.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.application.backend.enums.PreferredModeOfTransport;
import com.example.application.backend.entity.Profile;
import com.example.application.R;
import com.example.application.backend.enums.TypesOfDietaryRequirements;
import com.example.application.controller.Controller;
import com.example.application.model.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ChangePreferencesUI extends AppCompatActivity implements View.OnClickListener{
    //Widgets
    private Spinner transportMode;
    private Spinner dietRequirement;
    private Spinner travelTime;
    private Button confirmChanges;
    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    //MVC
    private Profile newProfile;
    private Model changePreferencesModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_preferences);

        //Instantiating widgets
        transportMode = findViewById(R.id.transportSpinner);
        dietRequirement = findViewById(R.id.dietSpinner);
        travelTime = findViewById(R.id.travelSpinner);
        confirmChanges = (Button) findViewById(R.id.confirmChanges);
        confirmChanges.setOnClickListener(this);
        ArrayAdapter<CharSequence> transportAdapter = ArrayAdapter.createFromResource(this, R.array.transportMode, android.R.layout.simple_spinner_item);
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        transportMode.setAdapter(transportAdapter);
        ArrayAdapter<CharSequence> travelAdapter = ArrayAdapter.createFromResource(this, R.array.travelTime, android.R.layout.simple_spinner_item);
        travelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        travelTime.setAdapter(travelAdapter);
        ArrayAdapter<CharSequence> dietAdapter = ArrayAdapter.createFromResource(this, R.array.diet, android.R.layout.simple_spinner_item);
        dietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dietRequirement.setAdapter(dietAdapter);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //MVC related
        newProfile = new Profile(); //the new profile object
        changePreferencesModel = new ChangePreferencesModel(mAuth, mDatabase, ChangePreferencesUI.this);
    }

    @Override
    public void onClick(View view) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        createProfile();

        //Update database
        ArrayList<Object> list = new ArrayList<Object>();
        list.add(newProfile);
        Controller changePreferencesController = new Controller(changePreferencesModel, list);
        changePreferencesController.handleEvent();
    }

    private void createProfile() {
        String transport = transportMode.getSelectedItem().toString().trim();
        String travel = travelTime.getSelectedItem().toString().trim();
        String diet = dietRequirement.getSelectedItem().toString().trim();

        switch (transport) {
            case "Walking":
                newProfile.setPreferredModeOfTransport(PreferredModeOfTransport.WALKING);
                break;
            case "Public Transport":
                newProfile.setPreferredModeOfTransport(PreferredModeOfTransport.PUBLIC_TRANSPORT);
                break;
            case "Car":
                newProfile.setPreferredModeOfTransport(PreferredModeOfTransport.CAR);
                break;
        }

        switch (travel) {
            case "0.5 hour":
                newProfile.setPreferredMaximumTravelTime(15);
                break;
            case "1 hour":
                newProfile.setPreferredMaximumTravelTime(30);
                break;
            case "1.5 hours":
                newProfile.setPreferredMaximumTravelTime(45);
                break;
            case "2 hours":
                newProfile.setPreferredMaximumTravelTime(60);
        }

        switch (diet) {
            case "None":
                newProfile.setDietaryRequirements(TypesOfDietaryRequirements.NONE);
                break;
            case "Halal":
                newProfile.setDietaryRequirements(TypesOfDietaryRequirements.HALAL);
                break;
            case "Vegetarian":
                newProfile.setDietaryRequirements(TypesOfDietaryRequirements.VEGETARIAN);
                break;
            case "Both":
                newProfile.setDietaryRequirements(TypesOfDietaryRequirements.BOTH);
                break;
        }
    }
}

//NOTE: No observer pattern for this