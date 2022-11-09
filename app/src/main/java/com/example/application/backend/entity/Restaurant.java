package com.example.application.backend.entity;

import com.example.application.backend.enums.TypesOfDietaryRequirements;

public class Restaurant {
    private int crowdLevel;
    private float ratings;
    private float travellingTime;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String description;
    private String image;
    //ArrayList<LocalTime> openCloseTimings;
    private boolean openNow;

    public Restaurant() {
    }

    //JSON control class will extract relevant attribute information and convert them into their correct form.
    public Restaurant(int cl, float rtngs, float tt, String name, String adrs, double lati, double longi, String des, String img, boolean openNow) {
        this.crowdLevel = cl;
        this.ratings = rtngs;
        this.travellingTime = tt;
        this.name = name;
        this.address = adrs;
        this.latitude = lati;
        this.longitude = longi;
        this.description = des;
        this.image = img;
        this.openNow = openNow;
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
    public float getTravellingTime() {
        return travellingTime;
    }

    public void setTravellingTime(float travelTime) {
        this.travellingTime = travelTime;
    }

    //Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    //Latitude
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    //Longitude
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

}