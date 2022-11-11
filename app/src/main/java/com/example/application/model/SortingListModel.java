package com.example.application.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.control.sorting.SortingCriteria;
import com.example.application.backend.entity.Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * SortingListModel is the model child class that inherits from the abstract Model parent class
 * It inherits and implements the service() method with the logic to sort the retrieved provided list of restaurants and store the eventual list in the database.
 * To perform its service logic, SortingListModel needs an attribute list containing a sortingList, sortingListModel reference, an arraylist of filtering criteria, and the full restaurant list
 * The sortingList is the attribute list needed by the sortingListModel to perform its service logic of sorting the recommended list
 * The sortingList will contain the sorting criteria as well as the restaurant list to be sorted
 * @author Isaac
 * @version 1.0
 * @since 2022-11-11
 */
public class SortingListModel extends Model{

    /**
     * This is the overridden constructor for SortingListModel
     * @param mAuth refers to our firebase authenticator reference
     * @param mDatabase refers to our firebase realtime database reference
     * @param context refers to the UI activity page (View class) in which the event happened
     */
    public SortingListModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    /**
     * This is the inherited and implemented service() model containing the logic to sort the given restaurant list
     * The process involves sorting the restaurant list with the sorting criteria passed in from the attribute list (sortingList),
     * storing the sorted recommended list in a hashmap and storing the list in the database
     */
    @Override
    public void service() {
        // FORMAT: attributeList = [sortingCriteria, recommendedList]

        //Retrieve account object and sort recommended list
        SortingCriteria sortingCriteria = (SortingCriteria) super.attributeList.get(0);
        ArrayList<Restaurant> recommendedList = (ArrayList<Restaurant>) super.attributeList.get(1);
        System.out.println("Address of recommendedList: "+recommendedList);
        System.out.println("\nRecommendedList size before: "+ recommendedList.size());
        sortingCriteria.sort(recommendedList);
        System.out.println("\nRecommendedList size after: "+ recommendedList.size());


        //Hash Map to store string data
        Map<String, Object> map = new HashMap<>();
        map.put("recommendedList", recommendedList);

        //Get UserID
        String userID = mAuth.getCurrentUser().getUid();

        //Update database (This means update "recommendedList" key in "Account" child)
        mDatabase.child(userID).child("Account").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "List sorted and updated!", Toast.LENGTH_SHORT).show();
                System.out.println("Database updated! Proceeding to update visuals");
                //Informing observers to update
                setChanged();
                notifyObservers();
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failure to sort list", Toast.LENGTH_SHORT).show();
            }
        });
        System.out.println("By-pass 2");
        return;
    }
}