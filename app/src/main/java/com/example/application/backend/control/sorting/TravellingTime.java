package com.example.application.backend.control.sorting;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

/**
 * TravellingTime is a class that facilitates the sorting of restaurants.
 * This class overrides the sort method in the parent SortingCriteria class
 * to sort restaurants based on their travelling time from the user's inputted location.
 * @author celest
 * @version 1.0
 * @since 2022-10-07
 */
public class TravellingTime extends SortingCriteria {

    /**
     * Class constructor.
     * Creates the sorting criteria travelling time.
     */
    public TravellingTime() {};

    /**
     * Arranges the restaurants from shortest to longest travelling time based on the user's chosen mode of transport.
     * If the travelling time to restaurants are equal, the restaurants are sorted alphabetically in ascending order.
     * @param restaurantList	The list of filtered restaurants
     */
    public void sort(ArrayList<Restaurant> restaurantList) {

        int sizeOfRestaurantList = restaurantList.size();

        Restaurant temp = new Restaurant();

        //debug
        for (int i=0; i<restaurantList.size();i++){
            System.out.println(restaurantList.get(i).getAddress());
        }
        System.out.println();

        // Bubble sort is used.
        for(int i = 0; i < sizeOfRestaurantList; i++) {

            for(int j = 1; j <= (sizeOfRestaurantList-1); j++) {
                System.out.println("Res1: "+restaurantList.get(j-1).getTravellingTime()+"   "+"Res2: "+restaurantList.get(j).getTravellingTime());
                if(restaurantList.get(j-1).getTravellingTime() > restaurantList.get(j).getTravellingTime())
                {
                    temp = restaurantList.get(j-1);
                    restaurantList.set(j-1, restaurantList.get(j));
                    restaurantList.set(j, temp);
                    System.out.println("Swapped");
                }

                // This statement checks if the crowd levels of both restaurants are equal
                else if(restaurantList.get(j-1).getTravellingTime() == restaurantList.get(j).getTravellingTime())
                {
                    String firstName = restaurantList.get(j-1).getName().toUpperCase();
                    String secondName = restaurantList.get(j).getName().toUpperCase();

                    // This if statement compares the alphabets of both restaurants individually.
                    // The statement is true when the first restaurant is greater than the second restaurant alphabetically.
                    if(firstName.compareTo(secondName) > 0)
                    {
                        temp = restaurantList.get(j-1);
                        restaurantList.set(j-1, restaurantList.get(j));
                        restaurantList.set(j, temp);
                    }
                }
            }
        }
        //debug
        for (int i=0; i<restaurantList.size();i++){
            System.out.println(restaurantList.get(i).getAddress());
        }
        System.out.println();
    }
}
