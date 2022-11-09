package com.example.application;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.backend.control.filtering.FilteringCriteria;
import com.example.application.backend.control.filtering.FilteringStoreFactory;
import com.example.application.backend.control.others.FirebaseRetrieval;
import com.example.application.backend.control.sorting.SortingCriteria;
import com.example.application.backend.control.sorting.SortingStoreFactory;
import com.example.application.backend.entity.Restaurant;
import com.example.application.model.FilteringListModel;
import com.example.application.model.Model;
import com.example.application.model.SortingListModel;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class DisplayRestaurantsList extends AppCompatActivity implements Observer, OnMapReadyCallback {
    //Widgets and associated stuff
    //private TextView textView;
    private Button buttonSortBy, buttonFilterBy;
    private RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MyAdapter myAdapter;

    //Filtering Dropdown stuff
    boolean[] selectedFilteringCriteria;
    String[] filteringCriteriaArray;
    ArrayList<Object> subCriteria2D = new ArrayList<>();
    int[] clickCounter1;
    int[] profileSubCriteriaChoice; //used to pre-select subCriteria option according to users' default/previous selection
    String[] selectedSubCriteria; //used to store the selected subcriteria for filtering Criteria
    int subCriteriaPosition;
    int numberOfDefaultCriteria = 1; //number of default filtering criteria (This needs to be set

    //Sorting dropdown stuff
    boolean[] selectedSortingCriteria;
    ArrayList<Integer> sortingCriteriaList = new ArrayList<>();
    String[] sortingCriteriaArray;
    int singlePosition = 2; //default sorting selection is travelling time (index 2)

    //Firebase
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String userID;

    //MVC related
    Model filteringListModel;
    Model sortingListModel;

    //map related
    GoogleMap gMap;
    private LocationRequest locationRequest;
    private static final int DEFAULT_ZOOM = 15;
    boolean useCurLoc;

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
        userID = mAuth.getCurrentUser().getUid();
                                                                //userID = "ytqpxJbhKISbEHjoFMqyd6G1j412";//For testing purposes
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        // this function sets the back button on top of the screen
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //map related
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapRestaurantList);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        //MVC Stuff and Observer Pattern
        sortingListModel = new SortingListModel(mAuth, mDatabase, DisplayRestaurantsList.this);
        filteringListModel = new FilteringListModel(mAuth, mDatabase, DisplayRestaurantsList.this);
        sortingListModel.addObserver(this);
        //filteringListModel.addObserver(this);

        //Widgets and Associated stuff
        buttonSortBy = findViewById(R.id.buttonSortBy);
        buttonFilterBy = findViewById(R.id.buttonFilterBy);

        //SortingCriteria dropdown
        buttonSortBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortingDropDown();
            }
        });

        //FilteringCriteria dropdown
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

        SortingCriteria sortingCriteria = SortingStoreFactory.getDatastore(sortingCriteriaArray[singlePosition]);
        ArrayList<Object> sortingList = new ArrayList<Object>();
        sortingList.add(sortingCriteria);

        //From selectedFilteringCriteria array instantiate required filteringCriteria objects
        ArrayList<Object> filteringList = new ArrayList<Object>();
        ArrayList<FilteringCriteria> filteringCriteriaList = new ArrayList<>(); //Store all needed filtering criteria object
        for (int k=0; k<filteringCriteriaArray.length; k++) { //for default, need to use all filteringCriteria
                selectedFilteringCriteria[k]=true;
                System.out.println("Outside K value: "+k);
                FilteringCriteria filteringCriteria = FilteringStoreFactory.getDatastore(filteringCriteriaArray[k]);
                //subCriteria to be added later on
                filteringCriteriaList.add(filteringCriteria);
        }//end for
        System.out.println("FilteringCriteriaList length: "+filteringCriteriaList.size());
        //Pass in everything to method
        filteringList.add(sortingList); //needed to instantiate sorting controller in filteringModel class
        filteringList.add(sortingListModel);
        filteringList.add(filteringCriteriaList); //Format: [sortingList, sortingListModel, ArrayList<FC> FCList, FullRestList]
        FirebaseRetrieval.defaultFilterAndSort(mAuth, mDatabase, DisplayRestaurantsList.this, filteringList, profileSubCriteriaChoice, selectedSubCriteria, numberOfDefaultCriteria, subCriteria2D, filteringListModel);

        //add subcriteria2D in as well
        //need to update profileSubCriteriaChoice (for initial dot and selectedSubCriteria (mb optional as it gets overwritten)

        //for future filtering binaries, null criteria must be considered in .filter() function
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        getUserLocation();
    }

    public void getUserLocation() {
        mDatabase.child(userID).child("Account").child("Use Current Location")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        useCurLoc = snapshot.getValue(boolean.class);
                        showLocation(useCurLoc);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DisplayRestaurantsList.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showLocation(boolean useCurLoc) {
        if (useCurLoc == true) {
            System.out.println("using current location");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlertDialog();
                return;
            }
            gMap.setMyLocationEnabled(true);

            mDatabase.child(userID).child("Account").child("Current Location")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String data = snapshot.getValue(String.class);
                            double latitude = Double.parseDouble(data.substring(data.indexOf("(") + 1, data.indexOf(",")));
                            double longitude = Double.parseDouble(data.substring(data.indexOf(",") + 1, data.indexOf(")")));
                            LatLng location = new LatLng(latitude, longitude);
                            gMap.setMyLocationEnabled(true);
                            gMap.getUiSettings().setMyLocationButtonEnabled(true);
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    location, DEFAULT_ZOOM));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DisplayRestaurantsList.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            System.out.println("use chosen location");
            mDatabase.child(userID).child("Account").child("Chosen Location")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String data = snapshot.getValue(String.class);
                            double latitude = Double.parseDouble(data.substring(data.indexOf("(") + 1, data.indexOf(",")));
                            double longitude = Double.parseDouble(data.substring(data.indexOf(",") + 1, data.indexOf(")")));
                            LatLng location = new LatLng(latitude, longitude);
                            gMap.setMyLocationEnabled(true);
                            gMap.getUiSettings().setMyLocationButtonEnabled(true);
                            gMap.addMarker(new MarkerOptions().position(location).title("Chosen Location"));
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    location, DEFAULT_ZOOM));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DisplayRestaurantsList.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void showOnMap(ArrayList<Restaurant> restList) {
        //gMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter getApplicationContext());
        System.out.println("size " + restList.size());
        for(int index = 0; index < restList.size(); index++){
            System.out.println("index " + index);
            LatLng restaurant_location = new LatLng(restList.get(index).getLatitude(), restList.get(index).getLongitude());
            gMap.addMarker(new MarkerOptions().position(restaurant_location).title(restList.get(index).getName()));

            //gMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(this, restList.get(index)));
        }
    }

    public void retrieve(){
        ArrayList<Restaurant> restList = new ArrayList<>();

        mDatabase.child(userID).child("Account").child("recommendedList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();

                for (DataSnapshot child : children) {
                    Restaurant restaurant = child.getValue(Restaurant.class);
                    restList.add(restaurant);
                }
                showOnMap(restList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DisplayRestaurantsList.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showAlertDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DisplayRestaurantsList.this);
        builder.setTitle("Need Location Permission!");
        builder.setMessage("This app needs location permission. Close the app and allow location permission in phone settings.");
        builder.setPositiveButton("Close App", (dialog, which) -> {
            dialog.cancel();
            finishAffinity();
        });
        final android.app.AlertDialog permissionAlert = builder.create();
        permissionAlert.show();
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
        this.retrieve();
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

        clickCounter1 = new int[filteringConfiguration.size()];
        for (int i=0; i<filteringConfiguration.size(); i++){
            clickCounter1[i] = 0;
        }
        profileSubCriteriaChoice = new int[filteringConfiguration.size()];
        for (int i=0; i<filteringConfiguration.size(); i++){
            profileSubCriteriaChoice[i] = 0;
        }

        selectedSubCriteria = new String[filteringConfiguration.size()];
        for (int i=0; i<filteringConfiguration.size(); i++){
            selectedSubCriteria[i] = null;
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

        builder.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
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
                    if (clickCounter1[i]%2==1) { //Can also use selectedFilteringCriteria
                        //get hashmap with all sub criteria
                        Map<String, String> hashy = (Map<String, String>) subCriteria2D.get(i);
                        //create string array with sub criteria
                        String[] subCriterialist = new String[hashy.size()];
                        for (int j = 0; j < hashy.size(); j++) {
                            subCriterialist[j] = hashy.get(String.valueOf(j));
                        }

                        subCriteriaDropDown(subCriterialist, i); //pass in filtering criteria index for sub criteria retrieval
                        clickCounter1[i]++;
                    }
                    else{clickCounter1[i]++;}
                    System.out.println("clickCounter: "+clickCounter1[i]);
                }
            });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //get the sortingCriteria here using the singlePosition (keeps track of last-used sortingCriteria
                    SortingCriteria sortingCriteria = SortingStoreFactory.getDatastore(sortingCriteriaArray[singlePosition]);
                    ArrayList<Object> sortingList = new ArrayList<Object>();
                    sortingList.add(sortingCriteria);

                    //From selectedFilteringCriteria array instantiate required filteringCriteria objects
                    ArrayList<Object> filteringList = new ArrayList<Object>();
                    ArrayList<FilteringCriteria> filteringCriteriaList = new ArrayList<>(); //Store all needed filtering criteria object
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
                    FirebaseRetrieval.filterAndSort(mAuth, mDatabase, DisplayRestaurantsList.this, filteringList, filteringListModel);
                }
            });

            builder.setNegativeButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // dismiss dialog
                    dialogInterface.dismiss();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // dismiss dialog
                    dialogInterface.dismiss();
                }
            });
            // show dialog
            builder.show();
    }

    public void subCriteriaDropDown(String[] subCriteriaList, int filteringCriteriaIndex){
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRestaurantsList.this);
        builder.setTitle("Select Sub Criteria");


        builder.setSingleChoiceItems(subCriteriaList, profileSubCriteriaChoice[filteringCriteriaIndex], new DialogInterface.OnClickListener() { //pre selected option is user's profile
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                subCriteriaPosition = i;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedSubCriteria[filteringCriteriaIndex] = subCriteriaList[subCriteriaPosition];
                profileSubCriteriaChoice[filteringCriteriaIndex] = subCriteriaPosition;
                for (int k=0; k<2; k++){
                    System.out.println("CHOSEN CRITERIA: "+selectedSubCriteria[k]);
                }
            }
        });

        builder.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // dismiss dialog
                dialogInterface.dismiss();
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
/*
                    for (int k=0; k<2; k++)
        System.out.println("boolean array: "+selectedFilteringCriteria[k]);
        }

 */
/*
        //First time display (involves default filtering and sorting)
            //Filter first then sort
        ArrayList<Object> initialSortingList = new ArrayList<Object>(); //To put into controller
        ArrayList<Object> initialFilteringList = new ArrayList<>();

        initialFilteringList.add(FilteringStoreFactory.getDatastore(filteringCriteriaArray[0]));
        initialFilteringList.add(FilteringStoreFactory.getDatastore(filteringCriteriaArray[1]));
        initialSortingList.add(SortingStoreFactory.getDatastore(sortingCriteriaArray[singlePosition])); //this step adds the default sorting Criteria


        FirebaseRetrieval.pureSorting(mAuth, mDatabase, DisplayRestaurantsList.this, initialSortingList, sortingListModel); //add the list into this

 */