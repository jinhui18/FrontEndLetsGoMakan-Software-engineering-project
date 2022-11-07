package com.example.application.backend.control.others;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.DisplayRestaurantsList;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Restaurant;
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

public class FirebaseRetrieval {
    public static void pureSorting(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context, ArrayList<Object> attributeList, Model sortingListModel){
        final ArrayList<Account> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = "XFil8xUcH7MmzdqQSoFnTiwWWU92";//user.getUid();

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
        String userID = "XFil8xUcH7MmzdqQSoFnTiwWWU92";//user.getUid();

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
            Model filteringListModel
    ) {
        final ArrayList<Account> arrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = "XFil8xUcH7MmzdqQSoFnTiwWWU92";//user.getUid();

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


}
