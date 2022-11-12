/*
test
 */
package com.example.application.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
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
import androidx.core.content.ContextCompat;

import com.example.application.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * ShowMap is a class that displays a map with the user's current location.
 * <p> This class first checks for location permissions and requests for permission if not granted.
 * <p> It also checks if the user's GPS has been enabled and prompts the user to turn on if it is off.
 * <p> This class acts as the homepage of our application and users can get their restaurant reccomendations by clicking the "Get Recommendations" button.
 * @author celest
 * @version 1.0
 * @since 2022-11-11
 */
public class ShowMap extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {

    //Initialising all widgets
    private Button getRecommendations;
    private ImageView settings;

    //Initialising variables needed to check user's location permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private LocationRequest locationRequest;
    private boolean locationPermissionGranted;
    private LocationManager locationManager;

    //Initialising a map variable and setting the default zoom
    private GoogleMap gMap;
    private static final int DEFAULT_ZOOM = 15;

    /**
     * All variables are set with values in this method.
     * <p>The InstanceState of this activity is saved.
     * @param savedInstanceState    The instance state of the current activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //we use the activity_show_map layout
        setContentView(R.layout.activity_show_map);

        //create a locationRequest to be passed in as a parameter for the fused location provider client
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        //links the app to the phone's settings
        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        getRecommendations = findViewById(R.id.mapToRec);
        getRecommendations.setOnClickListener(this);

        settings = findViewById(R.id.settings);
        settings.setOnClickListener(this);
    }

    /**
     * Gets the Google Map object.
     * @param googleMap The generated map fragment
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        //check if user has turned on GPS and ask to turn it on if it is off
        isGPSEnabled();
        //check if user has allowed location permissions and request permission if not allowed.
        getLocationPermission();
    }

    /**
     * Links to a new event.
     * @param v The activity_show_map layout
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //user clicks on the "Get Recommendations" button
            case R.id.mapToRec: {
                startActivity(new Intent(this, SetTimeLocationUI.class));
                break;
            }
            //user clicks on the "Settings" button
            case R.id.settings: {
                startActivity(new Intent(this, SettingsPageUI.class));
                break;
            }
        }
    }

    /**
     * Checks if the user has granted location permissions to the app.
     * <p> Requests for permission if user has not granted.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted = true;
            Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            //update map
            updateLocationUI();
        }
        else{
            //display a popup to get user's permission
            ActivityCompat.requestPermissions(ShowMap.this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Gets the user's chosen permission decision.
     * <p>If permission is not granted, an alert message pops up.
     * <p>This method is invoked for every call of requestPermissions.
     * @param requestCode   The request code passed in requestPermissions
     * @param permissions   The requested permissions
     * @param grantResults  The grant results for the corresponding permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                } else {
                    locationPermissionGranted = false;
                    Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
                    showLocationDialog();
                }
                updateLocationUI();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Updates the map by turning on or off the "Get Recommendations" button
     * and "MyLocationButton" and the user's current location in the map.
     */
    private void updateLocationUI(){
        if (gMap == null){
            return;
        }
        try {
            if (locationPermissionGranted) {
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
                getRecommendations.setEnabled(true);
                getDeviceLocation();
            } else {
                //permission not granted so users cannot continue using the app
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                getRecommendations.setEnabled(false);
            }
        }catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Shows an alert to the user by explaining the rationale behind requiring their location permission.
     * <p>Prompts the user to allow location permission in their phone settings and closes the app.
     */
    private void showLocationDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowMap.this);
        builder.setTitle("Need Location Permission!");
        builder.setMessage("This app needs location permission. Close the app and allow location permission in phone settings.");
        builder.setPositiveButton("Close App", (dialog, which) -> {
            dialog.cancel();
            finishAffinity();
        });
        final AlertDialog permissionAlert = builder.create();
        permissionAlert.show();
    }

    /**
     * Checks if the user has turned on their GPS.
     * Alerts the user if their GPS is not on.
     * @return  Gps is enabled or not
     */
    private boolean isGPSEnabled()
    {
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

    /**
     * Shows an alert to the user that their GPS is off.
     * Requests for the user to turn it on by linking to the location settings in their phone.
     * Shows another alert to explain the rationale of requiring GPS services if user still keeps their GPS off.
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setTitle("GPS is required!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        buildAlertMessageNeedGps();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Shows an alert by explaining the rationale behind requiring the user to turn on their GPS.
     */
    private void buildAlertMessageNeedGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location services are required to continue using the app. Do you want to enable it?")
                .setCancelable(false)
                .setTitle("GPS")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        startActivity(new Intent(ShowMap.this, LoginUI.class));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Gets the user's current location and moves the camera on the map to this location.
     */
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
                        LatLng location = new LatLng(latitude, longitude);
                        //zoom into the user's current location
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                location, DEFAULT_ZOOM));
                    }
                    else{
                        //permission is not granted, so an alert must be shown
                        showLocationDialog();
                    }
                }
            }, Looper.getMainLooper());
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

}