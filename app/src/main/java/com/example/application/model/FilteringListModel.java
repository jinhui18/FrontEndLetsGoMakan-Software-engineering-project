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

/**
 * FilteringListModel is the model child class that inherits from the abstract Model parent class
 * It inherits and implements the service() method with the logic to filter the retrieved full restaurant list, and pass the filtered list to the sorting controller to handle the sorting event and storing of the final list in the database.
 * To perform its service logic, FilteringListModel needs an attribute list containing a sortingList, sortingListModel reference, an arraylist of filtering criteria, and the full restaurant list
 * The sortingList is the attribute list needed by the sortingListModel to perform its service logic of sorting the recommended list
 * The sortingList will contain the sorting criteria as well has an empty slot to hold the filtered restaurant list.
 * @author Isaac
 * @version 1.0
 * @since 2022-11-11
 */
public class FilteringListModel extends Model{

    /**
     * This is the overridden constructor for FilteringListModel
     * @param mAuth refers to our firebase authenticator reference
     * @param mDatabase refers to our firebase realtime database reference
     * @param context refers to the UI activity page (View class) in which the event happened
     */
    public FilteringListModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    /**
     * This is the inherited and implemented service() model containing the logic to filter the full restaurant list and trigger the sorting of the filtered list
     * The process involves first retrieving the full restaurant list from the database,
     * filtering the full restaurant list with the filtering criteria, which contains the filtering sub criteria as its attribute (see filtering criteria class) that passed in from the attribute list
     * Instantiates a sortingListController controller object with the sortingListModel and sortingList to handle the event of sorting the filtered list with the sorting criteria passed in via the attribute list
     * sortingListController object invokes handleEvent() to sort the list
     */
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

