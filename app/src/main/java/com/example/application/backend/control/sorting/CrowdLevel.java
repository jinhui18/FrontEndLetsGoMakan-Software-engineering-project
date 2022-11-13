package com.example.application.backend.control.sorting;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

/**
 * CrowdLevel is a class that facilitates the sorting of restaurants.
 * This class overrides the sort method in the parent SortingCriterias class
 * to sort restaurants based on their real-time crowd level.
 * @author celest
 * @version 1.0
 * @since 2022-10-07
 */
public class CrowdLevel extends SortingCriteria {

    /**
     * Class constructor.
     * Creates the sorting criteria crowd level.
     */
    public CrowdLevel() {};

    /**
     * Arranges the restaurants from least to most crowded.
     * If the crowd levels of restaurants are equal, the restaurants are sorted
     * by their travelling times in ascending order.
     * In the worst case, if the crowd level and travelling times of restaurants are the same,
     * they will be sorted alphabetically in ascending order
     * @param restaurantList	The list of filtered restaurants.
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
            for(int j = 1; j <= (sizeOfRestaurantList - 1); j++) {
                if(restaurantList.get(j-1).getCrowdLevel() > restaurantList.get(j).getCrowdLevel())
                {

                    temp = restaurantList.get(j-1);
                    restaurantList.set(j-1, restaurantList.get(j));
                    restaurantList.set(j, temp);
                }

                // This statement checks if the crowd levels of both restaurants are equal
                else if(restaurantList.get(j-1).getCrowdLevel() == restaurantList.get(j).getCrowdLevel())
                {
                    // creating a temporary list with both of the restaurants that we are comparing.
                    ArrayList<Restaurant> tempList = new ArrayList<Restaurant>();
                    tempList.add(0, restaurantList.get(j-1));
                    tempList.add(1, restaurantList.get(j));

                    // we will use the sort method in the TravellingTime class as we need to sort by travelling time
                    // and then alphabetically if the travelling times are the same.
                    TravellingTime tempSort = new TravellingTime();
                    tempSort.sort(tempList);

                    // we copy over the sorted list to our current restaurant list.
                    restaurantList.set(j-1, tempList.get(0));
                    restaurantList.set(j, tempList.get(1));
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
