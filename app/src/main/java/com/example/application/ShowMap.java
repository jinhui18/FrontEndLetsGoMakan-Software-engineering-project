package com.example.application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.application.view.ChangePreferences;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShowMap extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {

    private Button getRecommendations;
    private ImageView settings;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;
    private boolean locationPermissionGranted;

    private String userID;

    private LocationRequest locationRequest;
    private static final String TAG = ShowMap.class.getSimpleName();
    private GoogleMap gMap;

    ArrayList<String> userLocation = new ArrayList<>();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        userID = mAuth.getCurrentUser().getUid();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        getRecommendations = (Button) findViewById(R.id.mapToRec);
        getRecommendations.setOnClickListener(this);

        settings = (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        getLocationPermission();
        isGPSEnabled();
        getDeviceLocation();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mapToRec: {
                startActivity(new Intent(this, SetTimeLocation.class));
                break;
            }
            case R.id.settings: {
                startActivity(new Intent(this, ChangePreferences.class));
            }
        }
    }

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted = true;
            Toast.makeText(this, "Location permission granted.", Toast.LENGTH_SHORT).show();
        }
        else{
            ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                locationPermissionGranted = true;
                Toast.makeText(this, "Location permission granted.", Toast.LENGTH_SHORT).show();
            }
        } else {
            showLocationDialog();
        }
        updateLocationUI();
    }

    @SuppressLint("MissingPermission")
    private void updateLocationUI(){
        if (gMap == null){
            return;
        }
        try {
            if (locationPermissionGranted) {
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                getLocationPermission();
            }
        }catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation(){
        try{
            LocationServices.getFusedLocationProviderClient(ShowMap.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(ShowMap.this).removeLocationUpdates(this);

                    if(locationResult != null && locationResult.getLocations().size() > 0)
                    {
                        int index = locationResult.getLocations().size() - 1;
                        double latitude = locationResult.getLocations().get(index).getLatitude();
                        double longitude = locationResult.getLocations().get(index).getLongitude();
                        userLocation.add(Double.toString(latitude));
                        userLocation.add(Double.toString(longitude));
                        mDatabase.child(userID).child("Current Location").setValue(userLocation);
                        LatLng location = new LatLng(latitude, longitude);
                        // Add a marker to current location and move the camera
                        gMap.addMarker(new MarkerOptions().position(location).title("Your current location"));
                        gMap.setMyLocationEnabled(true);
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                location, DEFAULT_ZOOM));
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

    private void showLocationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowMap.this);
        builder.setTitle("Need Location Permission!");
        builder.setMessage("This app needs location permission. Restart the app and allow location permission.");
        builder.setPositiveButton("Restart App", (dialog, which) -> {
            dialog.cancel();
            finish();
            startActivity(getIntent());
        });
    }

    private void turnOnGPS() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(ShowMap.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Toast.makeText(ShowMap.this, "location must be enabled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });
    }

    private boolean isGPSEnabled()
    {
        LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        boolean isEnabled = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(isEnabled == true) {
                Toast.makeText(this, "GPS is on", Toast.LENGTH_SHORT).show();
            }
        }
        if(isEnabled == false){
            buildAlertMessageNoGps();
        }
        return isEnabled;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}