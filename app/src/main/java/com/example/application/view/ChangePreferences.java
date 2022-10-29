package com.example.application.view;

import androidx.annotation.NonNull;
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
import com.example.application.Profile;
import com.example.application.R;
import com.example.application.TypesOfDietaryRequirements;
import com.example.application.model.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class ChangePreferences extends AppCompatActivity implements View.OnClickListener{
    private Spinner transportMode;
    private Spinner dietRequirement;
    private Spinner travelTime;

    private Button confirmChanges;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Profile newProfile;

    private Model changePreferencesModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_preferences);

        //New profile
        newProfile = new Profile();

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

        //Instantiating model
        changePreferencesModel = new ChangePreferencesModel();

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    @Override
    public void onClick(View view) {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        createProfile();

        //Hash Map to store string data
        Map<String, Object> map = new HashMap<>();
        map.put("dietaryRequirements", newProfile.getDietaryRequirements().toString());
        map.put("preferredMaximumTravelTime", newProfile.getPreferredMaximumTravelTime().toString());
        map.put("preferredModeOfTransport", newProfile.getPreferredModeOfTransport().toString());

        //Update database
        mDatabase.child("Account").child(userID).child("Profile").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ChangePreferences.this, "Preferrences updated!", Toast.LENGTH_SHORT).show();
            }
        });
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
                newProfile.setPreferredMaximumTravelTime(PreferredMaximumTravelTime.HALF_HOUR);
                break;
            case "1 hour":
                newProfile.setPreferredMaximumTravelTime(PreferredMaximumTravelTime.ONE_HOUR);
                break;
            case "1.5 hours":
                newProfile.setPreferredMaximumTravelTime(PreferredMaximumTravelTime.ONE_HALF_HOUR);
                break;
            case "2 hours":
                newProfile.setPreferredMaximumTravelTime(PreferredMaximumTravelTime.TWO_HOUR);
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
