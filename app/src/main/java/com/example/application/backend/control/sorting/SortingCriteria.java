package com.example.application.backend.control.sorting;

import com.example.application.backend.entity.Restaurant;
import java.util.ArrayList;

public abstract class SortingCriteria {
    public abstract void sort(ArrayList<Restaurant> restaurantList);
    public void testing(){
        System.out.println("object instantiated dynamically");
    }
}

