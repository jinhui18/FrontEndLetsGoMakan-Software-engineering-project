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
                        if(arrayList.get(0).getuseCurrentTime() == true){
                            String timedata  = arrayList.get(0).getCurrentTime();
                            SimpleDateFormat parser = new SimpleDateFormat("EEEE dd-mm-yyyy AAA BBBB hh:mm:ss CCC");
                            try {
                                date[0] = parser.parse(timedata);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date[0]);
                            date_as_int[0] = cal.get(Calendar.DAY_OF_WEEK);
                        }
                        else{
                            String timedata  = arrayList.get(0).getChosenTime();
                            SimpleDateFormat parser = new SimpleDateFormat("EEEE dd-mm-yyyy AAA BBBB hh:mm:ss CCC");
                            try {
                                date[0] = parser.parse(timedata);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date[0]);
                            date_as_int[0] = cal.get(Calendar.DAY_OF_WEEK);
                        }
                        if(arrayList.get(0).getuseCurrentLocation() == true){
                            String locdata = arrayList.get(0).getCurrentLocation();
                            double latitude = Double.parseDouble(locdata.substring(locdata.indexOf("(") + 1, locdata.indexOf(",")));
                            double longitude = Double.parseDouble(locdata.substring(locdata.indexOf(",") + 1, locdata.indexOf(")")));
                            location[0] = new LatLng(latitude, longitude);
                        }
                        else{
                            String locdata = arrayList.get(0).getChosenLocation();
                            double latitude = Double.parseDouble(locdata.substring(locdata.indexOf("(") + 1, locdata.indexOf(",")));
                            double longitude = Double.parseDouble(locdata.substring(locdata.indexOf(",") + 1, locdata.indexOf(")")));
                            location[0] = new LatLng(latitude, longitude);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
