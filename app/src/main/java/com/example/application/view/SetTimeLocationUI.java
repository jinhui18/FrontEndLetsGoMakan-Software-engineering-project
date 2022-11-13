package com.example.application.view;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.application.R;
import com.example.application.backend.control.others.FirebaseForAPI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

/**
 * SetTimeLocationUI is the View class that displays the UI elements to allow users to set their date, time and location for the restaurant search.
 * It contains four clickable boxes, "Search", "Use current location" are two of them, which gets users to set a location for the restaurant search.
 * The next two are "Enter a different Date & Time" and "Use current Date and Time", which gets users to set a date and time for the restaurant search.
 * It also contains a button, "Lets Go Makan" to start the search once the system detects an input location and date and time.
 * @author Celest
 * @version 1.0
 * @since 2022-11-13
 */
public class SetTimeLocationUI extends AppCompatActivity implements View.OnClickListener{

    //Widgets
    private TextView textView_1, textView_2, textView_3;
    private ImageView imageView_1;
    private ImageView imageView_2;
    private ImageView imageView_3;
    private ImageView imageView_4;
    private Button changeDateTimeButton, goToListButton;
    private CheckBox useCurrentLocation, useCurrentDateTime;
    private TextInputLayout textInputLocation;
    private ProgressBar progressBar;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userID;

    private String coordinates;

    private String day, month, year, hour, minute;
    private String currentDay, currentMonth, currentYear, currentHour, currentMinute, currentSecond;


    private boolean useCurLoc, useCurDateTime, choseDateTime, choseLoc;

    private String userLocation;

    private LocationRequest locationRequest;

    private FusedLocationProviderClient fusedLocationClient;

    /**
     * This method is called after the activity has launched but before it starts running.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in #onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time_location);

        //Instantiating the widgets
        textView_1 = findViewById(R.id.setTimeLocationText);
        textView_2 = findViewById(R.id.locationText);
        textView_3 = findViewById(R.id.timeText);

        imageView_1 = findViewById(R.id.whiteBox);
        imageView_2 = findViewById(R.id.greyBox1);
        imageView_3 = findViewById(R.id.greyBox2);
        imageView_4 = findViewById(R.id.orangeBox);

        useCurrentLocation = findViewById(R.id.useCurrentLocation);
        useCurrentDateTime = findViewById(R.id.useCurrentDateTime);

        textInputLocation = findViewById(R.id.textInputLayoutLocation);

        changeDateTimeButton = findViewById(R.id.changeDateTimeButton);
        goToListButton = findViewById(R.id.timeLocToList);

        //Allow users to click on the "Lets Go Makan!" button
        goToListButton.setOnClickListener(this);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //This function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Sets up a location request to pass into the location provider object as a parameter
        locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        //All variables for choosing location and date/time must be false initially
        useCurLoc = false;
        useCurDateTime = false;
        choseDateTime = false;
        choseLoc = false;

        //Calling the methods to create the google search box, date and time pickers.
        getLocation();
        pickDateTime();

        //Using current location or date/time is false initially, and will be changed once users ticks the respective checkboxes.
        mDatabase.child(userID).child("Account").child("useCurrentLocation").setValue(false);
        mDatabase.child(userID).child("Account").child("useCurrentDateTime").setValue(false);

        //Checks if user ticks the checkbox for "Use current location"
        useCurrentLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                textInputLocation.setEnabled(false);
                useCurLoc = true;
                mDatabase.child(userID).child("Account").child("useCurrentLocation").setValue(true);
                getDeviceLocation();
            }
            else{
                textInputLocation.setEnabled(true);
                useCurLoc = false;
                mDatabase.child(userID).child("Account").child("useCurrentLocation").setValue(false);
            }
        });

        //Checks if user ticks the checkbox for "Use current Date and Time"
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

    /**
     * This method contains the logic for the clickable buttons and text in this activity class.The clickable buttons "Lets Go Makan!" and boxes are "Search", "Use current location", "Enter a different Date & Time" and "Use current Date and Time".
     * When "Search" box is pressed, a google search box opens to allow users to input their chosen location.
     * When "Use current location" box is pressed, the checkbox is ticked and the user has chosen to use their current location for the search.
     * When "Enter a different Date & Time" box is pressed, a date picker opens to allow users to choose a date and then a time picker opens to allow users to choose a time for the search.
     * When "Use current Date and Time" box is pressed, the checkbox is ticked and the user has chosen to use their current date and time for the search.
     * When "Lets Go Makan!" button is pressed, it will call this method.
     * This method will first check if the user has set a location, either by choosing in the google search box or by ticking the "Use current location" checkbox.
     * If that is not true, an error message will be displayed to alert the user to input a location.
     * Then, if the user chose to use their current location, this method will call the getDeviceLocation() method to retrieve the user's current location
     * using the Google Play Services Fused Location Provider API.
     * Then, the method will check if the user has set a date and time, either by choosing in the date and time pickers or by ticking the "Use current Date and Time" checkbox.
     * If that is not true, an error message will be displayed to alert the user to input a date and time.
     * Then, if the user chose to use the current date and time, this method will call the getCurrentDateTime() method to retrieve the user's current date and time using the Time package.
     * Finally, it will create a FirebaseForAPI object to call the getAPIData() method in the FirebaseForAPI class and then redirect user to the loadingPageUI class.
     * @param view  is the ID of the button that user selects.
     */
    @Override
    public void onClick(View view) {
        //Check if user has set a location
        if (useCurLoc || choseLoc) {
            //Check if user decided to use current location
            if (useCurLoc) {
                //Retrieve the current location and store into Firebase
                getDeviceLocation();
            }

            //Check if user has set a time
            if (useCurDateTime || choseDateTime) {
                //Check if user decided to use current date and time
                if (useCurDateTime) {
                    //Retrieve the current date and time and store into Firebase
                    getCurrentDateTime();
                }

                //Instantiate a FirebaseForAPI object to get API data
                FirebaseForAPI fb = new FirebaseForAPI();
                fb.getAPIData(mAuth, mDatabase, this);

                //Direct users to the loading page
                startActivity(new Intent(SetTimeLocationUI.this, LoadingPageUI.class));
            }
            //User did not set a date and time so an alert pops up
            else{
                showTimeDialog();
            }
        }
        //User did not set a location so an alert pops up
        else{
            showLocDialog();
        }
    }

