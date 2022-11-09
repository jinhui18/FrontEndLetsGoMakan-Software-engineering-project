package com.example.application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DisplayRestaurant extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView imageView_restaurant, imageView_bookmarked;
    private TextView textView_restaurant_name, textView_restaurant_address, textView_restaurant_opening_hours_time, textView_restaurant_crowd_level_value;
    private TextView textView_restaurant_travelling_time, textView_restaurant_review, textView_restaurant_link;
    private TextView textView_website_link, textView_make_reservation, textView_get_direction;

    private String restaurant_url;
    private String restaurant_name;
    private String restaurant_address;
    private String restaurant_opening_hours_time;
    private String restaurant_crowd_level_value;
    private String restaurant_travelling_time;

    private GoogleMap gMap;
    private double restaurant_latitude;
    private double restaurant_longitude;

    private static final int DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_restaurant);

        imageView_restaurant = findViewById(R.id.imageView_restaurant);
        imageView_bookmarked = findViewById(R.id.imageView_bookmarked);

        textView_restaurant_name = findViewById(R.id.textView_restaurant_name);
        textView_restaurant_address = findViewById(R.id.textView_restaurant_address);
        textView_restaurant_opening_hours_time = findViewById(R.id.textView_restaurant_opening_hours_time);
        textView_restaurant_crowd_level_value = findViewById(R.id.textView_restaurant_crowd_level_value);
        textView_restaurant_travelling_time = findViewById(R.id.textView_restaurant_travelling_time);

        // all of the things below i cant get yet
        textView_restaurant_review = findViewById(R.id.textView_restaurant_review);
        textView_restaurant_link = findViewById(R.id.textView_restaurant_link);
        textView_website_link = findViewById(R.id.textView_website_link);
        textView_make_reservation = findViewById(R.id.textView_make_reservation);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //map stuff
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapRestaurant);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        Intent intent = getIntent();
        restaurant_url = intent.getExtras().getString("restaurant_url");
        restaurant_name = intent.getExtras().getString("restaurant_name");
        restaurant_address = intent.getExtras().getString("restaurant_address");
        restaurant_opening_hours_time = intent.getExtras().getString("restaurant_opening_hours_time");
        restaurant_crowd_level_value = intent.getExtras().getString("restaurant_crowd_level_value");
        restaurant_travelling_time = intent.getExtras().getString("restaurant_travelling_time");
        restaurant_latitude = intent.getExtras().getDouble("restaurant_latitude");
        restaurant_longitude = intent.getExtras().getDouble("restaurant_longitude");

        //change the image of the restaurant
        /*
        Picasso.get()
                .load(restaurant_url)
                .fit()
                .into(imageView_restaurant);

         */

        textView_restaurant_name.setText(restaurant_name);
        textView_restaurant_address.setText(restaurant_address);

        // need to modify the string, i need to see what format opening hours is stored in database first
        // all three data below is the same, need change formatting
        textView_restaurant_opening_hours_time.setText(restaurant_opening_hours_time);
        textView_restaurant_crowd_level_value.setText(restaurant_crowd_level_value);
        //textView_restaurant_travelling_time.setText(restaurant_travelling_time);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        LatLng location = new LatLng(restaurant_latitude, restaurant_longitude);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            gMap.setMyLocationEnabled(true);
            gMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        gMap.addMarker(new MarkerOptions().position(location).title(restaurant_name));
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                location, DEFAULT_ZOOM));
    }
}
