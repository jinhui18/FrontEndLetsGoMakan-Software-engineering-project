package com.example.application.backend.control.sorting;

import com.example.application.backend.entity.Restaurant;
import java.util.ArrayList;
/**
 * This is the abstract class that all our sorting sub classes inherit from
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public abstract class SortingCriteria {
    /**
     * This method sorts the recommended restaurant list according to a sorting criteria
     * @param restaurantList refers to the recommended restaurant list to be sorted
     */
    public abstract void sort(ArrayList<Restaurant> restaurantList);
}

