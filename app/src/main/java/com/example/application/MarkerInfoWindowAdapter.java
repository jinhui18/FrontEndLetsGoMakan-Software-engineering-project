package com.example.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.entity.Restaurant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final Context context;
    private final ArrayList<Restaurant> restaurant_list;
    private Restaurant restaurant;

    ImageView image;

    public MarkerInfoWindowAdapter(Context context, ArrayList<Restaurant> restaurant_list) {
        this.context = context.getApplicationContext();
        this.restaurant_list = restaurant_list;
    }

    @Override
    public View getInfoWindow(@NonNull Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View v =  inflater.inflate(R.layout.popup, null);

        image = v.findViewById(R.id.restaurant_image);
        TextView restaurant_name = v.findViewById(R.id.restaurant_name);
        TextView restaurant_travel = v.findViewById(R.id.restaurant_travel);
        TextView restaurant_details = v.findViewById(R.id.restaurant_details);

        Picasso.get().setLoggingEnabled(true);

        String name = arg0.getTitle();
        System.out.println(name);

        for(int index=0; index<restaurant_list.size(); index++){
            if(name.equals(restaurant_list.get(index).getName())){
                restaurant = restaurant_list.get(index);
            }
        }
        System.out.println(restaurant.getName());

        Picasso.get()
                .load(restaurant.getImage())
                .into(image);

        restaurant_name.setText(restaurant.getName());

        restaurant_travel.setText(String.valueOf(restaurant.getTravellingTime()));

        restaurant_details.setText("See more details");

        return v;
    }
}