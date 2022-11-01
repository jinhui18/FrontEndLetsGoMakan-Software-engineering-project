package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

public class MaximumTravelTime extends FilteringCriteria{
    /**
     * This method returns an ArrayList of all those restaurants whose traveling times are less than
     * (or equal to) the maximum traveling time.
     */
    @Override
    public ArrayList<Restaurant> filter(ArrayList<Restaurant>restaurantList) {
        ArrayList <Restaurant> filteredList = new ArrayList<Restaurant>();
        float maxTravelTime = (float) super.criteria;
        for (int i=0; i<restaurantList.size(); i++) {
            if (restaurantList.get(i).getTravellingTime()<=maxTravelTime) {
                filteredList.add(restaurantList.get(i));
            }
        }
        return filteredList;
    }
}

