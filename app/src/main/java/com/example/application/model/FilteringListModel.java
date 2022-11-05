package com.example.application.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.control.filtering.FilteringCriteria;
import com.example.application.backend.control.others.FirebaseRetrieval;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Restaurant;
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
        // FORMAT: attributeList = [filteringCriteria, actualCriteria, fullRestaurantList]

        //Retrieve account object and sort recommended list
        FilteringCriteria filteringCriteria = (FilteringCriteria) super.attributeList.get(0);
        filteringCriteria.addCriteria(super.attributeList.get(1));
        ArrayList<Restaurant> fullRestaurantList = (ArrayList<Restaurant>) super.attributeList.get(2);
        System.out.println("\nRecommendedList size before: "+ fullRestaurantList.size());
        ArrayList<Restaurant> recommendedList = filteringCriteria.filter(fullRestaurantList);
        System.out.println("\nRecommendedList size before: "+ recommendedList.size());


        //Hash Map to store string data
        Map<String, Object> map = new HashMap<>();
        map.put("recommendedList", recommendedList);

        //Get UserID
        String userID = "XFil8xUcH7MmzdqQSoFnTiwWWU92"; //mAuth.getCurrentUser().getUid();

        //Update database (This means update "recommendedList" key in "Account" child)
        mDatabase.child(userID).child("Account").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "List filtered and updated!", Toast.LENGTH_SHORT).show();
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
        System.out.println("By-pass2B");
        return;
    }
}
