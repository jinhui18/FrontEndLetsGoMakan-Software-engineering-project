package com.example.application.backend.sorting;

import com.example.application.backend.entity.Restaurant;
import java.util.ArrayList;

public abstract class SortingCriteria {
    public abstract void sort(ArrayList<Restaurant> restaurantList);
}

