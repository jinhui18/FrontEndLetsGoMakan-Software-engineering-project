package com.example.application.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.control.others.FirebaseRetrieval;
import com.example.application.backend.control.sorting.SortingCriteria;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Restaurant;
import com.example.application.backend.enums.TypesOfDietaryRequirements;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SortingListModel extends Model{

    public SortingListModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    @Override
    public void service() {
        // FORMAT: attributeList = [filteringCriteria]

        //Retrieve account object and sort recommended list
        ArrayList<Restaurant> recommendedList = FirebaseRetrieval.retrieveRecommendedList(mAuth, mDatabase, context);
        SortingCriteria sortingCriteria = (SortingCriteria) super.attributeList.get(0);
        sortingCriteria.sort(recommendedList);

        //Testing
        Restaurant r1 = new Restaurant(true, (float) 1, 5, (float) 5, "Bukit Timah St12", "Sedapz", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Green_Dot_logo.svg/1200px-Green_Dot_logo.svg.png", 12, TypesOfDietaryRequirements.NONE);
        Restaurant r2 = new Restaurant(true, (float) 2, 4, (float) 4, "Bukit Timah St12", "Sedapz", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Green_Dot_logo.svg/1200px-Green_Dot_logo.svg.png", 12, TypesOfDietaryRequirements.HALAL);
        Restaurant r3 = new Restaurant(true, (float) 3, 3, (float) 3, "Bukit Timah St12", "Sedapz", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Green_Dot_logo.svg/1200px-Green_Dot_logo.svg.png", 12, TypesOfDietaryRequirements.VEGETARIAN);
        Restaurant r4 = new Restaurant(true, (float) 4, 2, (float) 2, "Bukit Timah St12", "Sedapz", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Green_Dot_logo.svg/1200px-Green_Dot_logo.svg.png", 12, TypesOfDietaryRequirements.BOTH);

        ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
        restaurantArrayList.add(r1);
        restaurantArrayList.add(r4);
        restaurantArrayList.add(r3);
        restaurantArrayList.add(r2);


        //Hash Map to store string data
        Map<String, Object> map = new HashMap<>();
        map.put("recommendedList", restaurantArrayList);

        //Get UserID
        String userID = "ss28rX1fEiV1bRtmkrJmCksoCZ43";//mAuth.getCurrentUser().getUid();

        //Update database (This means update "recommendedList" key in "Account" child)
        mDatabase.child(userID).child("Account").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "List sorted and updated!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failure to sort list", Toast.LENGTH_SHORT).show();
            }
        });

        //Informing observers to update
        setChanged();
        notifyObservers();
    }
}
