package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

/**
 * This is the abstract class that all our filtering sub classes inherit from
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public abstract class FilteringCriteria {
    /**
     * criteria refers to the filtering sub criteria the filtering class objects need to sort the restaurant list by
     */
    protected Object criteria;

    /**
     * This method adds the filtering sub criteria to the filtering object responsible for filtering the given restaurant list
     * @param object refers to the criteria
     */
    public void addCriteria(Object object) {this.criteria=object;}

    /**
     * This method filters the full restaurant list and returns the filtered list
     * @param restaurantList refers to the full restaurant list to be sorted
     * @return returns a filtering restaurant list according to the provided filtering sub criteria
     */
    public abstract ArrayList<Restaurant> filter(ArrayList<Restaurant> restaurantList);
}
