package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowRestaurantMap extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    String userID;

    boolean useCurLoc;

    private static final int DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_restaurant_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        userID = mAuth.getCurrentUser().getUid();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        getUserLocation();
        showOnMap();
    }

    public void getUserLocation() {
        mDatabase.child(userID).child("Account").child("Use Current Location")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        useCurLoc = snapshot.getValue(boolean.class);
                        showLocation(useCurLoc);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ShowRestaurantMap.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showLocation(boolean useCurLoc) {
        if (useCurLoc == true) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlertDialog();
                return;
            }
            gMap.setMyLocationEnabled(true);

            mDatabase.child(userID).child("Account").child("Current Location")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String data = snapshot.getValue(String.class);
                            double latitude = Double.parseDouble(data.substring(data.indexOf("(") + 1, data.indexOf(",")));
                            double longitude = Double.parseDouble(data.substring(data.indexOf(",") + 1, data.indexOf(")")));
                            LatLng location = new LatLng(latitude, longitude);
                            gMap.setMyLocationEnabled(true);
                            gMap.getUiSettings().setMyLocationButtonEnabled(true);
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    location, DEFAULT_ZOOM));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ShowRestaurantMap.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            mDatabase.child(userID).child("Account").child("Chosen Location")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String data = snapshot.getValue(String.class);
                            double latitude = Double.parseDouble(data.substring(data.indexOf("(") + 1, data.indexOf(",")));
                            double longitude = Double.parseDouble(data.substring(data.indexOf(",") + 1, data.indexOf(")")));
                            LatLng location = new LatLng(latitude, longitude);
                            gMap.setMyLocationEnabled(true);
                            gMap.getUiSettings().setMyLocationButtonEnabled(true);
                            gMap.addMarker(new MarkerOptions().position(location).title("Chosen Location"));
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    location, DEFAULT_ZOOM));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ShowRestaurantMap.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void showOnMap() {
        double latitude = 1.347078972102637;
        double longitude = 103.68033412545849;
        LatLng location = new LatLng(latitude, longitude);
        LatLng location2 = new LatLng(1.34729,103.68080);
        LatLng location3 = new LatLng(1.34268, 103.68240);
        gMap.addMarker(new MarkerOptions().position(location).title("McDonald's"));
        gMap.addMarker(new MarkerOptions().position(location2).title("KFC"));
        gMap.addMarker(new MarkerOptions().position(location3).title("South Spine Fine Foods"));
    }

    public void showAlertDialog(){

    }
}