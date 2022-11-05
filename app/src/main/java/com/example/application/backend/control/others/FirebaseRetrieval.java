package com.example.application.backend.control.others;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.DisplayRestaurantsList;
import com.example.application.backend.entity.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseRetrieval {
    public static ArrayList<Restaurant> retrieveRecommendedList(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context){
        ArrayList<Restaurant> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = "ss28rX1fEiV1bRtmkrJmCksoCZ43";//user.getUid();

        mDatabase.child(userID).child("Account").child("recommendedList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();

                        for (DataSnapshot child : children) {
                            Restaurant restaurant = child.getValue(Restaurant.class);
                            arrayList.add(restaurant);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
        return arrayList;
    }

    public static ArrayList<Restaurant> retrieveFullRestaurantList(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context){
        ArrayList<Restaurant> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = "ss28rX1fEiV1bRtmkrJmCksoCZ43";//user.getUid();

        mDatabase.child(userID).child("Account").child("fullRestaurantList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();

                        for (DataSnapshot child : children) {
                            Restaurant restaurant = child.getValue(Restaurant.class);
                            arrayList.add(restaurant);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
        return arrayList;
    }
}
