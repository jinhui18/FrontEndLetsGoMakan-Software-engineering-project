package com.example.application.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.application.R;
import com.example.application.backend.control.others.FirebaseRetrieval;
import com.example.application.backend.entity.Restaurant;
import com.example.application.model.FilteringListModel;
import com.example.application.model.Model;
import com.example.application.model.SortingListModel;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

/**
 * DisplayRestaurantListUI is the View class that displays the UI elements showing our recommended restaurant list in different representations
 * It includes a scrollable list and an interactive map with pins demarcating the locations of the restaurants
 * @author Isaac
 * @version 2.0
 * @since 2022-11-12
 */
public class DisplayRestaurantsListUI extends AppCompatActivity implements Observer, OnMapReadyCallback {
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
    private Restaurant restaurant;
    ArrayList<Restaurant> restList = new ArrayList<>();

    // store data store configuration
    private final static Map<String, String> sortingConfiguration = new HashMap<String, String>();
    private final static Map<String, String> filteringConfiguration = new HashMap<String, String>();

    /**
     * This is the onCreate() method that is called when the application first displays this activity class
     * @param savedInstanceState
     */
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
        sortingListModel = new SortingListModel(mAuth, mDatabase, DisplayRestaurantsListUI.this);
        filteringListModel = new FilteringListModel(mAuth, mDatabase, DisplayRestaurantsListUI.this);
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
        linearLayoutManager = new LinearLayoutManager(DisplayRestaurantsListUI.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(DisplayRestaurantsListUI.this,null);
        recyclerView.setAdapter(myAdapter);


        FirebaseRetrieval.defaultFilterAndSort(mAuth, mDatabase, DisplayRestaurantsListUI.this, filteringCriteriaArray, sortingCriteriaArray, selectedFilteringCriteria, singlePosition, profileSubCriteriaChoice, selectedSubCriteria, numberOfDefaultCriteria, subCriteria2D, filteringListModel, sortingListModel);

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
        mDatabase.child(userID).child("Account").child("useCurrentLocation")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        useCurLoc = snapshot.getValue(boolean.class);
                        showLocation(useCurLoc);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DisplayRestaurantsListUI.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
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

            mDatabase.child(userID).child("Account").child("currentLocation")
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
                            Toast.makeText(DisplayRestaurantsListUI.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            System.out.println("use chosen location");
            mDatabase.child(userID).child("Account").child("chosenLocation")
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
                            gMap.addMarker(new MarkerOptions().position(location).title("Chosen Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    location, DEFAULT_ZOOM));
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(DisplayRestaurantsListUI.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void showOnMap(ArrayList<Restaurant> restList) {
        //gMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter getApplicationContext());
        System.out.println("size " + restList.size());
        gMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(this, restList));
        for(int index = 0; index < restList.size(); index++){
            System.out.println("index " + index);
            LatLng restaurant_location = new LatLng(restList.get(index).getLatitude(), restList.get(index).getLongitude());
            gMap.addMarker(new MarkerOptions().position(restaurant_location).title(restList.get(index).getName()));
        }
        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                String name = marker.getTitle();
                for(int index = 0; index < restList.size(); index++) {
                    if (name.equals(restList.get(index).getName())) {
                        restaurant = restList.get(index);
                    }
                }
                Intent intent = new Intent(DisplayRestaurantsListUI.this, DisplayRestaurantUI.class);
                intent.putExtra("restaurant_url", restaurant.getImage());
                intent.putExtra("restaurant_name", restaurant.getName());
                intent.putExtra("restaurant_address", restaurant.getAddress());
                intent.putExtra("restaurant_latitude", restaurant.getLatitude());
                intent.putExtra("restaurant_longitude", restaurant.getLongitude());
                //intent.putExtra("restaurant_opening_hours_time", restaurant.getOpenCloseTimings());
                intent.putExtra("restaurant_crowd_level_value", restaurant.getCrowdLevel());
                intent.putExtra("ratings", restaurant.getRatings());
                startActivity(intent);
        }});
    }

    public void retrieve(){
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
                Toast.makeText(DisplayRestaurantsListUI.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showAlertDialog(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DisplayRestaurantsListUI.this);
        builder.setTitle("Need Location Permission!");
        builder.setMessage("This app needs location permission. Close the app and allow location permission in phone settings.");
        builder.setPositiveButton("Close App", (dialog, which) -> {
            dialog.cancel();
            finishAffinity();
        });
        final android.app.AlertDialog permissionAlert = builder.create();
        permissionAlert.show();
    }

    /**
     * retrieveAndDisplay() is used by our scrollable list to fetch recommended restaurant list data from the database and reflect the changes during runtime
     */
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
                        showOnMap(arrayList);
                        return;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(DisplayRestaurantsListUI.this, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
        return;
    }

    /**
     * update() method is used in the Observer pattern when the Model clas notifies this class when it completes any changes in the database
     * @param observable is the reference to the Model class (Observable)
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        gMap.clear();
        this.retrieveAndDisplay();
        System.out.println("UPDATE IS CALLED");
    }

    /**
     * getAllCriteria() is used to read all sorting, filtering and filtering sub criteria from our mutable text files upon start up of this activity
     */
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
                    subCriteria2D.add(new HashMap<String, String>()); //size can be zero if there are no subCriteria
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

    /**
     * This is the UI element that shows a drop down list of all sorting criteria
     */
    public void sortingDropDown(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRestaurantsListUI.this);
        builder.setTitle("Select Sorting Criteria text");


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
                System.out.println("Hello I am Isaac");
                FirebaseRetrieval.pureSorting(mAuth, mDatabase, DisplayRestaurantsListUI.this, sortingCriteriaArray, singlePosition, sortingListModel);
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

    /**
     * This is the UI element that shows a drop down list of all filtering criteria
     */
    public void filteringDropDown(){
            AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRestaurantsListUI.this);
            builder.setTitle("Select Filtering Criteria");
            builder.setCancelable(false);

            System.out.println("FILTERING DROP DOWN FUNCTION RAN");
            builder.setMultiChoiceItems(filteringCriteriaArray, selectedFilteringCriteria, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    System.out.println("NUMBER: "+i);
                    if (selectedFilteringCriteria[i]) { //Can also use selectedFilteringCriteria
                        //get hashmap with all sub criteria
                        Map<String, String> hashy = (Map<String, String>) subCriteria2D.get(i);
                        //create string array with sub criteria
                        String[] subCriterialist = new String[hashy.size()]; //This array can have size zero when no subCriteria
                        for (int j = 0; j < hashy.size(); j++) {
                            subCriterialist[j] = hashy.get(String.valueOf(j));
                        }
                        //if subCriteriaList size is 0, there are no subCriteria so no dropdown
                        if (subCriterialist.length!=0) subCriteriaDropDown(subCriterialist, i); //pass in filtering criteria index for sub criteria retrieval
                    }//end if
                }//end onClick
            });

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    FirebaseRetrieval.filterAndSort(mAuth, mDatabase, DisplayRestaurantsListUI.this, filteringCriteriaArray, sortingCriteriaArray, selectedSubCriteria,  selectedFilteringCriteria, singlePosition, filteringListModel, sortingListModel);
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

    /**
     * This is the UI element that shows a drop down list with all sub criteria of the user's selected filtering criteria
     * @param subCriteriaList contains the list of all sub criteria
     * @param filteringCriteriaIndex refers to the index of the selected filtering criteria
     */
    public void subCriteriaDropDown(String[] subCriteriaList, int filteringCriteriaIndex){
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayRestaurantsListUI.this);
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
                for (int k=0; k<filteringCriteriaArray.length; k++){
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