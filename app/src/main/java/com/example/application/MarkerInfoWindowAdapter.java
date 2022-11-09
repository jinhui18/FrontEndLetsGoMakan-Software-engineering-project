package com.example.application;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.application.backend.entity.Restaurant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private Restaurant restaurant;

    ImageView image;

    public MarkerInfoWindowAdapter(Context context, Restaurant restaurant) {
        this.context = context.getApplicationContext();
        this.restaurant = restaurant;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v =  inflater.inflate(R.layout.popup, null);

        image = (ImageView) v.findViewById(R.id.restaurant_image);

        Picasso.get().setLoggingEnabled(true);

        Picasso.get()
                .load(restaurant.getImage())
                .into(image);

        TextView restaurant_name = (TextView) v.findViewById(R.id.restaurant_name);
        restaurant_name.setText(restaurant.getName());

        TextView restaurant_travel = (TextView) v.findViewById(R.id.restaurant_travel);
        restaurant_travel.setText(Float.toString(restaurant.getTravellingTime()));

        TextView restaurant_details = (TextView) v.findViewById(R.id.restaurant_details);
        restaurant_details.setText("See more details");

        return v;
    }
}