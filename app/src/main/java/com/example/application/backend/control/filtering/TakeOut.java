package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

/**
 * TakeOut is our filtering child class that filters the full restaurant list according to whether the restaurants in the list have take out available as an option or not
 * This class inherits from FilteringCriteria and implements its filter() function.
 */
public class TakeOut extends FilteringCriteria {
    /**
     * This is the implemented filter method inherited from Filtering Criteria abstract class
     * @param restaurantList refers to the full restaurant list to be filtered
     * @return returns the filtered restaurant list
     */
    @Override
    public ArrayList<Restaurant> filter(ArrayList<Restaurant> restaurantList) {
        ArrayList<Restaurant> filteredList = new ArrayList<>();
        System.out.println("Takeout filter is called");
        for (int i=0; i<restaurantList.size(); i++){
            Restaurant restaurant = restaurantList.get(i);
            if (restaurant.isTakeOut()) {filteredList.add(restaurant);}
        }
        return filteredList;
    }
}
