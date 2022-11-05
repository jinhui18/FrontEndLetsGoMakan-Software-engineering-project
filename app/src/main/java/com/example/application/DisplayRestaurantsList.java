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

import com.example.application.backend.control.filtering.FilteringCriteria;
import com.example.application.backend.control.filtering.FilteringStoreFactory;
import com.example.application.backend.control.sorting.CrowdLevel;
import com.example.application.backend.control.sorting.SortingCriteria;
import com.example.application.backend.control.sorting.SortingStoreFactory;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Restaurant;
import com.example.application.controller.Controller;
import com.example.application.model.FilteringListModel;
import com.example.application.model.Model;
import com.example.application.model.SortingListModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class DisplayRestaurantsList extends AppCompatActivity implements Observer {
    //Widgets and associated stuff
    //private TextView textView;
    private Button button, buttonSortBy, buttonFilterBy;
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MyAdapter myAdapter;
    //Filtering Dropdown stuff
    boolean[] selectedFilteringCriteria;
    ArrayList<Integer> filteringCriteriaList = new ArrayList<>();
    String[] filteringCriteriaArray = {"Halal", "Ratings", "Crowd Level"};
    //Sorting dropdown stuff
    boolean[] selectedSortingCriteria;
    ArrayList<Integer> sortingCriteriaList = new ArrayList<>();
    String[] sortingCriteriaArray;
    int singlePosition = 3; //default sorting selection is travelling time (index 3)
    int[] multiPosition = {-1} ; //

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
                                                                String userID; //For testing purposes

    //MVC related
    Model filteringListModel;
    Model sortingListModel;

    // store data store configuration
    private final static Map<String, String> sortingConfiguration = new HashMap<String, String>();
    private final static Map<String, String> filteringConfiguration = new HashMap<String, String>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_restaurants_list);

        //Reading all sorting and filtering criteria binaries
        this.getAllCriteria();

        //Firebase (has to come first)
        mAuth = FirebaseAuth.getInstance();
        //userID = firebaseAuth.getCurrentUser().getUid();
                                                                userID = "ss28rX1fEiV1bRtmkrJmCksoCZ43";//For testing purposes
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        //MVC Stuff and Observer Pattern
        sortingListModel = new SortingListModel(mAuth, mDatabase, DisplayRestaurantsList.this);
        filteringListModel = new FilteringListModel(mAuth, mDatabase, DisplayRestaurantsList.this);
        sortingListModel.addObserver(this);
        filteringListModel.addObserver(this);

        //Widgets and Associated stuff
        buttonSortBy = findViewById(R.id.buttonSortBy);
        buttonFilterBy = findViewById(R.id.buttonFilterBy);
        button = findViewById(R.id.button6);//For testing purposes


        //SortingCriteria dropdown (Expand for code)
        buttonSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRestaurantsList.this);
                builder.setTitle("Select Sorting Criteria");


                builder.setSingleChoiceItems(sortingCriteriaArray, singlePosition, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        singlePosition = i;
                        Toast.makeText(DisplayRestaurantsList.this, "Selected position: "+String.valueOf(i), Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DisplayRestaurantsList.this, "Test "+String.valueOf(singlePosition), Toast.LENGTH_SHORT).show();
                        SortingCriteria sortingCriteria = SortingStoreFactory.getDatastore(sortingCriteriaArray[singlePosition]);
                        ArrayList<Object> initialSortingList = new ArrayList<Object>();
                        initialSortingList.add(sortingCriteria);
                        Controller sortingController = new Controller(sortingListModel, initialSortingList);
                        sortingController.handleEvent();
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
        //FilteringCriteria dropdown (Expand for code)
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


        //Restaurant list stuff
        recyclerView = findViewById(R.id.recycler_id);
        selectedFilteringCriteria = new boolean[filteringCriteriaArray.length];
        selectedSortingCriteria = new boolean[sortingCriteriaArray.length];
        linearLayoutManager = new LinearLayoutManager(DisplayRestaurantsList.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(DisplayRestaurantsList.this,null);
        recyclerView.setAdapter(myAdapter);


        //First time display (involves default filtering and sorting)
            //Filter first then sort
        ArrayList<Object> initialSortingList = new ArrayList<Object>(); //To put into controller
        ArrayList<Object> initialFilteringList = new ArrayList<>();

        //initialFilteringList.add(FilteringStoreFactory.getDatastore(filteringCriteriaArray[]));
        initialSortingList.add(SortingStoreFactory.getDatastore(sortingCriteriaArray[singlePosition]));
        System.out.println("THE OBEJCT: "+sortingCriteriaArray[singlePosition]);

        //Controller initialFilteringController = new Controller(filteringListModel, initialFilteringList);
        Controller initialSortingController = new Controller(sortingListModel, initialSortingList);

        //For testing, assume that recommendedList == fullList
        //initialFilteringController.handleEvent();
        initialSortingController.handleEvent();

        this.retrieveAndDisplay(); //displays recyclerView
    }

    public void retrieveAndDisplay(){
        ArrayList<Restaurant> arrayList = new ArrayList<>();
        myAdapter.setArrayList(arrayList);
        mDatabase.child(userID).child("Account").child("recommendedList")
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

    @Override
    public void update(Observable observable, Object o) {
        this.retrieveAndDisplay();
    }

    public void getAllCriteria(){
        //Retrieving all sorting criteria from sorting_configuration.txt
        try {
            System.out.println("My Path");
            Scanner sortingConfigurationReader = new Scanner(getAssets().open("sorting_configuration.txt"));
            Scanner filteringConfigurationReader = new Scanner(getAssets().open("filtering_configuration.txt"));

            while(sortingConfigurationReader.hasNextLine()) {
                String line  = sortingConfigurationReader.nextLine();
                String[] parts = line.split("=");
                sortingConfiguration.put(parts[0], parts[1]);
            }
            sortingConfigurationReader.close();

            while(filteringConfigurationReader.hasNextLine()) {
                String line  = sortingConfigurationReader.nextLine();
                String[] parts = line.split("=");
                filteringConfiguration.put(parts[0], parts[1]);
            }
            filteringConfigurationReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialising sortingArray and filteringArray
        sortingCriteriaArray = new String[sortingConfiguration.size()];
        for (int i=0; i<sortingConfiguration.size(); i++){
            sortingCriteriaArray[i] = sortingConfiguration.get(String.valueOf(i));
        }
        /*
        filteringCriteriaArray = new String[filteringConfiguration.size()];
        for (int i=0; i<sortingConfiguration.size(); i++){
            filteringCriteriaArray[i] = filteringConfiguration.get(String.valueOf(i));
        } */
                                //Testing
                                for (String name: sortingConfiguration.keySet()) {
                                    Toast.makeText(DisplayRestaurantsList.this, name, Toast.LENGTH_SHORT).show();
                                }
                                //Testing
                                for (int i=0; i<sortingConfiguration.size(); i++){
                                    System.out.println("Testing: "+ sortingCriteriaArray[i]);
                                }
    }
}

