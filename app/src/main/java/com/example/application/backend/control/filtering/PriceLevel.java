package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

/**
 * PriceLevel is our filtering child class that filters the full restaurant list according to the given price level sub criteria set by the user
 * This class inherits from FilteringCriteria and implements its filter function.
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public class PriceLevel extends FilteringCriteria{
    /**
     * This is the implemented filter method inherited from Filtering Criteria abstract class
     * @param restaurantList refers to the full restaurant list to be filtered
     * @return returns the filtered restaurant list
     */
    @Override
    public ArrayList<Restaurant> filter(ArrayList<Restaurant> restaurantList) {
        ArrayList<Restaurant> filteredList = new ArrayList<>();
        String option = (String) super.criteria;
        int priceLevelNum=0;

        if (option.compareTo("Inexpensive $")==0) {
            for (int i=0; i<restaurantList.size(); i++){
                Restaurant restaurant = restaurantList.get(i);
                if (restaurant.getPriceLevel()==0 || restaurant.getPriceLevel()==1){
                    filteredList.add(restaurant);
                }
            }
            return filteredList;
        }
        else if (option.compareTo("Moderate $$")==0) priceLevelNum=2;
        else if (option.compareTo("Expensive $$$")==0) priceLevelNum=3;
        else priceLevelNum=4;

        for (int i=0; i<restaurantList.size(); i++){
            Restaurant restaurant = restaurantList.get(i);
            if (restaurant.getPriceLevel()==priceLevelNum){
                filteredList.add(restaurant);
            }
        }

        return filteredList;
    }
}