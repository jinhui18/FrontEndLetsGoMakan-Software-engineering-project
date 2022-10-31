package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;

import java.util.ArrayList;

public abstract class FilteringCriteria {
    public abstract ArrayList<Restaurant> filter(ArrayList<Restaurant> restaurantList);
}

