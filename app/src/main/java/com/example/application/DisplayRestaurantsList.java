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
import com.example.application.backend.control.others.FirebaseRetrieval;
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
    String[] filteringCriteriaArray;
    ArrayList<Object> subCriteria2D = new ArrayList<>();
    int[] clickCounter;

    //Sorting dropdown stuff
    boolean[] selectedSortingCriteria;
    ArrayList<Integer> sortingCriteriaList = new ArrayList<>();
    String[] sortingCriteriaArray;
    int singlePosition = 2; //default sorting selection is travelling time (index 2)
    int[] multiPosition = {-1} ; //useless


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
                                                                userID = "XFil8xUcH7MmzdqQSoFnTiwWWU92";//For testing purposes
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
                sortingDropDown();
            }
        });

        //FilteringCriteria dropdown (Expand for code)
        buttonFilterBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filteringDropDown();
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

        initialFilteringList.add(FilteringStoreFactory.getDatastore(filteringCriteriaArray[0]));
        initialFilteringList.add(FilteringStoreFactory.getDatastore(filteringCriteriaArray[1]));
        initialSortingList.add(SortingStoreFactory.getDatastore(sortingCriteriaArray[singlePosition]));
        System.out.println("THE OBEJCT: "+sortingCriteriaArray[singlePosition]);
        System.out.println("THE OBEJCT: "+filteringCriteriaArray[0]);
        System.out.println("THE OBEJCT: "+filteringCriteriaArray[1]);
        //FirebaseRetrieval.retrieveFullRestaurantList(mAuth, mDatabase, DisplayRestaurantsList.this, initialFilteringList, filteringListModel);
        FirebaseRetrieval.pureSorting(mAuth, mDatabase, DisplayRestaurantsList.this, initialSortingList, sortingListModel); //add the list into this

    }

    public void retrieveAndDisplay(){
        ArrayList<Restaurant> arrayList = new ArrayList<>();
        myAdapter.setArrayList(arrayList);
        mDatabase.child(userID).child("Account").child("recommendedList")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();

                        for (DataSnapshot child : children) {
                            Restaurant restaurant = child.getValue(Restaurant.class);
                            arrayList.add(restaurant);
                            System.out.println("Size here: "+ arrayList.size());
                        }
                        myAdapter.notifyDataSetChanged();
                        return;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DisplayRestaurantsList.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
        return;
    }

    @Override
    public void update(Observable observable, Object o) {
        this.retrieveAndDisplay();
        System.out.println("UPDATE IS CALLED");
    }

    public void getAllCriteria(){
        //Retrieving all sorting criteria from sorting_configuration.txt
        try {
            System.out.println("My Path");
            Scanner sortingConfigurationReader = new Scanner(getAssets().open("sorting_configuration.txt"));
            Scanner filteringConfigurationReader = new Scanner(getAssets().open("filtering_configuration.txt"));
            Scanner subCriteriaConfigurationReader = new Scanner(getAssets().open("sub_criteria.txt"));

            while(sortingConfigurationReader.hasNextLine()) {
                String line  = sortingConfigurationReader.nextLine();
                String[] parts = line.split("=");
                sortingConfiguration.put(parts[0], parts[1]);
            }
            sortingConfigurationReader.close();

            while(filteringConfigurationReader.hasNextLine()) {
                String line  = filteringConfigurationReader.nextLine();
                String[] parts = line.split("=");
                filteringConfiguration.put(parts[0], parts[1]);
            }
            filteringConfigurationReader.close();

            int count=0; int index=0;
            while(subCriteriaConfigurationReader.hasNextLine()) {
                if (count==0){
                    subCriteria2D.add(new HashMap<String, String>());
                }
                String line  = subCriteriaConfigurationReader.nextLine();
                if (line.compareTo("NEXT")==0) {
                    count=0; index++; continue;
                }
                String[] parts = line.split("=");
                ((Map<String, String>) subCriteria2D.get(index)).put(parts[0], parts[1]);
            }
            subCriteriaConfigurationReader.close();

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

        filteringCriteriaArray = new String[filteringConfiguration.size()];
        for (int i=0; i<filteringConfiguration.size(); i++){
            filteringCriteriaArray[i] = filteringConfiguration.get(String.valueOf(i));
        }

        clickCounter = new int[filteringConfiguration.size()];
        for (int i=0; i<filteringConfiguration.size(); i++){
            clickCounter[i] = 0;
        }

                                //Testing
                                for (int i=0; i<sortingConfiguration.size(); i++){
                                    System.out.println("Testing: "+ sortingCriteriaArray[i]);
                                }
                                //Testing
                                for (int i=0; i<filteringConfiguration.size(); i++){
                                    System.out.println("TestingB: "+ filteringCriteriaArray[i]);
                                }
                                //Testing
                                for (int i=0; i<subCriteria2D.size(); i++){
                                    Map<String,String> hashy = (Map<String, String>) subCriteria2D.get(i);
                                    for (int j=0; j<hashy.size();j++){
                                        System.out.println("hashy value: "+ hashy.get(String.valueOf(j)));
                                    }
                                    System.out.println();
                                }
    }

    public void sortingDropDown(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRestaurantsList.this);
        builder.setTitle("Select Sorting Criteria");


        builder.setSingleChoiceItems(sortingCriteriaArray, singlePosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                singlePosition = i;
                //Toast.makeText(DisplayRestaurantsList.this, "Selected position: "+String.valueOf(i), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(DisplayRestaurantsList.this, "Test "+String.valueOf(singlePosition), Toast.LENGTH_SHORT).show();
                SortingCriteria sortingCriteria = SortingStoreFactory.getDatastore(sortingCriteriaArray[singlePosition]);
                ArrayList<Object> initialSortingList = new ArrayList<Object>();
                initialSortingList.add(sortingCriteria);
                FirebaseRetrieval.pureSorting(mAuth, mDatabase, DisplayRestaurantsList.this, initialSortingList, sortingListModel);
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

            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    public void filteringDropDown(){
            AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRestaurantsList.this);
            builder.setTitle("Select Filtering Criteria");
            builder.setCancelable(false);

            System.out.println("FILTERING DROP DOWN FUNCTION RAN");
            builder.setMultiChoiceItems(filteringCriteriaArray, selectedFilteringCriteria, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    System.out.println("NUMBER: "+i);
                    if (clickCounter[i]%2==0) {
                        //get hashmap with all sub criteria
                        Map<String, String> hashy = (Map<String, String>) subCriteria2D.get(i);
                        //create string array with sub criteria
                        String[] subCriterialist = new String[hashy.size()];
                        for (int j = 0; j < hashy.size(); j++) {
                            subCriterialist[j] = hashy.get(String.valueOf(j));
                        }
                        subCriteriaDropDown(subCriterialist);
                        clickCounter[i]++;
                    }
                    else{clickCounter[i]++;}
                    System.out.println("clickCounter: "+clickCounter[i]);
                }
            });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

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

    public void subCriteriaDropDown(String[] subCriteriaList){
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRestaurantsList.this);
        builder.setTitle("Select Sorting Criteria");


        builder.setSingleChoiceItems(subCriteriaList, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Toast.makeText(DisplayRestaurantsList.this, "Selected position: "+String.valueOf(i), Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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

            }
        });

        AlertDialog mDialog = builder.create();
        mDialog.show();
    } //end of function
} //end of class
/*
//Testing
                                for (String name: sortingConfiguration.keySet()) {
                                        Toast.makeText(DisplayRestaurantsList.this, name, Toast.LENGTH_SHORT).show();
                                        }

 */