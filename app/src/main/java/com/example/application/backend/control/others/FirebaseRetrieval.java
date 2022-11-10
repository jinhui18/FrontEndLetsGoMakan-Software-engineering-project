package com.example.application.backend.control.others;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.DisplayRestaurantsList;
import com.example.application.backend.control.filtering.FilteringCriteria;
import com.example.application.backend.control.filtering.FilteringStoreFactory;
import com.example.application.backend.control.sorting.SortingCriteria;
import com.example.application.backend.control.sorting.SortingStoreFactory;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Profile;
import com.example.application.backend.entity.Restaurant;
import com.example.application.backend.enums.TypesOfDietaryRequirements;
import com.example.application.controller.Controller;
import com.example.application.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class FirebaseRetrieval {
    public static void pureSorting(
            FirebaseAuth mAuth,
            DatabaseReference mDatabase,
            Context context,
            String[] sortingCriteriaArray,
            int singlePosition,
            Model sortingListModel
    ) {
        final ArrayList<Account> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        SortingCriteria sortingCriteria = SortingStoreFactory.getDatastore(sortingCriteriaArray[singlePosition]);
        ArrayList<Object> attributeList = new ArrayList<Object>();
        attributeList.add(sortingCriteria);

        mDatabase.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Iterable<DataSnapshot> children = snapshot.getChildren();

                            for (DataSnapshot child : children) {
                                Account account = child.getValue(Account.class);
                                arrayList.add(account);
                            }
                            System.out.println("Size of arrayList 999 :" + arrayList.size());
                            ArrayList<Restaurant> recommendedList = new ArrayList<>();
                            if (arrayList.get(0).getRecommendedList()!=null) {
                                recommendedList=arrayList.get(0).getRecommendedList();
                            }
                            attributeList.add(recommendedList);
                            System.out.println("RECOMMENDED LIST RETRIEVED AND APPENDED");
                            Controller initialSortingController = new Controller(sortingListModel, attributeList);
                            initialSortingController.handleEvent(); //model -> update -> retrieveAndDisplay() called

                        return;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
        System.out.println("By-pass 1 --->>>"+ arrayList.size());
        return;
    }

    public static void filterAndSort(
            FirebaseAuth mAuth,
            DatabaseReference mDatabase,
            Context context,
            String[] filteringCriteriaArray,
            String[] sortingCriteriaArray,
            String[] selectedSubCriteria,
            boolean[] selectedFilteringCriteria,
            int singlePosition,
            Model filteringListModel,
            Model sortingListModel
    ) {
        final ArrayList<Account> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();



        //get the sortingCriteria here using the singlePosition (keeps track of last-used sortingCriteria
        SortingCriteria sortingCriteria = SortingStoreFactory.getDatastore(sortingCriteriaArray[singlePosition]);
        ArrayList<Object> sortingList = new ArrayList<Object>();
        sortingList.add(sortingCriteria);

        //From selectedFilteringCriteria array instantiate required filteringCriteria objects
        ArrayList<Object> filteringList = new ArrayList<Object>();
        ArrayList<FilteringCriteria> filteringCriteriaList = new ArrayList<>(); //Store all needed filtering criteria object


        mDatabase.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Iterable<DataSnapshot> children = snapshot.getChildren();

                            for (DataSnapshot child : children) {
                                Account account = child.getValue(Account.class);
                                arrayList.add(account);
                            }
                            System.out.println("Size of arrayList 000 :" + arrayList.size());
                            Account userAccount = arrayList.get(0);


                            for (int k=0; k<filteringCriteriaArray.length; k++) {
                                System.out.println("boolean array: " + selectedFilteringCriteria[k]);
                                if (selectedFilteringCriteria[k] == true){
                                    FilteringCriteria filteringCriteria = FilteringStoreFactory.getDatastore(filteringCriteriaArray[k]);
                                    filteringCriteria.addCriteria(selectedSubCriteria[k]); //add Corresponding criteria if user selected that filtering option
                                    filteringCriteriaList.add(filteringCriteria);
                                }//end if
                            }//end for
                            //Pass in everything to method
                            filteringList.add(sortingList); //needed to instantiate sorting controller in filteringModel class
                            filteringList.add(sortingListModel);
                            filteringList.add(filteringCriteriaList); //Format: [sortingList, sortingListModel, ArrayList<FC> FCList, FullRestList]
                            filteringList.add(userAccount.getFullRestaurantList());
                            System.out.println("FULL RESTAURANT LIST RETRIEVED AND APPENDED"); //Format: [sortingList, sortingListModel, ArrayList<FC> FCList, FullRestList]

                            Controller filteringController = new Controller(filteringListModel, filteringList);
                            filteringController.handleEvent();


                        return;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
        System.out.println("By-pass1B");
        return;
    }

    public static void defaultFilterAndSort(
            FirebaseAuth mAuth,
            DatabaseReference mDatabase,
            Context context,
            String[] filteringCriteriaArray,
            String[] sortingCriteriaArray,
            boolean[] selectedFilteringCriteria,
            int singlePosition,
            int[] profileSubCriteriaChoice,
            String[] selectedSubCriteria,
            int numberOfDefaultCriteria,
            ArrayList<Object> subCriteria2D,
            Model filteringListModel,
            Model sortingListModel
    ) {
        final ArrayList<Account> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

        SortingCriteria sortingCriteria = SortingStoreFactory.getDatastore(sortingCriteriaArray[singlePosition]);
        ArrayList<Object> sortingList = new ArrayList<Object>();
        sortingList.add(sortingCriteria);

        ArrayList<Object> filteringList = new ArrayList<Object>();
        ArrayList<FilteringCriteria> filteringCriteriaList = new ArrayList<>(); //Store all needed filtering criteria object


        mDatabase.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Iterable<DataSnapshot> children = snapshot.getChildren();

                        for (DataSnapshot child : children) {
                            Account account = child.getValue(Account.class);
                            arrayList.add(account);
                        }
                        System.out.println("Size of arrayList 000 :" + arrayList.size());
                        Account userAccount = arrayList.get(0);
                        Profile userProfile = userAccount.getProfile();
                        //TypesOfDietaryRequirements defaultDietaryRequirement = userProfile.getDietaryRequirements();
                        float defaultMaxTravelTime = userProfile.getPreferredMaximumTravelTime();

                        //There are only x many default filtering criteria
                        for (int i=0; i<numberOfDefaultCriteria; i++){
                            Map<String,String> hashy = (Map<String, String>) subCriteria2D.get(i);
                            for (int j=0; j<hashy.size();j++){
                                //For future extensions, add on more else if statements here
                                if (i==0){
                                    String subCriteria = hashy.get(String.valueOf(j));
                                    String[] parts = subCriteria.split(" ");
                                    float maxTimeLimit = Float.parseFloat(parts[0]);
                                    if (defaultMaxTravelTime<=maxTimeLimit){ //maxTimeLimit will be in ascending order as per txt file
                                        profileSubCriteriaChoice[i] = j;
                                        selectedSubCriteria[i] = subCriteria;
                                        break;
                                    }
                                }
                            }//inner for
                        }//outer for
                        //Format: [sortingList, sortingListModel, ArrayList<FC> FCList, FullRestList]
                        for (int k=0; k<numberOfDefaultCriteria; k++){
                            System.out.println("CHOSEN CRITERIA: "+selectedSubCriteria[k]);
                        }

                        System.out.println("FilteringCriteriaList length: "+filteringCriteriaList.size());
                        for (int k=0; k<numberOfDefaultCriteria; k++) {
                            System.out.println("K value: "+k);
                            selectedFilteringCriteria[k]=true; //This is to check the box in the UI dropdown
                            FilteringCriteria filteringCriteria = FilteringStoreFactory.getDatastore(filteringCriteriaArray[k]);
                            filteringCriteria.addCriteria(selectedSubCriteria[k]); //add Corresponding criteria if user profile has that filtering option
                            filteringCriteriaList.add(filteringCriteria);
                        }//end for
                        //Pass in everything to method
                        filteringList.add(sortingList); //needed to instantiate sorting controller in filteringModel class
                        filteringList.add(sortingListModel);
                        filteringList.add(filteringCriteriaList); //Format: [sortingList, sortingListModel, ArrayList<FC> FCList, FullRestList]
                        filteringList.add(userAccount.getFullRestaurantList());
                        System.out.println("FULL RESTAURANT LIST RETRIEVED AND APPENDED"); //Format: [sortingList, sortingListModel, ArrayList<FC> FCList, FullRestList]

                        Controller filteringController = new Controller(filteringListModel, filteringList);
                        filteringController.handleEvent();

                        return;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
        System.out.println("By-pass1C");
        return;
    }


}
