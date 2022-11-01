package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

public class MaximumTravelTime extends FilteringCriteria{
    private float maxTravelTime;
    public MaximumTravelTime() {}

    public MaximumTravelTime(float maxTravelTime) {
        this.maxTravelTime = maxTravelTime;
    }
    /**
     * This method returns an ArrayList of all those restaurants whose traveling times are less than
     * (or equal to) the maximum traveling time.
     */
    @Override
    public ArrayList<Restaurant> filter(ArrayList<Restaurant>restaurantList) {
        ArrayList <Restaurant> filteredList = new ArrayList<Restaurant>();
        for (int i=0; i<restaurantList.size(); i++) {
            if (restaurantList.get(i).getTravellingTime()<=this.maxTravelTime) {
                filteredList.add(restaurantList.get(i));
            }
        }
        return filteredList;
    }

    @Override
    public void addCriteria(Object object) {
        this.maxTravelTime = (float) maxTravelTime;
    }
}

