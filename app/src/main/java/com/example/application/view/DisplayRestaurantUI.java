package com.example.application.view;

import static java.lang.Math.floor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.application.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

/**
 * @author Jin Hui
 * This class display the restaurant details of a selected restaurant from DisplayRestaurntListUI.
 * @version 1.0
 * @since 2022-11-11
 */
public class DisplayRestaurantUI extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView imageView_restaurant;
    private TextView textView_restaurant_name, textView_restaurant_address, textView_restaurant_opening_hours_time, textView_restaurant_crowd_level_value;
    private TextView textView_restaurant_travelling_time, textView_price_level, textView_takeout, textView_phone_number;
    private RatingBar ratingBar;
    private Button buttonWebsite;

    private String restaurant_image_url, restaurant_website_url, phone_number;
    private String restaurant_name;
    private String restaurant_address;
    private boolean restaurant_opening_hours_time;
    private int restaurant_crowd_level_value, restaurant_price_level;
    private String restaurant_travelling_time;
    private float ratings;
    private Boolean restaurant_takeout;


    private GoogleMap gMap;
    private double restaurant_latitude;
    private double restaurant_longitude;

    private static final int DEFAULT_ZOOM = 15;

    /**
     * This method is called after the activity has launched but before it starts running.
     * This method will initialise all widgets in display_restaurant activity.
     * This method will start another a new activity in DisplayRestaurantListUI class when the system successfully push Restaurant into our DataBase.
     * Else, it will continue to show a loading page with a progress bar running.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in #onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_restaurant);

        //Initialise all widgets
        imageView_restaurant = findViewById(R.id.imageView_restaurant);
        textView_restaurant_opening_hours_time = findViewById(R.id.textView_restaurant_opening_hours_text);
        textView_restaurant_crowd_level_value = findViewById(R.id.textView_restaurant_crowd_level_text);
        textView_phone_number = findViewById(R.id.textView_phone_number);


        textView_restaurant_name = findViewById(R.id.textView_restaurant_name);
        textView_restaurant_address = findViewById(R.id.textView_restaurant_address);
        textView_restaurant_travelling_time = findViewById(R.id.textView_restaurant_travelling_time);
        ratingBar = findViewById(R.id.ratingBar);
        buttonWebsite = findViewById(R.id.buttonWebsite);

        textView_price_level = findViewById(R.id.textView_price_level);
        textView_takeout = findViewById(R.id.textView_takeout);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //map stuff
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapRestaurant);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        //Getting the intent and extracting all values stored in the intent
        Intent intent = getIntent();
        restaurant_image_url = intent.getExtras().getString("restaurant_url");
        restaurant_name = intent.getExtras().getString("restaurant_name");
        restaurant_address = intent.getExtras().getString("restaurant_address");
        restaurant_opening_hours_time = intent.getExtras().getBoolean("restaurant_opening_hours_time");
        restaurant_crowd_level_value = intent.getExtras().getInt("restaurant_crowd_level_value");
        restaurant_travelling_time = intent.getExtras().getString("restaurant_travelling_time");
        restaurant_latitude = intent.getExtras().getDouble("restaurant_latitude");
        restaurant_longitude = intent.getExtras().getDouble("restaurant_longitude");
        ratings = intent.getExtras().getFloat("ratings");
        restaurant_price_level = intent.getExtras().getInt("restaurant_price_level");
        restaurant_website_url = intent.getExtras().getString("restaurant_website");
        restaurant_takeout = intent.getExtras().getBoolean("restaurant_takeout");
        phone_number = intent.getExtras().getString("restaurant_phone_number");


        //change the image of the restaurant
        Picasso.get()
                .load(restaurant_image_url)
                .into(imageView_restaurant);

        //Setting travelling time
        textView_restaurant_travelling_time.setText(restaurant_travelling_time + " mins away");
        System.out.println("travelling time in disy rest ui for " + restaurant_name+ " is " + restaurant_travelling_time);


        //Setting restaurant name
        textView_restaurant_name.setText(restaurant_name);

        //Setting restaurant address
        textView_restaurant_address.setText(restaurant_address);

        //Setting price level of restaurant
        int price_level = restaurant_price_level;
        String price_level_text = "$";
        switch(price_level){
            case 0:
            case 1:
                price_level_text = "Price: $";
                break;
            case 2:
                price_level_text = "Price: $$";
                break;
            case 3:
                price_level_text = "Price: $$$";
                break;
            case 4:
                price_level_text = "Price: $$$$";
                break;
        }
        textView_price_level.setText("Price: " + price_level_text);

        //Setting price level of restaurant
        ratingBar.setRating((float) floor(ratings));
        if (restaurant_takeout){
            textView_takeout.setText("Take out: Available" );
        }else{
            textView_takeout.setText("Take out: Not available" );
        }

        //Setting open/closed status hours of restaurant
        if (restaurant_opening_hours_time == true){
            textView_restaurant_opening_hours_time.setText("Open/Closed: " + "Open");
        }else{
            textView_restaurant_opening_hours_time.setText("Open/Closed: " + "Closed");
        }

        //Setting crowd level of restaurant
        textView_restaurant_crowd_level_value.setText("Crowd Level :" + restaurant_crowd_level_value + "/5");

        //Setting phone number of restaurant and allowing it to redirect to phone app with number keyed in
        textView_phone_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (phone_number != null) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" +phone_number));
                        startActivity(callIntent);
                    }else{
                        Toast.makeText(DisplayRestaurantUI.this, "No phone number.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        //Setting redirection of user to the website of the restaurant
        buttonWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement URL
                if (restaurant_website_url != null){
                    Uri uri = Uri.parse(restaurant_website_url); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(DisplayRestaurantUI.this, "Restaurant does not have a website.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    /**
     * This hook is called whenever an item in your options menu is selected.
     * The purpose of this method is to not restart the activity in DisplayRestaurantListUI when pressing the back button from DisplayRestaurantUI
     * @param item If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in #onSaveInstanceState
     */
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method obtains the map object when map is loaded.
     * It displays the current location of user and the pin of the location of restaurant chosen.
     * @param googleMap A non-null instance of a GoogleMap associated with the MapFragment or MapView that defines the callback.
     */
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
