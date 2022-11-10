package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;


public class PriceLevel extends FilteringCriteria{
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