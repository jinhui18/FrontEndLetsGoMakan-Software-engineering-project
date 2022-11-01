package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

public abstract class FilteringCriteria {
    protected Object criteria;
    public void addCriteria(Object object) {this.criteria=object;}
    public abstract ArrayList<Restaurant> filter(ArrayList<Restaurant> restaurantList);
}

/**
 * How to use this:
 * instantiate the filtering criteria object with static method of FilteringStoreFactory (dynamic loading)
 * use the addCriteria() method to add user's set preferences as criteria
 * lastly, call handleEvent()
 */