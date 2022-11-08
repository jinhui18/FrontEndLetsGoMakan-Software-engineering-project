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
import java.net.URLConnection;
import java.net.URLEncoder;
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

    private String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36";
    private JSONArray jpopularTimes = new JSONArray();
    private ArrayList<JSONArray> popTimeList = new ArrayList<JSONArray>();
    private ArrayList<String> userLocation = new ArrayList<>();
    private ArrayList<JSONObject> restaurantDetails = new ArrayList<JSONObject>();


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

        testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.mapToRec: {
                startActivity(new Intent(this, SetTimeLocation.class));
                break;
            }
            case R.id.settings: {
                startActivity(new Intent(this, Settings.class));
            }
            case R.id.testButton: {
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=1.3420045%2C103.7118808&radius=1500&type=restaurant&key=AIzaSyBvQjZ15jD__Htt-F3TGvMp_ZWNw79JZv0";
                new PlaceTask().execute(url);

                try {
                    for (int i = 0; i < restaurantDetails.size(); i++) {
                        ArrayList<String> forSearchData = new ArrayList<String>();
                        forSearchData.add(restaurantDetails.get(i).getString("name"));
                        forSearchData.add(restaurantDetails.get(i).getString("formatted_address"));
                        new getSearchData().execute(forSearchData);
                        for (int j = 0; j < popTimeList.size(); j++)
                        {
                            System.out.println(popTimeList.get(j).toString());
                        }

                    }
                     /*   popTimeList.add(getPT(getSearchData(restaurantDetails.get(i).getString("name"),restaurantDetails.get(i).getString("formatted_address"))));
                    }

                    for(int i = 0; i < popTimeList.size();i++){
                        JSONArray toPrint = popTimeList.get(i);
                        System.out.println(toPrint.toString());
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            Toast.makeText(this, "Location permission granted.", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                locationPermissionGranted = true;
                Toast.makeText(this, "Location permission granted.", Toast.LENGTH_SHORT).show();
            }
        } else {
            showLocationDialog();
        }
        updateLocationUI();
    }

    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (gMap == null) {
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
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        try {
            LocationServices.getFusedLocationProviderClient(ShowMap.this).requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices.getFusedLocationProviderClient(ShowMap.this).removeLocationUpdates(this);

                    if (locationResult != null && locationResult.getLocations().size() > 0) {
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
                    } else {
                        Log.d(TAG, "Location permission not granted... Exiting the app");
                        finish();
                        startActivity(getIntent());
                    }
                }
            }, Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void showLocationDialog() {
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

    private boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isEnabled = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isEnabled == true) {
                Toast.makeText(this, "GPS is on", Toast.LENGTH_SHORT).show();
            }
        }
        if (isEnabled == false) {
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

    @Override
    public void processFinish(ArrayList<JSONObject> output) {
        restaurantDetails = output;
    }

    @Override
    public void processFinishJA(ArrayList<JSONArray> output) {
        popTimeList = output;
    }

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

            for (int i = 0; i < 3; i++) {
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

    public class getSearchData extends AsyncTask<ArrayList<String>, String, ArrayList<JSONArray>> {


        AsyncResponse delegate = ShowMap.this;

        @Override
        protected ArrayList<JSONArray> doInBackground(ArrayList<String>... stringsL) {
            JSONArray data = new JSONArray();
            try {
                ArrayList<String> strings = stringsL[0];
                String name = strings.get(0);
                String formattedAddress = strings.get(1);

                String tbm = "map";
                String hl = "sg";
                String tch = "1";
                String q = name + " " + formattedAddress;

                String appender = "tbm=" + tbm + "&hl=" + hl + "&tch=" + tch + "&q=" + URLEncoder.encode(q, "UTF-8");
                String searchUrl = "https://www.google.de/search?" + appender;

                String json = downloadWeirdStuff(searchUrl);

                int jEnd = json.lastIndexOf("}");
                if (jEnd >= 0)
                    json = json.substring(0, jEnd + 1);

                //now parse

                JSONObject jb = new JSONObject(json);

                //now read
                String jdata = (String) jb.get("d"); //read the data String
                jdata = jdata.substring(4); //cut it to get the JSONArray again

                //reparse
                data = new JSONArray(jdata);

            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<JSONArray> popTimeL = new ArrayList<JSONArray>();
            JSONArray popTime = new JSONArray();
            try {
                if (((JSONArray) ((JSONArray) ((JSONArray) data.get(0)).get(1)).get(0)).length() > 11 &&
                        (JSONArray) ((JSONArray) ((JSONArray) ((JSONArray) data.get(0)).get(1)).get(0)) != null) {
                    //get information array
                    JSONArray info = (JSONArray) ((JSONArray) ((JSONArray) ((JSONArray) data.get(0)).get(1)).get(0))
                            .get(14);

                    if (info.get(84) == null) {
                        System.out.println("No information on popular times available!");
                    } else {
                        popTime = (JSONArray) ((JSONArray) info.get(84)).get(0); //get popular times
                        for(int i = 0; i < popTime.length(); i++)
                        {
                            popTimeL.add((JSONArray)popTime.get(i));
                        }
                        return popTimeL;
                    }
                } else {
                    System.out.println("Empty data");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return popTimeL;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONArray> jsonArray) {
            delegate.processFinishJA(jsonArray);
        }


    }
    public String downloadWeirdStuff(String url) throws IOException
    {
        URL url1 = new URL(url);
        URLConnection connection = url1.openConnection();
        connection.setRequestProperty("User-Agent", userAgent);
        connection.connect();

        InputStream response = connection.getInputStream();

        String json = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(response, "utf-8"), 8);
        StringBuilder sbuild = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sbuild.append(line);
        }
        response.close();
        json = sbuild.toString();
        return json;
    }
}