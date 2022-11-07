package com.example.application;

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

import com.example.application.view.ChangePreferencesUI;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class ShowMap extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback {

    private Button getRecommendations;
    private ImageView settings;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;
    private static boolean locationPermissionGranted;

    private LocationRequest locationRequest;
    private static final String TAG = ShowMap.class.getSimpleName();
    private GoogleMap gMap;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

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
        updateLocationUI();
    }

    @Override
    public void onStart() {
        super.onStart();
        isGPSEnabled();
        getLocationPermission();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mapToRec: {
                startActivity(new Intent(this, SetTimeLocation.class));
                break;
            }
            case R.id.settings: {
                startActivity(new Intent(this, ChangePreferencesUI.class));
                break;
            }
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted = true;
            Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            updateLocationUI();
        }
        else{
            ActivityCompat.requestPermissions(ShowMap.this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                locationPermissionGranted = true;
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            locationPermissionGranted = false;
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            showLocationDialog();
            return;
        }
        updateLocationUI();
    }*/

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
                gMap.setMyLocationEnabled(false);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);
                getRecommendations.setEnabled(false);
            }
        }catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

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
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                location, DEFAULT_ZOOM));
                    }
                    else{
                        showLocationDialog();
                    }
                }
            }, Looper.getMainLooper());
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
}