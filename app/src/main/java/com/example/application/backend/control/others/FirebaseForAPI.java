package com.example.application.backend.control.others;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.DisplayRestaurantsList;
import com.example.application.backend.control.filtering.FilteringCriteria;
import com.example.application.backend.control.filtering.FilteringStoreFactory;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Profile;
import com.example.application.backend.entity.Restaurant;
import com.example.application.backend.enums.PreferredModeOfTransport;
import com.example.application.backend.enums.TypesOfDietaryRequirements;
import com.example.application.controller.Controller;
import com.example.application.model.Model;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class FirebaseForAPI {
    public static void getAPIData(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) { //pass other api params here
        final ArrayList<Account> arrayList = new ArrayList<>();
        Profile[] profile = new Profile[1];
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        final PreferredModeOfTransport[] travelMethod = new PreferredModeOfTransport[1];
        final float[] travelTime = new float[1];
        final LatLng[] location = new LatLng[1];
        final Date[] date = new Date[1];
        String time = new String();
        final int[] date_as_int = new int[1];

        mDatabase.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Iterable<DataSnapshot> children = snapshot.getChildren();

                        for (DataSnapshot child : children) {
                            Account account = child.getValue(Account.class);
                            arrayList.add(account);
                        }
                        System.out.println("Size of arrayList:" + arrayList.size());
                        //do api stuff here
                        profile[0] = arrayList.get(0).getProfile();
                        travelMethod[0] = profile[0].getPreferredModeOfTransport();
                        travelTime[0] = profile[0].getPreferredMaximumTravelTime();
                        mDatabase.child(userID).child("Account").child("Current Time")
                                .addListenerForSingleValueEvent(new ValueEventListener(){

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String data = snapshot.getValue(String.class);
                                        SimpleDateFormat parser = new SimpleDateFormat("EEEE dd-mm-yyyy AAA BBBB hh:mm:ss CCC");
                                        try {
                                            date[0] = parser.parse(data);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(date[0]);
                                        date_as_int[0] = cal.get(Calendar.DAY_OF_WEEK);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        mDatabase.child(userID).child("Account").child("Current Location")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String data = snapshot.getValue(String.class);
                                        double latitude = Double.parseDouble(data.substring(data.indexOf("(") + 1, data.indexOf(",")));
                                        double longitude = Double.parseDouble(data.substring(data.indexOf(",") + 1, data.indexOf(")")));
                                        location[0] = new LatLng(latitude, longitude);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
