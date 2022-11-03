package com.example.application;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.backend.entity.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DisplayRestaurantsList extends AppCompatActivity {

    //private TextView textView;
    private Button button, buttonSortBy, buttonFilterBy;
    private RecyclerView recyclerView;
    private ArrayList<Restaurant> arrayList;

    // creating a variable for
    // our Firebase Database.
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    // creating a variable for our
    // Database Reference for Firebase.
    private DatabaseReference databaseReference;

    String userID;


    boolean[] selectedFilteringCriteria;
    ArrayList<Integer> filteringCriteriaList = new ArrayList<>();
    String[] filteringCriteriaArray = {"Travelling Time", "Ratings", "Crowd Level"};


    boolean[] selectedSortingCriteria;
    ArrayList<Integer> sortingCriteriaList = new ArrayList<>();
    String[] sortingCriteriaArray = {"Travelling Time", "Ratings", "Crowd Level"};

    int position = 0;

    public DisplayRestaurantsList() {
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_restaurants_list);

        buttonSortBy = findViewById(R.id.buttonSortBy);
        buttonFilterBy = findViewById(R.id.buttonFilterBy);
        recyclerView = findViewById(R.id.recycler_id);

        firebaseAuth = FirebaseAuth.getInstance();
        //userID = firebaseAuth.getCurrentUser().getUid();
        userID = "Usl1ufnfyEfevGFTWxu3nxSwdCt2";

        // below line is used to get the instance
        // of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app");

        // below line is used to get
        // reference for our database.
        //databaseReference = firebaseDatabase.getReference(userID).child("Account").child("recommendedList");
        databaseReference = firebaseDatabase.getReference();



        selectedFilteringCriteria = new boolean[filteringCriteriaArray.length];

        selectedSortingCriteria = new boolean[sortingCriteriaArray.length];

        buttonSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRestaurantsList.this);
                builder.setTitle("Select Sorting Criteria");


                builder.setSingleChoiceItems(sortingCriteriaArray, position, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        position = i;
                        //mResult.setText(listItems[i]);
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < sortingCriteriaList.size(); j++) {
                            // concat array value
                            stringBuilder.append(sortingCriteriaArray[sortingCriteriaList.get(j)]);
                            // check condition
                            if (j != sortingCriteriaList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append("\n");
                            }
                        }
                        // set text on textView
                        // if you want to show the selected criteria on the button
                        //buttonFilterBy.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedSortingCriteria.length; j++) {
                            // remove all selection
                            selectedSortingCriteria[j] = false;
                            // clear language list
                            sortingCriteriaList.clear();
                            // clear text view value
                            //buttonSortBy.setText("");
                        }
                    }
                });

                AlertDialog mDialog = builder.create();
                mDialog.show();
            }
        });


        buttonFilterBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRestaurantsList.this);
                builder.setTitle("Select Filtering Criteria");
                builder.setCancelable(false);


                builder.setMultiChoiceItems(filteringCriteriaArray, selectedFilteringCriteria, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            filteringCriteriaList.add(i);
                            // Sort array list
                            Collections.sort(filteringCriteriaList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            filteringCriteriaList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < filteringCriteriaList.size(); j++) {
                            // concat array value
                            stringBuilder.append(filteringCriteriaArray[filteringCriteriaList.get(j)]);
                            // check condition
                            if (j != filteringCriteriaList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append("\n");
                            }
                        }
                        // set text on textView
                        // if you want to show the selected criteria on the button
                        //buttonFilterBy.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedFilteringCriteria.length; j++) {
                            // remove all selection
                            selectedFilteringCriteria[j] = false;
                            // clear language list
                            filteringCriteriaList.clear();
                            // clear text view value
                            buttonSortBy.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }


        });

        button = findViewById(R.id.button6);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DisplayRestaurantsList.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        MyAdapter myAdapter = new MyAdapter(DisplayRestaurantsList.this,arrayList);
        recyclerView.setAdapter(myAdapter);

        databaseReference.child(userID).child("Account").child("recommendedList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();

                        for (DataSnapshot child : children) {
                            Restaurant restaurant = child.getValue(Restaurant.class);
                            arrayList.add(restaurant);
                        }
                        myAdapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DisplayRestaurantsList.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}

