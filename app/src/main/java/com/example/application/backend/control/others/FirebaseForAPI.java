package com.example.application.backend.control.others;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.DisplayRestaurantsList;
import com.example.application.ShowMap;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;


public class FirebaseForAPI {
    static PreferredModeOfTransport travelMethod;
    static float travelTime;
    static LatLng location;
    static Date date = new Date();
    String time = new String();
    static int date_as_int;

    public static void getAPIData(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        final ArrayList<Account> arrayList = new ArrayList<>();
        Profile[] profile = new Profile[1];
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();

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
                        travelMethod = profile[0].getPreferredModeOfTransport();
                        travelTime = profile[0].getPreferredMaximumTravelTime();
                        if (arrayList.get(0).getuseCurrentTime() == true) {
                            String timedata = arrayList.get(0).getCurrentTime();
                            SimpleDateFormat parser = new SimpleDateFormat("EEEE dd-mm-yyyy AAA BBBB hh:mm:ss CCC");
                            try {
                                date = parser.parse(timedata);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            date_as_int = cal.get(Calendar.DAY_OF_WEEK);
                        } else {
                            String timedata = arrayList.get(0).getChosenTime();
                            SimpleDateFormat parser = new SimpleDateFormat("EEEE dd-mm-yyyy AAA BBBB hh:mm:ss CCC");
                            try {
                                date = parser.parse(timedata);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            date_as_int = cal.get(Calendar.DAY_OF_WEEK);
                        }
                        if (arrayList.get(0).getuseCurrentLocation() == true) {
                            String locdata = arrayList.get(0).getCurrentLocation();
                            double latitude = Double.parseDouble(locdata.substring(locdata.indexOf("(") + 1, locdata.indexOf(",")));
                            double longitude = Double.parseDouble(locdata.substring(locdata.indexOf(",") + 1, locdata.indexOf(")")));
                            location = new LatLng(latitude, longitude);
                        } else {
                            String locdata = arrayList.get(0).getChosenLocation();
                            double latitude = Double.parseDouble(locdata.substring(locdata.indexOf("(") + 1, locdata.indexOf(",")));
                            double longitude = Double.parseDouble(locdata.substring(locdata.indexOf(",") + 1, locdata.indexOf(")")));
                            location = new LatLng(latitude, longitude);
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}