package com.example.application.backend.control.others;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.DisplayRestaurantsList;
import com.example.application.backend.control.filtering.FilteringCriteria;
import com.example.application.backend.control.filtering.FilteringStoreFactory;
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
    public static void pureSorting(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context, ArrayList<Object> attributeList, Model sortingListModel){
        final ArrayList<Account> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

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
                            if (arrayList.get(0).getRecommendedList()!=null) recommendedList=arrayList.get(0).getRecommendedList();
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
            ArrayList<Object> filteringList,
            Model filteringListModel
    ) {
        final ArrayList<Account> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

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
            ArrayList<Object> filteringList,
            int[] profileSubCriteriaChoice,
            String[] selectedSubCriteria,
            ArrayList<Object> subCriteria2D,
            Model filteringListModel
    ) {
        final ArrayList<Account> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

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
                        TypesOfDietaryRequirements defaultDietaryRequirement = userProfile.getDietaryRequirements();
                        float defaultMaxTravelTime = userProfile.getPreferredMaximumTravelTime();

                        //There are only x many default filtering criteria
                        for (int i=0; i<2; i++){
                            Map<String,String> hashy = (Map<String, String>) subCriteria2D.get(i);
                            for (int j=0; j<hashy.size();j++){
                                if (i==0){
                                    String subCriteria = hashy.get(String.valueOf(j));
                                    if (defaultDietaryRequirement==TypesOfDietaryRequirements.valueOf(subCriteria.toUpperCase(Locale.ROOT))){
                                        profileSubCriteriaChoice[i] = j;
                                        selectedSubCriteria[i] = subCriteria;
                                        break;
                                    }
                                }
                                else if (i==1){
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
                        for (int k=0; k<2; k++){
                            System.out.println("CHOSEN CRITERIA: "+selectedSubCriteria[k]);
                        }
                        ArrayList<FilteringCriteria> filteringCriteriaList = (ArrayList<FilteringCriteria>) filteringList.get(2); //Store all needed filtering criteria object
                        System.out.println("FilteringCriteriaList length: "+filteringCriteriaList.size());
                        for (int k=0; k<2; k++) {
                            System.out.println("K value: "+k);
                            FilteringCriteria filteringCriteria = filteringCriteriaList.get(k);
                            filteringCriteria.addCriteria(selectedSubCriteria[k]); //add Corresponding criteria if user profile has that filtering option
                            filteringCriteriaList.add(filteringCriteria);
                        }//end for

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
