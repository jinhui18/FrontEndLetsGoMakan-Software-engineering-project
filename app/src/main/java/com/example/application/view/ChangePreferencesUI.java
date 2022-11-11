package com.example.application.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Profile;
import com.example.application.backend.entity.Restaurant;
import com.example.application.backend.enums.PreferredModeOfTransport;
import com.example.application.controller.Controller;
import com.example.application.model.ChangePreferencesModel;
import com.example.application.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChangePreferencesUI extends AppCompatActivity implements View.OnClickListener{
    //Widgets
    private Spinner transportMode;
    private Spinner travelTime;
    private Button confirmChanges;
    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Profile currentProfile;
    //MVC
    private Profile newProfile;
    private Model changePreferencesModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_preferences);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //MVC related
        newProfile = new Profile(); //the new profile object
        changePreferencesModel = new ChangePreferencesModel(mAuth, mDatabase, ChangePreferencesUI.this);

        //Instantiating widgets
        transportMode = findViewById(R.id.transportSpinner);
        travelTime = findViewById(R.id.travelSpinner);
        confirmChanges = (Button) findViewById(R.id.confirmChanges);
        confirmChanges.setOnClickListener(this);

        initialiseDropDown(mAuth, mDatabase);
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
        startActivity(new Intent(this, SettingsPageUI.class));
    }

    public void initialiseDropDown(FirebaseAuth mAuth, DatabaseReference mDatabase){
        final ArrayList<Account> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        mDatabase.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Iterable<DataSnapshot> children = snapshot.getChildren();

                        for (DataSnapshot child : children) {
                            Account account = child.getValue(Account.class);
                            arrayList.add(account);
                        }
                        System.out.println("Size of arrayList 999 :" + arrayList.size());
                        ArrayList<Restaurant> recommendedList = new ArrayList<>();
                        currentProfile = arrayList.get(0).getProfile();
                        PreferredModeOfTransport pf = currentProfile.getPreferredModeOfTransport();
                        float pmt = currentProfile.getPreferredMaximumTravelTime();
                        int transportModeNum = 0; int travelTimeNum = 0;
                        System.out.println("PMT: "+pmt);
                        System.out.println("Mode of transport: "+ pf.toString());
                        ;

                        if ((pf.toString()).compareTo("WALKING")==0) {transportModeNum=0;}
                        else if ((pf.toString()).compareTo("PUBLIC_TRANSPORT")==0) {transportModeNum=1;}
                        else {transportModeNum=2;}

                        if (pmt==5) {travelTimeNum=0;}
                        else if (pmt==10) {travelTimeNum=1;}
                        else if (pmt==20) {travelTimeNum=2;}
                        else {travelTimeNum=3;}

                        ArrayAdapter<CharSequence> transportAdapter = ArrayAdapter.createFromResource(ChangePreferencesUI.this, R.array.transportMode, android.R.layout.simple_spinner_item);
                        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        transportMode.setAdapter(transportAdapter);
                        transportMode.setSelection(transportModeNum);


                        ArrayAdapter<CharSequence> travelAdapter = ArrayAdapter.createFromResource(ChangePreferencesUI.this, R.array.travelTime, android.R.layout.simple_spinner_item);
                        travelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        travelTime.setAdapter(travelAdapter);
                        travelTime.setSelection(travelTimeNum);


                        return;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ChangePreferencesUI.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
    }





    private void createProfile() {
        String transport = transportMode.getSelectedItem().toString().trim();
        String travel = travelTime.getSelectedItem().toString().trim();

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
            case "5 minutes":
                newProfile.setPreferredMaximumTravelTime(5);
                break;
            case "10 minutes":
                newProfile.setPreferredMaximumTravelTime(10);
                break;
            case "20 minutes":
                newProfile.setPreferredMaximumTravelTime(20);
                break;
            case "40 minutes":
                newProfile.setPreferredMaximumTravelTime(40);
                break;
        }
    }
}

//NOTE: No observer pattern for this