package com.example.application.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.control.filtering.FilteringCriteria;
import com.example.application.backend.control.others.FirebaseRetrieval;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Restaurant;
import com.example.application.controller.Controller;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilteringListModel extends Model{

    public FilteringListModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    @Override
    public void service() {
        //Format: [sortingList, sortingListModel, ArrayList<FC> FCList, FullRestList]
        ArrayList<Restaurant> fullRestaurantList = (ArrayList<Restaurant>) super.attributeList.get(3);
        ArrayList<FilteringCriteria> filteringCriteriaList = (ArrayList<FilteringCriteria>) super.attributeList.get(2);

        for (int i=0; i<filteringCriteriaList.size(); i++){
            FilteringCriteria filteringCriteria = filteringCriteriaList.get(i);
            fullRestaurantList = filteringCriteria.filter(fullRestaurantList);
            System.out.println("\nSize of restaurant list after filtering: "+ fullRestaurantList.size());
        }

        ArrayList<Object> sortingList = (ArrayList<Object>) attributeList.get(0);
        //add restaurant list to sortingList
        sortingList.add(fullRestaurantList);
        Controller sortingListController = new Controller((Model) attributeList.get(1), sortingList);
        sortingListController.handleEvent();

        return;
    }
}

/*
        //Retrieve account object and sort recommended list
        FilteringCriteria filteringCriteria = (FilteringCriteria) super.attributeList.get(0);
        filteringCriteria.addCriteria(super.attributeList.get(1));
        ArrayList<Restaurant> fullRestaurantList = (ArrayList<Restaurant>) super.attributeList.get(2);
        System.out.println("\nRecommendedList size before: "+ fullRestaurantList.size());
        ArrayList<Restaurant> recommendedList = filteringCriteria.filter(fullRestaurantList);
        System.out.println("\nRecommendedList size before: "+ recommendedList.size());
 */
