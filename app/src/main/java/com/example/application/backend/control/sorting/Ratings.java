package com.example.application.backend.control.sorting;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;
/**
 * Ratings is a sorting criteria sub class that inherits from the abstract parent class SortingCriteria
 * It implements and overrides its sort method to sort the provided restaurant list according to the ratings of the restaurant
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public class Ratings extends SortingCriteria {
    /**
     * Class constructor.
     * Creates the sorting criteria crowd level.
     */
    public Ratings() {}

    /**
     * This is the overrided implemented method from the abstract parent class with the logic to sort the restaurant list by ratings
     * The restaurants are sorted in ascending price levels with restaurants with the highest ratings appearing at the front of the list (vice versa)
     * @param restaurantList refers to the restaurant list to be sorted by ratings
     */
    public void sort(ArrayList<Restaurant> restaurantList) {
        ArrayList<Restaurant> r0 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r1 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r2 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r3 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r4 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r5 = new ArrayList<Restaurant>();

        //debug
        for (int i=0; i<restaurantList.size();i++){
            System.out.println(restaurantList.get(i).getAddress());
        }
        System.out.println();

        // add same rating restaurant into separate arrays
        for (int i=0; i<restaurantList.size(); i++) {
            Restaurant restaurant = restaurantList.get(i);
            int rating = (int) restaurant.getRatings();
            if (rating ==0) r0.add(restaurant);
            else if (rating==1) r1.add(restaurant);
            else if (rating==2) r2.add(restaurant);
            else if (rating==3) r3.add(restaurant);
            else if (rating==4) r4.add(restaurant);
            else r5.add(restaurant);
        }

        // sort each array by travel time & alphabetical order
        TravellingTime tt = new TravellingTime();
        tt.sort(r0);
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
        restaurantList.addAll(r0);

        //debug
        for (int i=0; i<restaurantList.size();i++){
            System.out.println(restaurantList.get(i).getAddress());
        }
        System.out.println();
    }
}
