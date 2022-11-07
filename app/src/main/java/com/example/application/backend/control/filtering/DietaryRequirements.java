package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;
import com.example.application.backend.enums.TypesOfDietaryRequirements;

import java.util.ArrayList;
import java.util.Locale;

public class DietaryRequirements extends FilteringCriteria{

    @Override
    public ArrayList<Restaurant> filter(ArrayList<Restaurant>restaurantList){
        ArrayList<Restaurant>filteredList = new ArrayList<Restaurant>();
        TypesOfDietaryRequirements reqs = TypesOfDietaryRequirements.valueOf(((String)super.criteria).toUpperCase(Locale.ROOT));
        for(int i=0; i<restaurantList.size(); i++) {
            if(reqs.toString().equals(restaurantList.get(i).getAvailableDietaryRequirements())) {
                filteredList.add(restaurantList.get(i));
            }
        }
        return filteredList;
    }
}
