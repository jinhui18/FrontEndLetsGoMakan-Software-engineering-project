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
import android.os.AsyncTask;
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

import com.example.application.backend.control.others.AsyncResponse;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class ShowMap extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback, AsyncResponse {

    private Button getRecommendations;
    private Button testButton;
    private ImageView settings;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;
    private boolean locationPermissionGranted;

    private String userID;

    private LocationRequest locationRequest;
    private static final String TAG = ShowMap.class.getSimpleName();
    private GoogleMap gMap;

    ArrayList<String> userLocation = new ArrayList<>();
    ArrayList<JSONObject>restaurantDetails = new ArrayList<JSONObject>();

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private LocationManager locationManager;

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

        locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        getRecommendations = (Button) findViewById(R.id.mapToRec);
        getRecommendations.setOnClickListener(this);

        settings = (ImageView) findViewById(R.id.settings);
        settings.setOnClickListener(this);

        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
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
                startActivity(new Intent(this, Settings.class));
            }
            case R.id.testButton:{
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=1.3420045%2C103.7118808&radius=1500&type=restaurant&key=AIzaSyBvQjZ15jD__Htt-F3TGvMp_ZWNw79JZv0";
                new PlaceTask().execute(url);

                try{
                    for (int i = 0; i < restaurantDetails.size(); i++)
                    {
                        System.out.println("Name: " + restaurantDetails.get(i).getString("name"));
                        System.out.println("Address: " + restaurantDetails.get(i).getString("formatted_address"));
                        System.out.println("Ratings: " + restaurantDetails.get(i).getString("rating") + "\n");
                        System.out.println(restaurantDetails.get(i).toString() + "\n");
                    }
                }catch(JSONException e){e.printStackTrace();}
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

    @Override
    public void processFinish(ArrayList<JSONObject> output) {restaurantDetails = output;}

    public class PlaceTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {

            String data = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                data = stringBuilder.toString();

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new GetPlaceIDs().execute(s);
        }
    }

    public class GetPlaceIDs extends AsyncTask<String, String, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> placeIDList = new ArrayList<String>();
            try {
                JSONObject nearbySearchResult = new JSONObject(strings[0]);
                JSONArray resultsArray = nearbySearchResult.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    placeIDList.add(resultsArray.getJSONObject(i).getString("place_id"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return placeIDList;
        }


        protected void onPostExecute(ArrayList<String> strings) {
            new aGetPlaceDetails().execute(strings);
            /*GetPlaceDetails(strings);*/
        }
    }

    public class aGetPlaceDetails extends AsyncTask<ArrayList<String>, String, ArrayList<JSONObject>> {

        AsyncResponse delegate = ShowMap.this;

        @Override
        protected ArrayList<JSONObject> doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> placeIDs = arrayLists[0];
            ArrayList<JSONObject> result = new ArrayList<JSONObject>();

            for (int i = 0; i < placeIDs.size(); i++) {
                String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeIDs.get(i) +
                        "&fields=name%2Crating%2Cformatted_address&key=AIzaSyBvQjZ15jD__Htt-F3TGvMp_ZWNw79JZv0";

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");


                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    ResponseBody response = client.newCall(request).execute().body();
                    JSONObject responseObject = new JSONObject(response.string());
                    JSONObject resultObject = responseObject.getJSONObject("result");
                    result.add(resultObject);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
            delegate.processFinish(jsonObjects);
        }
    }
}