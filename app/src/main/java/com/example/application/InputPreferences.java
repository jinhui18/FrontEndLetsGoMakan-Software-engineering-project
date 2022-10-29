package com.example.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.application.PreferredMaximumTravelTime;
import com.example.application.PreferredModeOfTransport;
import com.example.application.TypesOfDietaryRequirements;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class InputPreferences extends AppCompatActivity implements View.OnClickListener {

    private Spinner transportMode;
    private Spinner dietRequirement;
    private Spinner travelTime;

    private Button createProfile;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    Profile currentProfile = new Profile();

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

        ArrayAdapter<CharSequence> transportAdapter = ArrayAdapter.createFromResource(this, R.array.transportMode, android.R.layout.simple_spinner_item);
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        transportMode.setAdapter(transportAdapter);

        ArrayAdapter<CharSequence> travelAdapter = ArrayAdapter.createFromResource(this, R.array.travelTime, android.R.layout.simple_spinner_item);
        travelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        travelTime.setAdapter(travelAdapter);

        ArrayAdapter<CharSequence> dietAdapter = ArrayAdapter.createFromResource(this, R.array.diet, android.R.layout.simple_spinner_item);
        dietAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dietRequirement.setAdapter(dietAdapter);
    }

    @Override
    public void onClick(View v){
        createProfile();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        mDatabase.child("Account").child(userID).child("Profile").setValue(currentProfile).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                Toast.makeText(InputPreferences.this, "Account successfully updated!", Toast.LENGTH_SHORT).show();

                /*TESTING UPDATE
                Profile newProfile = new Profile(TypesOfDietaryRequirements.VEGETARIAN, PreferredMaximumTravelTime.HALF_HOUR, PreferredModeOfTransport.CAR);
                Map<String, Object> map = new HashMap<>();
                map.put("dietaryRequirements", newProfile.getDietaryRequirements().toString());
                map.put("preferredMaximumTravelTime", newProfile.getPreferredMaximumTravelTime().toString());
                map.put("preferredModeOfTransport", newProfile.getPreferredModeOfTransport().toString());

                //Update database
                mDatabase.child("Account").child(userID).child("Profile").updateChildren(map);
                Toast.makeText(InputPreferences.this, "HELLOOOO", Toast.LENGTH_SHORT);
                */

                startActivity(new Intent(InputPreferences.this, CreateNewAccountVerifyEmail.class));
            } else {
                Toast.makeText(InputPreferences.this, "Failed to create account, please try again!", Toast.LENGTH_SHORT).show();
            }
        });


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
                currentProfile.setPreferredMaximumTravelTime(PreferredMaximumTravelTime.HALF_HOUR);
                break;
            case "1 hour":
                currentProfile.setPreferredMaximumTravelTime(PreferredMaximumTravelTime.ONE_HOUR);
                break;
            case "1.5 hours":
                currentProfile.setPreferredMaximumTravelTime(PreferredMaximumTravelTime.ONE_HALF_HOUR);
                break;
            case "2 hours":
                currentProfile.setPreferredMaximumTravelTime(PreferredMaximumTravelTime.TWO_HOUR);
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
