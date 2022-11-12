package com.example.application.backend.control.others;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.control.filtering.FilteringCriteria;
import com.example.application.backend.control.filtering.FilteringStoreFactory;
import com.example.application.backend.control.sorting.SortingCriteria;
import com.example.application.backend.control.sorting.SortingStoreFactory;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Profile;
import com.example.application.backend.entity.Restaurant;
import com.example.application.controller.Controller;
import com.example.application.model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * FirebaseRetrieval is our facade class that aggregates all sorting and filtering methods relating to different classes into three methods
 * These methods are pureSort() for sorting only, filterAndSort() for filtering then sorting, and defaultFilterAndSort() for the initial filter and sorting of the restaurant list by default criteria
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public class FirebaseRetrieval {
    /**
     * This static method is called whenever a user selects a sorting crietria and confirms the selection.
     * The method will retrieve the recommended restaurant list from Firebase,
     * sort the list according to the selected sorting criteria via the MVC architecture path,
     * push the recommended restaurant list back to Firebase,
     * notifying our UI elements on the display restaurant list activity page,
     * update UI elements on the activity page via the observer pattern
     * @param mAuth refers to the Firebase authentication reference
     * @param mDatabase refers to the reference to our Firebase realtime database
     * @param context refers to the current activity of the application when the function is called
     * @param sortingCriteriaArray refers to the array containing the String selections of all our different sorting criteria
     * @param singlePosition refers to the position of the user's selected sorting crietria in sortingCriteriaArray
     * @param sortingListModel refers to the model object in MVC architecture that will invoke its service() method to sort the restaurant list according to the chosen sorting criteria
     */
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

    /**
     * This static method is called whenever a user selects one or more filtering criteria and confirms the selection
     * The process involves retreiving the full restaurant list from the database,
     * filtering the full restaurant list according to the user's selected filtering criteria and their associated fitlering sub criteria followed by
     * sorting the filtered list according to the user's last selected sorting criteria by MVC architecture pathway,
     * pushing the filtered and sorted restarant list back to the database (as the recommended restaurant list),
     * notifying UI elements on the display restaurant list page via the observer pattern,
     * which leads to the updating of the UI elements.
     * @param mAuth refers to the Firebase authetication reference
     * @param mDatabase refers to our Firebase realtime database reference
     * @param context refers to the current activity of the application when the function is called
     * @param filteringCriteriaArray refers to the array containing the String selections of all our different filtering criteria
     * @param sortingCriteriaArray refers to the array containing the String selections of all our different sorting criteria
     * @param selectedSubCriteria refers to the array containing the selected sub criteria for the type of filtering represented by each index in the array
     * @param selectedFilteringCriteria refers to the array containing the String selections of all our different filtering criteria
     * @param singlePosition refers to the position of the user's selected sorting crietria in sortingCriteriaArray
     * @param filteringListModel refers to the model object in MVC architecture that will invoke its service() method to sort the restaurant list according to the chosen filtering criteria
     * @param sortingListModel refers to the model object in MVC architecture that will invoke its service() method to sort the restaurant list according to the chosen sorting criteria
     */
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

    /**
     * This static method is called upon the creation of Display Restaurnt List UI activity to filter and sort the full restaurant list according to default criteria
     * The process involves retrieving the full restaurant list from the database,
     * filtering the full restaurant list according to the default filtering criteria (which is maximum travel time the user is comfortable with) with the user's preference in his profile (sub criteria for this filtering criteria),
     * and sorting the filtered list according to travel time by MVC architecture pathway.
     * Then the filtered and sorted restaurant list will be pushed back to the database (as the recommended list),
     * followed by notification of all UI elements in Display Restaurant List UI activity where their update() method will be invoked to reflect the new restaurant list at runtime.
     * @param mAuth refers to the Firebase authetication reference
     * @param mDatabase refers to our Firebase realtime database reference
     * @param context context refers to the current activity of the application when the function is called
     * @param filteringCriteriaArray refers to the array containing the String selections of all our different filtering criteria
     * @param sortingCriteriaArray refers to the array containing the String selections of all our different sorting criteria
     * @param selectedFilteringCriteria refers to the array containing the String selections of all our different filtering criteria
     * @param singlePosition refers to the position of the user's selected sorting crietria in sortingCriteriaArray
     * @param profileSubCriteriaChoice refers to our user's preferences for the default fitlering criteria which will be used in this method
     * @param selectedSubCriteria refers to the array containing the selected sub criteria for the type of filtering represented by each index in the array
     * @param numberOfDefaultCriteria refers to the number of default filtering criteria in our application
     * @param subCriteria2D refers to an array list of hash maps where each hash map contains all possible sub criteria of the filtering criteria repreented at that position in the array list
     * @param filteringListModel refers to the model object in MVC architecture that will invoke its service() method to sort the restaurant list according to the chosen filtering criteria
     * @param sortingListModel refers to the model object in MVC architecture that will invoke its service() method to sort the restaurant list according to the chosen sorting criteria
     */
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
