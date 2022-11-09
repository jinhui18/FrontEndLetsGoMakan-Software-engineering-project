package com.example.application.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.application.CreateNewAccountVerifyEmail;
import com.example.application.R;
import com.example.application.backend.entity.Profile;
import com.example.application.backend.enums.PreferredModeOfTransport;
import com.example.application.backend.enums.TypesOfDietaryRequirements;
import com.example.application.controller.Controller;
import com.example.application.model.ChangePreferencesModel;
import com.example.application.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InputPreferencesUI extends AppCompatActivity implements View.OnClickListener {

    private Spinner transportMode;
    private Spinner dietRequirement;
    private Spinner travelTime;

    private Button createProfile;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Profile currentProfile = new Profile();
    private Model inputPreferencesModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_preferences);

        transportMode = findViewById(R.id.transportSpinner);
        dietRequirement = findViewById(R.id.dietSpinner);
        travelTime = findViewById(R.id.travelSpinner);

        createProfile = (Button) findViewById(R.id.createPreferences);
        createProfile.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        ArrayAdapter<CharSequence> transportAdapter = ArrayAdapter.createFromResource(this, R.array.transportMode, android.R.layout.simple_spinner_item);
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        transportMode.setAdapter(transportAdapter);

        ArrayAdapter<CharSequence> travelAdapter = ArrayAdapter.createFromResource(this, R.array.travelTime, android.R.layout.simple_spinner_item);
        travelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        travelTime.setAdapter(travelAdapter);

        ArrayAdapter<CharSequence> dietAdapter = ArrayAdapter.createFromResource(this, R.array.diet, android.R.layout.simple_spinner_item);
        dietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dietRequirement.setAdapter(dietAdapter);

        inputPreferencesModel = new ChangePreferencesModel(mAuth, mDatabase, InputPreferencesUI.this);
    }

    @Override
    public void onClick(View v){
        createProfile();
        String userID = mAuth.getCurrentUser().getUid(); //changed FirebaseAuth.getInstance() to mAuth

        ArrayList<Object> list = new ArrayList<Object>();
        list.add(currentProfile);
        Controller changePreferencesController = new Controller(inputPreferencesModel, list);
        changePreferencesController.handleEvent();
        startActivity(new Intent(InputPreferencesUI.this, CreateNewAccountVerifyEmail.class));
    }

    private void createProfile() {
        String transport = transportMode.getSelectedItem().toString().trim();
        String travel = travelTime.getSelectedItem().toString().trim();
        String diet = dietRequirement.getSelectedItem().toString().trim();

        switch (transport) {
            case "Walking":
                currentProfile.setPreferredModeOfTransport(PreferredModeOfTransport.WALKING);
                break;
            case "Public Transport":
                currentProfile.setPreferredModeOfTransport(PreferredModeOfTransport.PUBLIC_TRANSPORT);
                break;
            case "Car":
                currentProfile.setPreferredModeOfTransport(PreferredModeOfTransport.CAR);
                break;
        }

        switch (travel) {
            case "0.5 hour":
                currentProfile.setPreferredMaximumTravelTime(15);
                break;
            case "1 hour":
                currentProfile.setPreferredMaximumTravelTime(30);
                break;
            case "1.5 hours":
                currentProfile.setPreferredMaximumTravelTime(45);
                break;
            case "2 hours":
                currentProfile.setPreferredMaximumTravelTime(60);
        }

        switch (diet) {
            case "None":
                currentProfile.setDietaryRequirements(TypesOfDietaryRequirements.NONE);
                break;
            case "Halal":
                currentProfile.setDietaryRequirements(TypesOfDietaryRequirements.HALAL);
                break;
            case "Vegetarian":
                currentProfile.setDietaryRequirements(TypesOfDietaryRequirements.VEGETARIAN);
                break;
            case "Both":
                currentProfile.setDietaryRequirements(TypesOfDietaryRequirements.BOTH);
                break;
        }
    }
}
              /*  //TESTING UPDATE
                Profile newProfile = new Profile(TypesOfDietaryRequirements.VEGETARIAN, PreferredMaximumTravelTime.HALF_HOUR, PreferredModeOfTransport.CAR);
                Map<String, Object> map = new HashMap<>();
                map.put("Profile", newProfile);
                //map.put("dietaryRequirements", newProfile.getDietaryRequirements().toString());
                //map.put("preferredMaximumTravelTime", newProfile.getPreferredMaximumTravelTime().toString());
                //map.put("preferredModeOfTransport", newProfile.getPreferredModeOfTransport().toString());

                //Update database
                mDatabase.child(userID).child("Account").updateChildren(map);
                Toast.makeText(InputPreferences.this, "HELLOOOO", Toast.LENGTH_SHORT);
                //END OF TESTING */