package com.example.application.backend.entity;

import com.example.application.backend.enums.TypesOfDietaryRequirements;

import java.time.LocalTime;
import java.util.ArrayList;

public class Restaurant {
    private boolean bookmark;
    private float crowdLevel;
    private int ratings;
    private float travellingTime;
    private String address;
    private String description;
    private String image;
    private String availableDietaryRequirements;
    private String availableCuisine;
    ArrayList<LocalTime> openCloseTimings;

    public Restaurant() {}
    //JSON control class will extract relevant attribute information and convert them into their correct form.
    public Restaurant(boolean bm, float cl, int rtngs, float tt, String adrs, String des, String img, ArrayList<LocalTime> timings, String cuisine, TypesOfDietaryRequirements dreqs) {
        this.bookmark = bm;
        this.crowdLevel = cl;
        this.ratings = rtngs;
        this.travellingTime = tt;
        this.address = adrs;
        this.description = des;
        this.image = img;
        this.openCloseTimings = timings;
        this.availableDietaryRequirements = dreqs.toString();
    }

    // Bookmark
    public boolean isBookmark() {
        return bookmark;
    }

    public String getAvailableCuisine(){
        return this.availableCuisine;
    }

    public String getAvailableDietaryRequirements(){
        return this.availableDietaryRequirements;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }

    // CrowdLevel
    public float getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(float crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    //Ratings
    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    //TravelTime
    public float getTravelingTime() {
        return travellingTime;
    }

    public void setTravelingTime(float travelTime) {
        this.travellingTime = travelTime;
    }

    //Address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //Restaurant Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //Restaurant Image (string format)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //Opening and Closing time in array
    public ArrayList<LocalTime> getOpenCloseTimings() {
        return openCloseTimings;
    }

    public void setOpenCloseTimings(ArrayList<LocalTime> openCloseTimings) {
        this.openCloseTimings = openCloseTimings;
    }
}