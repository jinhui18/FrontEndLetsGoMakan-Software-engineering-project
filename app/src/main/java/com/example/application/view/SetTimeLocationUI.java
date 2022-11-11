package com.example.application.view;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.backend.control.others.FirebaseForAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class SetTimeLocationUI extends AppCompatActivity implements View.OnClickListener{

    private TextView textView_1, textView_2, textView_3;
    private ImageView imageView_1;
    private ImageView imageView_2;
    private ImageView imageView_3;
    private ImageView imageView_4;
    private Button changeDateTimeButton, goToListButton;
    private CheckBox useCurrentLocation, useCurrentDateTime;
    private TextInputLayout textInputLocation;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String coordinates;
    private String userID;

    private String day, month, year, hour, minute;
    private String currentDay, currentMonth, currentYear, currentHour, currentMinute, currentSecond;


    private boolean useCurLoc, useCurDateTime, choseDateTime, choseLoc;

    private String userLocation;

    private LocationRequest locationRequest;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time_location);

        textView_1 = findViewById(R.id.setTimeLocationText);
        textView_2 = findViewById(R.id.locationText);
        textView_3 = findViewById(R.id.timeText);

        imageView_1 = findViewById(R.id.whiteBox);
        imageView_2 = findViewById(R.id.greyBox1);
        imageView_3 = findViewById(R.id.greyBox2);
        imageView_4 = findViewById(R.id.orangeBox);

        progressBar = findViewById(R.id.progressBar);
        useCurrentLocation = findViewById(R.id.useCurrentLocation);
        useCurrentDateTime = findViewById(R.id.useCurrentDateTime);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        textInputLocation = findViewById(R.id.textInputLayoutLocation);

        changeDateTimeButton = findViewById(R.id.changeDateTimeButton);
        goToListButton = findViewById(R.id.timeLocToList);
        goToListButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        useCurLoc = false;
        useCurDateTime = false;
        choseDateTime = false;
        choseLoc = false;

        mDatabase.child(userID).child("Account").child("useCurrentLocation").setValue(false);
        mDatabase.child(userID).child("Account").child("useCurrentDateTime").setValue(false);

        getLocation();
        pickDateTime();

        useCurrentLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                textInputLocation.setEnabled(false);
                useCurLoc = true;
                mDatabase.child(userID).child("Account").child("useCurrentLocation").setValue(true);
            }
            else{
                textInputLocation.setEnabled(true);
                useCurLoc = false;
                mDatabase.child(userID).child("Account").child("useCurrentLocation").setValue(false);
            }
        });

        useCurrentDateTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                changeDateTimeButton.setEnabled(false);
                useCurDateTime = true;
                mDatabase.child(userID).child("Account").child("useCurrentDateTime").setValue(true);
            }
            else{
                changeDateTimeButton.setEnabled(true);
                useCurDateTime = false;
                mDatabase.child(userID).child("Account").child("useCurrentDateTime").setValue(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(useCurLoc || choseLoc){
            System.out.println("useCurLoc: " + useCurLoc);
            System.out.println("choseLoc: " + choseLoc);
            if(useCurLoc){
                getDeviceLocation();
            }
            if(useCurDateTime || choseDateTime){
                System.out.println("useCurDateTime: " + useCurDateTime);
                System.out.println("choseDateTime: " + choseDateTime);
                if(useCurDateTime){
                    getCurrentDateTime();
                }

                FirebaseForAPI fb = new FirebaseForAPI();
                fb.getAPIData(mAuth, mDatabase, this);

                startActivity(new Intent(SetTimeLocationUI.this, LoadingPageUI.class));

                /*
                mDatabase.child(userID).child("Account").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        //progressBar.setVisibility(View.GONE);
                        //System.out.println("HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE1");
                        //startActivity(new Intent(SetTimeLocation.this, DisplayRestaurantsList.class));
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        progressBar.setVisibility(View.GONE);
                        System.out.println("HEREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE2");

                        startActivity(new Intent(SetTimeLocation.this, DisplayRestaurantsList.class));
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                //progressBar.setVisibility(View.GONE);

                //startActivity(new Intent(SetTimeLocation.this, DisplayRestaurantsList.class));*/
            }
            else{
                showTimeDialog();
            }
        }
        else{
            showLocDialog();
        }
    }

    public void getLocation(){
        Places.initialize(getApplicationContext(), "AIzaSyBvQjZ15jD__Htt-F3TGvMp_ZWNw79JZv0");

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
                mDatabase.child(userID).child("Account").child("chosenLocation").setValue(coordinates);
                choseLoc = true;
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    public void pickDateTime(){
        changeDateTimeButton.setOnClickListener(view -> {
            DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                year = Integer.toString(selectedYear);
                month = Integer.toString(selectedMonth+1);
                day = Integer.toString(selectedDay);
                if(selectedDay<10   ){
                    day = "0"+selectedDay;
                }
                if((selectedMonth+1)<10){
                    month = "0"+(selectedMonth+1);
                }
                String date = day+"-"+month+"-"+year;
                mDatabase.child(userID).child("Account").child("chosenDate").setValue(date);
                TimePickerDialog.OnTimeSetListener onTimeSetListener = (view1, selectedHour, selectedMinute) -> {
                    hour = Integer.toString(selectedHour);
                    minute = Integer.toString(selectedMinute);
                    if(selectedHour<10){
                        hour = "0"+selectedHour;
                    }
                    if(selectedMinute<10){
                        minute = "0"+selectedMinute;
                    }
                    mDatabase.child(userID).child("Account").child("chosenTime").setValue(hour+":"+minute);
                    choseDateTime = true;
                    changeDateTimeButton.setText(String.format(Locale.getDefault(), "%s-%s-%s %s:%s", day, month, year, hour, minute));
                };
                int style = AlertDialog.THEME_HOLO_DARK;
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetTimeLocationUI.this, style, onTimeSetListener, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(SetTimeLocationUI.this, onDateSetListener, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
            datePickerDialog.setTitle("Select Date");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH,6);
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });
    }

    public void getCurrentDateTime(){
        System.out.println("getting current time");
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        currentYear = Integer.toString(today.year);
        currentMonth = Integer.toString(today.month + 1);
        currentDay = Integer.toString(today.monthDay);
        currentHour = Integer.toString(today.hour);
        currentMinute = Integer.toString(today.minute);
        currentSecond = Integer.toString(today.second);

        if((today.month+1) < 10){
            currentMonth = "0"+(today.month+1);
        }
        if(today.monthDay < 10){
            currentDay = "0"+today.monthDay;
        }
        if(today.hour < 10){
            currentHour = "0"+today.hour;
        }
        if(today.minute < 10){
            currentMinute = "0"+today.minute;
        }
        if(today.second < 10){
            currentSecond = "0"+today.second;
        }

        String dateTime = "Date "+currentDay+"-"+currentMonth+"-"+currentYear+" and Time "+currentHour+":"+currentMinute+":"+currentSecond+" SGT";

        System.out.println(currentMonth);
        System.out.println(currentDay);
        System.out.println(dateTime);

        mDatabase.child(userID).child("Account").child("currentDateTime").setValue(dateTime);
    }

    private void showTimeDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SetTimeLocationUI.this);
        builder1.setTitle("Choose a Time");
        builder1.setMessage("Please select a time or click the 'Use Current Time' checkbox to continue");
        builder1.setPositiveButton("Ok", (dialog, which) -> dialog.cancel());
        builder1.show();
    }

    private void showLocDialog(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(SetTimeLocationUI.this);
        builder2.setTitle("Choose a Location");
        builder2.setMessage("Please select a location or click the 'Use Current Location' checkbox to continue");
        builder2.setPositiveButton("Ok", (dialog, which) -> dialog.cancel());
        builder2.show();
    }

    private void getDeviceLocation() {
        System.out.println("getting device location");
        try {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    userLocation = "lat/lng: (" + latitude + ", " + longitude + ")";
                    mDatabase.child(userID).child("Account").child("currentLocation").setValue(userLocation);
                } else {
                    Log.d(TAG, "Location permission not granted... Exiting the app");
                    finish();
                    startActivity(getIntent());
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

}