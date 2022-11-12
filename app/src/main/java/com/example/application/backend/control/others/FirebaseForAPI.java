package com.example.application.backend.control.others;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Profile;
import com.example.application.backend.entity.Restaurant;
import com.example.application.backend.enums.PreferredModeOfTransport;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;


public class FirebaseForAPI implements AsyncResponse{

    ArrayList<JSONObject> restaurantDetails = new ArrayList<JSONObject>();
    ArrayList<Double> travelTimeList = new ArrayList<Double>();
    ArrayList<ArrayList<JSONArray>> popTimeList = new ArrayList<ArrayList<JSONArray>>();

    ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();

    StuffParser stuffParser = new StuffParser();

    private String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36";
    String dateData;

    PreferredModeOfTransport travelMethod;
    float travelTime;
    LatLng location;
    Date date = new Date();
    String time = new String();
    int date_as_int;
    String userID;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();



    public void getAPIData(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        final ArrayList<Account> arrayList = new ArrayList<>();
        Profile[] profile = new Profile[1];
        userID = mAuth.getCurrentUser().getUid();
        System.out.println(userID);
        System.out.println("hereeee");

        mDatabase.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        System.out.println("hiiiii");

                        Iterable<DataSnapshot> children = snapshot.getChildren();

                        for (DataSnapshot child : children) {
                            System.out.println("byeeeee");

                            Account account = child.getValue(Account.class);
                            System.out.println("chose"+account.getChosenLocation());
                            arrayList.add(account);
                        }
                        System.out.println("Size of arrayList:" + arrayList.size());
                        //do api stuff herez
                        profile[0] = arrayList.get(0).getProfile();
                        travelMethod = profile[0].getPreferredModeOfTransport();
                        travelTime = profile[0].getPreferredMaximumTravelTime();
                        if (arrayList.get(0).getUseCurrentDateTime() == true) {
                            dateData = arrayList.get(0).getCurrentDateTime().substring(5, 15);
                            SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                date = parser.parse(dateData);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            date_as_int = cal.get(Calendar.DAY_OF_WEEK);
                            time = arrayList.get(0).getCurrentDateTime().substring(25, 30);
                        } else {

                            dateData = arrayList.get(0).getChosenDate();
                            SimpleDateFormat parser = new SimpleDateFormat("dd-MM-yyyy");
                            try {
                                date = parser.parse(dateData);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            date_as_int = cal.get(Calendar.DAY_OF_WEEK);
                            time = arrayList.get(0).getChosenTime();
                        }
                        System.out.println("DTEASEADW: " + date);
                        if (arrayList.get(0).getUseCurrentLocation() == true) {
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
                        int radius = (int) (stuffParser.convertToSpeed(travelMethod) * travelTime/60);
                        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                                "location=" + location.latitude + "%2C" + location.longitude +
                                "&radius=" + radius +
                                "&type=restaurant" +
                                "&key=AIzaSyBvQjZ15jD__Htt-F3TGvMp_ZWNw79JZv0";

                        System.out.println(url);
                        new PlaceTask().execute(url);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
        System.out.println("secondss");






/*
        Map<String, Object> map = new HashMap<>();
        map.put("fullRestaurantList", restaurantList);
        mDatabase.child(userID).child("Account").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "List created!", Toast.LENGTH_SHORT).show();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to push list", Toast.LENGTH_SHORT).show();
            }
        });

 */
    }

    //AsyncTask Responses
    @Override
    public void processFinishPD(ArrayList<JSONObject> output) {
        restaurantDetails = output;
    }



    public void processFinishOneMap(ArrayList<Double> result) {
        travelTimeList = result;
    }

    @Override
    public void processFinishPT(ArrayList<ArrayList<JSONArray>> output) {
        popTimeList = output;
        // this is to get the full rest list alr
        System.out.println("sadasdsadsa size of restaurantDetails is " + restaurantDetails.size());
        System.out.println("restaurantDetails is " + restaurantDetails);
        try{
            for (int i = 0; i < restaurantDetails.size(); i++)
            {
                //System.out.println("rest details in index "+ i + " " + restaurantDetails.get(i));
                //System.out.println("size of rest details at index " + i + " is " + restaurantDetails.get(i).names().length());
                //System.out.println("travelTimeList at index " + i + " is " + travelTimeList.get(i));

                // original code  in if bracket is restaurantDetails.get(i).names().length() != 10 || popTimeList.get(i).size() != 7
                try {
                    if (popTimeList.get(i).size() != 7) {
                        System.out.println("Mistake");
                        continue;

                    } else {
                        System.out.println("THIS IS ALL THE ACCEPTED ONES ");
                        System.out.println(restaurantDetails.get(i));
                        Double lat = Double.valueOf(restaurantDetails.get(i).getJSONObject("geometry").getJSONObject("location").getString("lat"));
                        Double lng = Double.valueOf(restaurantDetails.get(i).getJSONObject("geometry").getJSONObject("location").getString("lng"));

                        float ratings = Float.valueOf(restaurantDetails.get(i).getString("rating"));

                        Double travelTime = travelTimeList.get(i);

                        String name = restaurantDetails.get(i).getString("name");

                        String address = restaurantDetails.get(i).getString("formatted_address");

                        String photoRef = restaurantDetails.get(i).getJSONArray("photos").getJSONObject(0).getString("photo_reference");

                        String photo = "https://maps.googleapis.com/maps/api/place/photo" +
                                "?maxwidth=400" +
                                "&photo_reference=" + photoRef +
                                "&key=AIzaSyBvQjZ15jD__Htt-F3TGvMp_ZWNw79JZv0";

                        int currentDay = date_as_int;

                        System.out.println(time);
                        System.out.println("Before and");


                        int currentHour = Integer.valueOf(time.substring(0, 2));

                        int popTimeIndex = currentHour - 6;

                        System.out.println(popTimeIndex);


                        String popTime;
                        try {
                            popTime = ((JSONArray) ((JSONArray) popTimeList.get(i).get(date_as_int - 1))).getJSONArray(1).getJSONArray(popTimeIndex).getString(2);//
                            System.out.println(popTime);
                        } catch (Exception e) {
                            popTime = "No Data";
                        }

                        System.out.println("hellllllllooooooooooooooooo" + popTime);


                        int crowdLevel = stuffParser.getCrowdLevelFromPT(popTime);

                        System.out.println(time);

                        int currentTime = (Integer.valueOf(time.substring(0, 2)) * 100) + Integer.valueOf(time.substring(3, 4));

                        System.out.println(currentTime);

                        boolean openNow;

                        try{
                            if (currentTime < 2200 && restaurantDetails.get(i).getJSONObject("opening_hours").getBoolean("open_now")) {
                                openNow = true;
                            }
                            else{
                                openNow = false;
                            }
                        }catch(Exception e){
                            if (currentTime <2200 && currentTime >1100){
                                openNow = true;
                            }
                            else{
                                openNow = false;
                            }

                        }

                        /*
                        if (currentTime < 2200 && restaurantDetails.get(i).getJSONObject("opening_hours").getBoolean("open_now")) {
                            openNow = true;
                        } else {
                            openNow = false;
                        }

                         */


                        boolean takeout = restaurantDetails.get(i).getBoolean("takeout");

                        String phoneNumber = null;
                        try{
                            phoneNumber = restaurantDetails.get(i).getString("formatted_phone_number");
                        }catch (Exception e){
                            phoneNumber = null;
                        }
                        //String phoneNumber = restaurantDetails.get(i).getString("formatted_phone_number");

                        String website = null;
                        try{
                            website = restaurantDetails.get(i).getString("website");
                        }catch(Exception e){
                            website = null;

                        }
                        //String website = restaurantDetails.get(i).getString("website");

                        int pl = 1;
                        try{
                            pl = Integer.valueOf(restaurantDetails.get(i).getString("price_level"));
                        }
                        catch(Exception e){
                            pl =1;
                        }
                        //int pl = Integer.valueOf(restaurantDetails.get(i).getString("price_level"));

                        Restaurant restaurant = new Restaurant(crowdLevel, pl, phoneNumber, ratings, travelTime, name, address, lat, lng, photo, openNow, takeout, website);

                        System.out.println("Name: " + restaurant.getName());

                        restaurantList.add(restaurant);
                    }
                }
                catch (Exception e){continue;}
            }
            System.out.println("list sizeeeeee" + restaurantList.size());
            Map<String, Object> map = new HashMap<>();
            map.put("fullRestaurantList", restaurantList);
            mDatabase.child(userID).child("Account").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //Toast.makeText(context, "List created!", Toast.LENGTH_SHORT).show();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(context, "Failed to push list", Toast.LENGTH_SHORT).show();
                }
            });
        }
        catch(Exception e){
            System.out.println("here ah");
            e.printStackTrace();}


    }


    public class PlaceTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String data = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

                String line = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                data = stringBuilder.toString();
                System.out.println("heeehehehe "+ data);


                bufferedReader.close();
            } catch (IOException e) {
                System.out.println("sheeshhhhhhhhhhhhh");
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println("SC20000000000000000000000000000000006");
            new GetPlaceIDs().execute(s);
        }
    }

    public class GetPlaceIDs extends AsyncTask<String, String, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            ArrayList<String> placeIDList = new ArrayList<String>();
            try {
                JSONObject nearbySearchResult = new JSONObject(strings[0]);
                JSONArray resultsArray = nearbySearchResult.getJSONArray("results");

                for (int i = 0; i < resultsArray.length(); i++) {
                    placeIDList.add(resultsArray.getJSONObject(i).getString("place_id"));
                }
            } catch (JSONException e) {
                System.out.println("sadsadasdsad");
                e.printStackTrace();
            }

            return placeIDList;
        }


        protected void onPostExecute(ArrayList<String> strings) {
            new GetPlaceDetails().execute(strings);
        }
    }

    public class GetPlaceDetails extends AsyncTask<ArrayList<String>, String, ArrayList<JSONObject>> {

        AsyncResponse delegate = FirebaseForAPI.this;

        @Override
        protected ArrayList<JSONObject> doInBackground(ArrayList<String>... arrayLists) {
            ArrayList<String> placeIDs = arrayLists[0];
            ArrayList<JSONObject> result = new ArrayList<JSONObject>();

            System.out.println("size of places ids is " + placeIDs.size());

            for (int i = 0; i < placeIDs.size(); i++) {
                String url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + placeIDs.get(i) +
                        "&fields=name%2Crating%2Cformatted_address%2Copening_hours%2Cphotos%2Cgeometry" +
                        "%2Cformatted_phone_number%2Cprice_level%2Ctakeout%2Cwebsite" +
                        "&key=AIzaSyBvQjZ15jD__Htt-F3TGvMp_ZWNw79JZv0";

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");

                System.out.println("Google URL: " + url);


                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    ResponseBody response = client.newCall(request).execute().body();
                    JSONObject responseObject = new JSONObject(response.string());
                    JSONObject resultObject = responseObject.getJSONObject("result");
                    result.add(resultObject);
                } catch (IOException e) {
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa");
                    e.printStackTrace();
                } catch (JSONException e) {
                    System.out.println("bbbbbbbbbbbbbbbbbbbbbbb");
                    e.printStackTrace();
                }
            }
            System.out.println("size of result is: " +result.size());
            System.out.println("Result is: " +result);
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> jsonObjects) {
            delegate.processFinishPD(jsonObjects);
            ArrayList<ArrayList<String>> restaurantNAList = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> latLngList = new ArrayList<ArrayList<String>>();
            System.out.println("size of json object is " + jsonObjects.size());
            System.out.println("json object is :" + jsonObjects);
            for (int i = 0; i < jsonObjects.size(); i++) {
                ArrayList<String> restaurantDets = new ArrayList<String>();
                ArrayList<String> latLng = new ArrayList<String>();
                try {
                    restaurantDets.add(jsonObjects.get(i).getString("name"));
                    restaurantDets.add(jsonObjects.get(i).getString("formatted_address"));
                    latLng.add(jsonObjects.get(i).getJSONObject("geometry").getJSONObject("location").getString("lat"));
                    latLng.add(jsonObjects.get(i).getJSONObject("geometry").getJSONObject("location").getString("lng"));

                    restaurantNAList.add(restaurantDets);
                    latLngList.add(latLng);
                } catch (JSONException e) {
                    System.out.print("Insanity");
                    e.printStackTrace();
                }
            }
            System.out.println("restaurantNAList size is " + restaurantNAList.size());
            new GetTotalTime().execute(latLngList);

            new GetPopularTimes().execute(restaurantNAList);
        }
    }


    public class GetPopularTimes extends AsyncTask<ArrayList<ArrayList<String>>, String, ArrayList<ArrayList<JSONArray>>> {
        AsyncResponse delegate = FirebaseForAPI.this;

        @Override
        protected ArrayList<ArrayList<JSONArray>> doInBackground(ArrayList<ArrayList<String>>... stringsL) {
            ArrayList<ArrayList<JSONArray>> result = new ArrayList<ArrayList<JSONArray>>();

            ArrayList<ArrayList<String>> strings = stringsL[0];

            for (int i = 0; i < strings.size(); i++) {
                try{
                    ArrayList<JSONArray> popTimeL = new ArrayList<JSONArray>();

                    ArrayList<String> details = strings.get(i);
                    String name = details.get(0);
                    String formattedAddress = details.get(1);

                    String q = name + " " + formattedAddress;

                    String appender = "tbm=map" + "&hl=sg" + "&tch=1" + "&q=" + URLEncoder.encode(q, "UTF-8");
                    String searchUrl = "https://www.google.de/search?" + appender;

                    String json = downloadWeirdStuff(searchUrl);


                    int jEnd = json.lastIndexOf("}");
                    if (jEnd >= 0)
                        json = json.substring(0, jEnd + 1);

                    //now parse

                    JSONObject jb = null;
                    jb = new JSONObject(json);


                    //now read
                    String jdata = (String) jb.get("d"); //read the data String
                    jdata = jdata.substring(4); //cut it to get the JSONArray again

                    //reparse
                    JSONArray data = new JSONArray(jdata);

                    if (((JSONArray) ((JSONArray) ((JSONArray) data.get(0)).get(1)).get(0)).length() > 11 &&
                            (JSONArray) ((JSONArray) ((JSONArray) ((JSONArray) data.get(0)).get(1)).get(0)) != null) {
                        //get information array
                        JSONArray info = (JSONArray) ((JSONArray) ((JSONArray) ((JSONArray) data.get(0)).get(1)).get(0))
                                .get(14);

                        if (info.get(84) instanceof JSONArray) {
                            JSONArray popTime = (JSONArray) ((JSONArray) info.get(84)).get(0); //get popular times
                            System.out.println(popTime.toString());
                            for (int j = 0; j < popTime.length(); j++) {
                                popTimeL.add((JSONArray) popTime.get(j));
                            }
                        } else {
                            JSONObject jo = new JSONObject();
                            jo.put("Pat", "Guan");
                            JSONArray ja = new JSONArray();
                            ja.put(jo);
                            ja.put(jo);
                            popTimeL.add(ja);
                        }
                        result.add(popTimeL);
                    } else {
                        JSONObject jo = new JSONObject();
                        JSONArray ja = new JSONArray();
                        ja.put(jo);
                        popTimeL.add(ja);
                        result.add(popTimeL);
                    }
                }catch(Exception e)
                {
                    System.out.println("hello world leh");
                    JSONObject jo = new JSONObject();
                    JSONArray ja = new JSONArray();
                    ja.put(jo);
                    ja.put(jo);
                    ArrayList<JSONArray> a = new ArrayList<JSONArray>();
                    result.add(a);
                }
            }
            System.out.println("result is :" + result);
            System.out.println("result size is :" + result.size());
            return result;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<JSONArray>> jsonArray) {
            delegate.processFinishPT(jsonArray);
        }


    }

    public String downloadWeirdStuff(String url) throws IOException {
        URL url1 = new URL(url);
        URLConnection connection = url1.openConnection();
        connection.setRequestProperty("User-Agent", userAgent);
        connection.connect();

        InputStream response = connection.getInputStream();

        String json = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(response, "utf-8"), 8);
        StringBuilder sbuild = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sbuild.append(line);
        }
        response.close();
        json = sbuild.toString();
        return json;
    }

    public class GetTotalTime extends AsyncTask<ArrayList<ArrayList<String>>, String, ArrayList<Double>> {

        AsyncResponse delegate = FirebaseForAPI.this;

        @Override
        protected ArrayList<Double> doInBackground(ArrayList<ArrayList<String>>... arrayLists) {
            ArrayList<ArrayList<String>> latlngs = arrayLists[0];
            ArrayList<Double> result = new ArrayList<Double>();
            for (int i = 0; i < latlngs.size(); i++) {
                ArrayList<String> latlng = latlngs.get(i);
                String lat = latlng.get(0);
                String lng = latlng.get(1);
                boolean pt = false;

                String travel = stuffParser.convertToLower(travelMethod);
                if (travel.compareTo("pt") == 0){
                    pt = true;
                    String YMD = dateData.substring(6,10) + dateData.substring(2,5) + "-" + dateData.substring(0,2);
                    System.out.println(YMD);
                    travel = travel + "&date=" + YMD + "&time=" + time + "&mode=TRANSIT&maxWalkDistance=2000";
                }

                String url = "https://developers.onemap.sg//privateapi/routingsvc/route?start=" + location.latitude + "," + location.longitude +
                        "&end=" + lat + "," + lng +
                        "&routeType=" + travel +
                        "&token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjkzNDgsInVzZXJfaWQiOjkzNDgsImVtYWlsIjoidGFucGF0Z3VhbkB5YWhvby5jb20uc2ciLCJmb3JldmVyIjpmYWxzZSwiaXNzIjoiaHR0cDpcL1wvb20yLmRmZS5vbmVtYXAuc2dcL2FwaVwvdjJcL3VzZXJcL3Nlc3Npb24iLCJpYXQiOjE2Njc5NzYwNDMsImV4cCI6MTY2ODQwODA0MywibmJmIjoxNjY3OTc2MDQzLCJqdGkiOiIzZTk4Y2JhMDM4ZGQwMjY4Y2VjMzliODFmNDgyNmFhNSJ9.CpZ5iOu5LPmJmF43Hj5Vu2O37np2FzjfvP8MMYMpEAY";

                System.out.println(url);

                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");


                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    ResponseBody response = client.newCall(request).execute().body();
                    JSONObject responseObject = new JSONObject(response.string());
                    if(pt){
                        result.add(responseObject.getJSONObject("plan").getJSONArray("itineraries").getJSONObject(0).getDouble("duration") / 60);
                    }
                    else {
                        result.add(responseObject.getJSONObject("route_summary").getDouble("total_time") / 60);
                    }
                } catch (IOException e) {
                    System.out.println("cccccccccccccccccccccccccc");
                    e.printStackTrace();
                } catch (JSONException e) {
                    System.out.println("dddddddddddddddddddddddddddddddd");
                    e.printStackTrace();
                }

            }
            return result;
        }


        @Override
        protected void onPostExecute(ArrayList<Double> aDouble) {
            delegate.processFinishOneMap(aDouble);
        }
    }
}