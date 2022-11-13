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
 * ShowMap is a View class that displays a map with the user's current location.
 * <p> This class first checks for location permissions and requests for permission if not granted.
 * <p> It also checks if the user's GPS has been enabled and prompts the user to turn on if it is off.
 * <p> This class acts as the homepage of our application and users can get their restaurant recommendations by clicking the "Get Recommendations" button.
 * @author celest
 * @version 1.0
 * @since 2022-11-11
 */
public class ShowMap extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {

    //Widgets
    private Button getRecommendations;
    private ImageView settings;

    //Initialising variables needed to check user's location permission
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private LocationRequest locationRequest;
    private boolean locationPermissionGranted;
    private LocationManager locationManager;

    //Map variable and setting the default zoom
    private GoogleMap gMap;
    private static final int DEFAULT_ZOOM = 15;

    /**
     * This method is called after the activity has launched but before it starts running.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in #onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //we use the activity_show_map layout
        setContentView(R.layout.activity_show_map);

        //Instantiating widgets
        getRecommendations = findViewById(R.id.mapToRec);
        settings = findViewById(R.id.settings);

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

        //Allow users to click on the "Get Recommendations" button
        getRecommendations.setOnClickListener(this);

        //Allow users to click on the "Settings" icon
        settings.setOnClickListener(this);
    }

    /**
     * Gets the Google Map object.
     * <p>Then, this method checks if the user has enabled their GPS. If not, the system will prompt the user to turn it on
     * by redirecting the user to their phone settings app if they decide to turn it on.
     * <p>This method also checks if location permission has been granted by the user. If not, the system will request for permission using an alert dialog.
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
     * This method contains the logic for the clickable buttons in this activity class. The clickable buttons are "Get Recommendations" and "Settings" icon.
     * <p>When "Settings" icon is pressed, the system redirects the user to the SettingsPageUI class.
     * <p>When "Get Recommendations" button is pressed, the system redirects the user to the SetTimeLocationUI class.
     * @param v  is the ID of the button that user selects.
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
     * <p>Requests for permission if user has not granted.
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
                //Location permissions have been checked so the map needs to be updated
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
            //When location permission is granted, the map will show the user's current location and allow users to click the "Get Recommendations" button.
            if (locationPermissionGranted) {
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(true);
                getRecommendations.setEnabled(true);
                getDeviceLocation();
            }
            //When location permission is not granted, the map will not show the user's current location and disable the "Get Recommendations" button.
            else {
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
            //GPs is turned on
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
     * <p>Requests for the user to turn it on by linking to the location settings in their phone.
     * <p>Shows another alert to explain the rationale of requiring GPS services if user still keeps their GPS off.
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
            //Links to the Google Play Services Fused Location Provider API
            LocationServices.getFusedLocationProviderClient(ShowMap.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(ShowMap.this).removeLocationUpdates(this);
                    //Check if the system retrieved the user's current location
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