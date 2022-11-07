package com.example.application;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class SetTimeLocation extends AppCompatActivity implements View.OnClickListener{

    private TextView textView_1, textView_2, textView_3;
    private ImageView imageView_1, imageView_2, imageView_3, imageView_4, imageView_5;
    private Button currentTimeButton, changeTimeButton, goToListButton;
    private CheckBox useCurrentLocation, useCurrentTime;
    private TextInputLayout textInputLocation;
    private TextInputEditText editLocation;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String coordinates;
    private String userID;

    private int hour,minute;

    private boolean useCurLoc, useCurTime, choseOneLoc, choseTime, choseLoc;

    private String userLocation;

    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time_location);

        textView_1 = (TextView) findViewById(R.id.setTimeLocationText);
        textView_2 = (TextView) findViewById(R.id.locationText);
        textView_3 = (TextView) findViewById(R.id.timeText);

        imageView_1 = (ImageView) findViewById(R.id.whiteBox);
        imageView_2 = (ImageView) findViewById(R.id.greyBox1);
        imageView_3 = (ImageView) findViewById(R.id.greyBox2);
        imageView_5 = (ImageView) findViewById(R.id.orangeBox);

        useCurrentLocation = (CheckBox) findViewById(R.id.useCurrentLocation);
        useCurrentTime = (CheckBox) findViewById(R.id.useCurrentTime);

        textInputLocation = (TextInputLayout) findViewById(R.id.textInputLayoutLocation);

        changeTimeButton = (Button) findViewById(R.id.changeTimeButton);
        goToListButton = (Button) findViewById(R.id.timeLocToList);
        goToListButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase.child(userID).child("Account").child("Use Current Location").setValue(false);
        mDatabase.child(userID).child("Account").child("Use Current Time").setValue(false);

        locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        useCurLoc = false;
        useCurTime = false;
        choseOneLoc = false;
        choseTime = false;
        choseLoc = false;

        getLocation();

        useCurrentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    textInputLocation.setEnabled(false);
                    useCurLoc = true;
                    mDatabase.child(userID).child("Account").child("Use Current Location").setValue(true);
                }
                else{
                    textInputLocation.setEnabled(true);
                    useCurLoc = false;
                    mDatabase.child(userID).child("Account").child("Use Current Location").setValue(false);
                }
            }
        });

        useCurrentTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    changeTimeButton.setEnabled(false);
                    useCurTime = true;
                    mDatabase.child(userID).child("Account").child("Use Current Time").setValue(true);
                }
                else{
                    changeTimeButton.setEnabled(true);
                    useCurTime = false;
                    mDatabase.child(userID).child("Account").child("Use Current Time").setValue(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(useCurLoc || choseLoc){
            Toast.makeText(this, "chose a location", Toast.LENGTH_SHORT).show();
            if(useCurLoc){
                getDeviceLocation();
                Toast.makeText(this, "current", Toast.LENGTH_SHORT).show();
            }
            if(useCurTime || choseTime){
                if(useCurTime){
                    getCurrentTime();
                }
                startActivity(new Intent(SetTimeLocation.this, DisplayRestaurantsList.class));
            }
            else{
                showTimeDialog();
            }
        }
        else{
            showLocDialog();
        }
        /*if(useCurLoc || choseLoc){
            Toast.makeText(this, "chose a location", Toast.LENGTH_SHORT).show();
            if(useCurTime || choseTime) {
                if (useCurLoc) {
                    getDeviceLocation();
                    Toast.makeText(this, "current", Toast.LENGTH_SHORT).show();
                } else {
                    getLocation();
                    Toast.makeText(this, "chose", Toast.LENGTH_SHORT).show();
                }
                if (useCurTime){
                    getCurrentTime();
                }
                Toast.makeText(this, "chose a time", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SetTimeLocation.this, DisplayRestaurantsList.class));
            }
            showTimeDialog();
        }
        else{
            showLocDialog();}*/


        /*if(useCurLoc || choseLoc){
            Toast.makeText(this, "chose a location", Toast.LENGTH_SHORT).show();
            if(useCurLoc){
                getDeviceLocation();
                Toast.makeText(SetTimeLocation.this, "current location selected", Toast.LENGTH_SHORT).show();
            }
            else if(choseLoc){
                getLocation();
                Toast.makeText(SetTimeLocation.this, "chosen location selected", Toast.LENGTH_SHORT).show();
            }
            if((useCurTime) || choseTime){
                if(useCurTime){
                    getCurrentTime();
                }
                Toast.makeText(SetTimeLocation.this, "Time selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SetTimeLocation.this, DisplayRestaurantsList.class));//change to displayrestaurantslist
            }
            else{
                showTimeDialog();
            }
        }
        else{
            showLocDialog();
        }*/
    }

    public void getLocation(){
        Places.initialize(getApplicationContext(), "AIzaSyBvQjZ15jD__Htt-F3TGvMp_ZWNw79JZv0");
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                coordinates = String.valueOf(place.getLatLng());
                mDatabase.child(userID).child("Account").child("Chosen Location").setValue(coordinates);
                choseLoc = true;
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                changeTimeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                if(minute<10){
                    mDatabase.child(userID).child("Account").child("Chosen Time").setValue(hour+":0"+minute);
                }
                else {
                    mDatabase.child(userID).child("Account").child("Chosen Time").setValue(hour + ":" + minute);
                }
                choseTime = true;
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("'Date\n'dd-MM-yyyy '\n\nand\n\nTime\n'HH:mm:ss z");
        String currentDateAndTime = sdf.format(new Date());
        mDatabase.child(userID).child("Account").child("Current Time").setValue(currentDateAndTime);
    }

    private void showTimeDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SetTimeLocation.this);
        builder1.setTitle("Choose a Time");
        builder1.setMessage("Please select a time or click the 'Use Current Time' checkbox to continue");
        builder1.setPositiveButton("Ok", (dialog, which) -> {
            dialog.cancel();
        });
        builder1.show();
    }

    private void showLocDialog(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(SetTimeLocation.this);
        builder2.setTitle("Choose a Location");
        builder2.setMessage("Please select a location or click the 'Use Current Location' checkbox to continue");
        builder2.setPositiveButton("Ok", (dialog, which) -> {
            dialog.cancel();
        });
        builder2.show();
    }

    private void getDeviceLocation(){
        try{
            LocationServices.getFusedLocationProviderClient(SetTimeLocation.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(SetTimeLocation.this).removeLocationUpdates(this);

                    if(locationResult != null && locationResult.getLocations().size() > 0)
                    {
                        Toast.makeText(SetTimeLocation.this, "got current location!", Toast.LENGTH_SHORT).show();
                        int index1 = locationResult.getLocations().size() - 1;
                        double latitude = locationResult.getLocations().get(index1).getLatitude();
                        double longitude = locationResult.getLocations().get(index1).getLongitude();
                        userLocation = "lat/lng: ("+latitude+", "+longitude+")";
                        mDatabase.child(userID).child("Account").child("Current Location").setValue(userLocation);
                    }
                    else{
                        Log.d(TAG, "Location permission not granted... Exiting the app");
                        finish();
                        startActivity(getIntent());
                    }
                }
            }, Looper.getMainLooper());
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
}