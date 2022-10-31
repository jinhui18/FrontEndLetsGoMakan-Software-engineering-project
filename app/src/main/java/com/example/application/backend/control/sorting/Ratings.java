package com.example.application.backend.control.sorting;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

public class Ratings extends SortingCriteria {

    public Ratings() {}

    public void sort(ArrayList<Restaurant> restaurantList) {
        ArrayList<Restaurant> r1 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r2 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r3 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r4 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r5 = new ArrayList<Restaurant>();

        // add same rating restaurant into separate arrays
        for (int i=0; i<restaurantList.size(); i++) {
            Restaurant restaurant = restaurantList.get(i);
            int rating = restaurant.getRatings();
            if (rating==1) r1.add(restaurant);
            else if (rating==2) r2.add(restaurant);
            else if (rating==3) r3.add(restaurant);
            else if (rating==4) r4.add(restaurant);
            else r5.add(restaurant);
        }

        // sort each array by travel time & alphabetical order
        TravellingTime tt = new TravellingTime();
        tt.sort(r1);
        tt.sort(r2);
        tt.sort(r3);
        tt.sort(r4);
        tt.sort(r5);

        // Add all separate lists into the displayed restaurant list
        restaurantList.clear();
        restaurantList.addAll(r5);
        restaurantList.addAll(r4);
        restaurantList.addAll(r3);
        restaurantList.addAll(r2);
        restaurantList.addAll(r1);
    }
}
