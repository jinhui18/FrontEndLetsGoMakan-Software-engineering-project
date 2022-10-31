package com.example.application.backend.control.sorting;

import com.example.application.backend.entity.Restaurant;
import java.util.ArrayList;

public class BookmarkedRestaurants extends SortingCriteria{
    public void sort(ArrayList<Restaurant> restaurantList) {
        ArrayList<Restaurant> bookmarkedList = new ArrayList<Restaurant>();
        for (int i=0; i<restaurantList.size(); i++) {
            Restaurant temp = restaurantList.get(i);
            if (temp.isBookmark()) bookmarkedList.add(temp);
        }
        restaurantList.clear();
        restaurantList.addAll(bookmarkedList);
    }
}
