package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

/**
 * OpenClose is our filtering child class that filters the full restaurant list according to operational status of the restaurant (open or close)
 * This class inherits from FilteringCriteria and implements its filter(ArrayList<Restaurant>) function.
 * @author Isaac
 * @version 1.0
 * @since 2022-11-12
 */
public class OpenClose extends FilteringCriteria{
    /**
     * This is the implemented filter method inherited from Filtering Criteria abstract class
     * @param restaurantList refers to the full restaurant list to be filtered
     * @return returns the filtered restaurant list
     */
    @Override
    public ArrayList<Restaurant> filter(ArrayList<Restaurant> restaurantList) {
        ArrayList<Restaurant> filteredList = new ArrayList<>();
        for (int i=0; i<restaurantList.size(); i++){
            Restaurant restaurant = restaurantList.get(i);
            if (restaurant.isOpenNow()){
                filteredList.add(restaurant);
            }
        }
        return filteredList;
    }
}
