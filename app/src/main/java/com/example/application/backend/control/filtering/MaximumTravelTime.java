package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

/**
 * MaximumTravelTime is our filtering child class that filters the full restaurant list according to the given maximum travel time sub criteria set by the user
 * This class inherits from FilteringCriteria and implements its filter() function.
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public class MaximumTravelTime extends FilteringCriteria{
    /**
     * This is the implemented filter method inherited from Filtering Criteria abstract class
     * @param restaurantList refers to the full restaurant list to be filtered
     * @return returns the filtered restaurant list
     */
    @Override
    public ArrayList<Restaurant> filter(ArrayList<Restaurant>restaurantList) {
        float maxTravelTime = (float) 0.0;
        ArrayList <Restaurant> filteredList = new ArrayList<Restaurant>();
        String travelTimeString = (String) super.criteria;
        if (travelTimeString.compareTo("5 minutes")==0) maxTravelTime=5;
        else if (travelTimeString.compareTo("10 minutes")==0) maxTravelTime=10;
        else if (travelTimeString.compareTo("20 minutes")==0) maxTravelTime=20;
        else if (travelTimeString.compareTo("40 minutes")==0) maxTravelTime=40;

        for (int i=0; i<restaurantList.size(); i++) {
            if (restaurantList.get(i).getTravellingTime()<=maxTravelTime && restaurantList.get(i).getTravellingTime()>=0) {
                filteredList.add(restaurantList.get(i));
            }
        }
        return filteredList;
    }
}

//possible to optimise this by reading all entries from the text file, converting string attribute to float and comparing to set maxTravelTime

