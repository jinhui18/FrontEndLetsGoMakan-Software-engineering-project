package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

public abstract class FilteringCriteria {
    public abstract ArrayList<Restaurant> filter(ArrayList<Restaurant> restaurantList);
    public abstract void addCriteria(Object object);
}

/**
 * How to use this:
 * instantiate the filtering criteria object with static method of FilteringStoreFactory
 * use the addCriteria() method to add user's set preferences
 * lastly, call handleEvent()
 */