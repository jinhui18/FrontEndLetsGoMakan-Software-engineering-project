package com.example.application.backend.control.filtering;

import com.example.application.backend.entity.Restaurant;
import com.example.application.backend.enums.TypesOfDietaryRequirements;

import java.util.ArrayList;

public class DietaryRequirements extends FilteringCriteria{

    private String reqs;
    public DietaryRequirements() {};

    /**
     * Overloaded constructor.
     * DietaryEnum is an enum of dietary requirements.
     * @param reqs
     */

    public DietaryRequirements(TypesOfDietaryRequirements reqs) {
        this.reqs = reqs.toString();
    }
    @Override
    public ArrayList<Restaurant> filter(ArrayList<Restaurant>restaurantList){
        ArrayList<Restaurant>filteredList = new ArrayList<Restaurant>();
        for(int i=0; i<restaurantList.size(); i++) {
            if(reqs.equals(restaurantList.get(i).getAvailableDietaryRequirements())) {
                filteredList.add(restaurantList.get(i));
            }
        }
        return filteredList;
    }
}
