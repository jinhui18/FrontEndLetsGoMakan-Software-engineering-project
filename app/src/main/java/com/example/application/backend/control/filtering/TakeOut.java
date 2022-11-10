package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

public class TakeOut extends FilteringCriteria {
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
