package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayRestaurant extends AppCompatActivity {

    private ImageView imageView_restaurant, imageView_bookmarked;
    private TextView textView_restaurant_name,textView_restaurant_address,textView_restaurant_opening_hours_time,textView_restaurant_crowd_level_value;
    private TextView textView_restaurant_travelling_time, textView_restaurant_review, textView_restaurant_link;
    private TextView textView_website_link, textView_make_reservation, textView_get_direction;

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
        textView_get_direction = findViewById(R.id.textView_get_direction);

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String restaurant_url = intent.getExtras().getString("restaurant_url");
        String restaurant_name = intent.getExtras().getString("restaurant_name");
        String restaurant_address = intent.getExtras().getString("restaurant_address");
        String restaurant_opening_hours_time = intent.getExtras().getString("restaurant_opening_hours_time");
        String restaurant_crowd_level_value = intent.getExtras().getString("restaurant_crowd_level_value");
        String restaurant_travelling_time = intent.getExtras().getString("restaurant_travelling_time");

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


}
