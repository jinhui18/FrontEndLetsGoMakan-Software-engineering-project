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
        float maxTravelTime = (float) 0.0;
        ArrayList <Restaurant> filteredList = new ArrayList<Restaurant>();
        String travelTimeString = (String) super.criteria;
        if (travelTimeString.compareTo("5 minutes")==0) maxTravelTime=5;
        else if (travelTimeString.compareTo("10 minutes")==0) maxTravelTime=10;
        else if (travelTimeString.compareTo("15 minutes")==0) maxTravelTime=15;
        else if (travelTimeString.compareTo("20 minutes")==0) maxTravelTime=20;

        for (int i=0; i<restaurantList.size(); i++) {
            if (restaurantList.get(i).getTravellingTime()<=maxTravelTime && restaurantList.get(i).getTravellingTime()>=0) {
                filteredList.add(restaurantList.get(i));
            }
        }
        return filteredList;
    }
}

//possible to optimise this by reading all entries from the text file, converting string attribute to float and comparing to set maxTravelTime