    /**
     * This method sets up the google search box using the places SDK to allow users to search and choose locations.
     * It then stores their chosen location in Firebase
     */
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
                mDatabase.child(userID).child("Account").child("chosenLocation").setValue(coordinates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetTimeLocationUI.this, "Unable to store chosen location", Toast.LENGTH_SHORT).show();
                    }
                });
                choseLoc = true;
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                Toast.makeText(SetTimeLocationUI.this, "Error getting place selected, please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method creates a date picker and time picker to allow users to input their preferred date and time when searching for restaurants.
     * It then stores their chosen date and time in Firebase.
     */
    public void pickDateTime(){
        //User clicks on the "Enter a different Date & Time" button
        changeDateTimeButton.setOnClickListener(view -> {

            //User chose a date
            DatePickerDialog.OnDateSetListener onDateSetListener = (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                //Retrieve the date
                year = Integer.toString(selectedYear);
                month = Integer.toString(selectedMonth+1);
                day = Integer.toString(selectedDay);

                //Formatting
                if(selectedDay<10   ){
                    day = "0"+selectedDay;
                }
                if((selectedMonth+1)<10){
                    month = "0"+(selectedMonth+1);
                }
                String date = day+"-"+month+"-"+year;

                //Storing the chosen date in Firebase
                mDatabase.child(userID).child("Account").child("chosenDate").setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {}
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetTimeLocationUI.this, "Unable to store chosen date", Toast.LENGTH_SHORT).show();
                    }
                });

                //User chose a time
                TimePickerDialog.OnTimeSetListener onTimeSetListener = (view1, selectedHour, selectedMinute) -> {
                    //Retrieve the time
                    hour = Integer.toString(selectedHour);
                    minute = Integer.toString(selectedMinute);

                    //Formatting
                    if(selectedHour<10){
                        hour = "0"+selectedHour;
                    }
                    if(selectedMinute<10){
                        minute = "0"+selectedMinute;
                    }

                    //Storing the chosen time in Firebase
                    mDatabase.child(userID).child("Account").child("chosenTime").setValue(hour+":"+minute).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SetTimeLocationUI.this, "Unable to store chosen time", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //User has chosen a date and time
                    choseDateTime = true;

                    //Show the chosen date and time in the widget
                    changeDateTimeButton.setText(String.format(Locale.getDefault(), "%s-%s-%s %s:%s", day, month, year, hour, minute));
                };

                //Instantiate the time picker dialog
                int style = AlertDialog.THEME_HOLO_DARK;
                TimePickerDialog timePickerDialog = new TimePickerDialog(SetTimeLocationUI.this, style, onTimeSetListener, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.updateTime(Calendar.HOUR_OF_DAY, Calendar.MINUTE);
                timePickerDialog.show();
            };
            //Instantiate the date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(SetTimeLocationUI.this, onDateSetListener, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
            datePickerDialog.setTitle("Select Date");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH,6);

            //Allowing user to choose only from the date to six days from that date
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });
    }

    /**
     * This method uses the built-in Time class to retrieve the user's current date and time and stores it in Firebase if users choose to use it.
     */
    public void getCurrentDateTime(){
        Time today = new Time(Time.getCurrentTimezone());
        //Alert the user if system failed to retrieve the user's current date and time
        if(today == null){
            Toast.makeText(this, "Unable to obtain current date and time", Toast.LENGTH_SHORT).show();
        }
        //Set the day to today
        today.setToNow();

        //Retrieve the date and time
        currentYear = Integer.toString(today.year);
        currentMonth = Integer.toString(today.month + 1);
        currentDay = Integer.toString(today.monthDay);
        currentHour = Integer.toString(today.hour);
        currentMinute = Integer.toString(today.minute);
        currentSecond = Integer.toString(today.second);

        //Formatting
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

        //Storing in Firebase
        mDatabase.child(userID).child("Account").child("currentDateTime").setValue(dateTime).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SetTimeLocationUI.this, "Unable to store current date and time", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method builds an alert dialog to alert users that they have not set a time
     * by either choosing in the "Enter a different Date & Time" widget or ticking the "Use current Date and Time" checkbox
     */
    private void showTimeDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SetTimeLocationUI.this);
        builder1.setTitle("Choose a Time");
        builder1.setMessage("Please select a time or click the 'Use Current Time' checkbox to continue");
        builder1.setPositiveButton("Ok", (dialog, which) -> dialog.cancel());
        builder1.show();
    }

    /**
     * This method builds an alert dialog to alert users that they have not set a location
     * by either choosing in the "Search" widget or ticking the "Use current location" checkbox.
     */
    private void showLocDialog(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(SetTimeLocationUI.this);
        builder2.setTitle("Choose a Location");
        builder2.setMessage("Please select a location or click the 'Use Current Location' checkbox to continue");
        builder2.setPositiveButton("Ok", (dialog, which) -> dialog.cancel());
        builder2.show();
    }

    /**
     * This method retrieves the user's current location using the Google Play Services Fused Location Provider API.
     * It then stores the user's current location in Firebase.
     */
    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        //Linking with the Google Play Services Fused Location Provider API
        LocationServices.getFusedLocationProviderClient(SetTimeLocationUI.this).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(SetTimeLocationUI.this).removeLocationUpdates(this);

                //System managed to retrieve the user's current location
                if(locationResult != null && locationResult.getLocations().size() > 0)
                {
                    int index = locationResult.getLocations().size() - 1;

                    //Retrieving the location
                    double latitude = locationResult.getLocations().get(index).getLatitude();
                    double longitude = locationResult.getLocations().get(index).getLongitude();

                    //Formatting
                    userLocation = "lat/lng: (" + latitude + ", " + longitude + ")";

                    //Storing the current location in Firebase
                    mDatabase.child(userID).child("Account").child("currentLocation").setValue(userLocation).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SetTimeLocationUI.this, "Unable to store current location", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }, Looper.getMainLooper());
    }
}