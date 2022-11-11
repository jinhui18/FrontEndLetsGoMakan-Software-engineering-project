package com.example.application.backend.control.sorting;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

/**
 * PriceLevel is a sorting criteria sub class that inherits from the abstract parent class SortingCriteria
 * It implements and overrides its sort(ArrayList<Restaurant>) method to sort the provided restaurant list according to the price level of the restaurant
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public class PriceLevel extends SortingCriteria {
    /**
     * Class constructor.
     * Creates the sorting criteria crowd level.
     */
    public PriceLevel(){}

    /**
     * This is the overrided implemented method from the abstract parent class with the logic to sort the restaurant list by price level
     * The restaurants are sorted in ascending price levels with restaurants with the lowest price levels appearing at the front of the list (vice versa)
     * @param restaurantList refers to the restaurant list to be sorted by price level
     */
    @Override
    public void sort(ArrayList<Restaurant> restaurantList) {
        ArrayList<Restaurant> r0 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r1 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r2 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r3 = new ArrayList<Restaurant>();
        ArrayList<Restaurant> r4 = new ArrayList<Restaurant>();


        // add same rating restaurant into separate arrays
        for (int i=0; i<restaurantList.size(); i++) {
            Restaurant restaurant = restaurantList.get(i);
            int priceLevel =restaurant.getPriceLevel();
            if (priceLevel==0) r0.add(restaurant);
            else if (priceLevel==1) r1.add(restaurant);
            else if (priceLevel==2) r2.add(restaurant);
            else if (priceLevel==3) r3.add(restaurant);
            else if (priceLevel==4) r4.add(restaurant);
        }

        // sort each array by travel time & alphabetical order
        TravellingTime tt = new TravellingTime();
        tt.sort(r0);
        tt.sort(r1);
        tt.sort(r2);
        tt.sort(r3);
        tt.sort(r4);

        // Add all separate lists into the displayed restaurant list
        restaurantList.clear();
        restaurantList.addAll(r0);
        restaurantList.addAll(r1);
        restaurantList.addAll(r2);
        restaurantList.addAll(r3);
        restaurantList.addAll(r4);

    }
}
