package com.example.application.backend.entity;

import com.example.application.backend.enums.TypesOfDietaryRequirements;

public class Restaurant {
    private int crowdLevel;
    private float ratings;
    private Double travellingTime;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String image;
    //ArrayList<LocalTime> openCloseTimings;
    private boolean openNow;

    public Restaurant() {
    }

    //JSON control class will extract relevant attribute information and convert them into their correct form.
    public Restaurant(int cl, float rtngs, double tt, String name, String adrs, double lati, double longi, String img, boolean openNow) {
        this.crowdLevel = cl;
        this.ratings = rtngs;
        this.travellingTime = tt;
        this.name = name;
        this.address = adrs;
        this.latitude = lati;
        this.longitude = longi;
        this.image = img;
        this.openNow = openNow;
    }


    // CrowdLevel
    public int getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(int crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    //Ratings
    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    //TravelTime
    public Double getTravellingTime() {
        return travellingTime;
    }

    public void setTravellingTime(Double travelTime) {
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


    //Restaurant Image (string format)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isOpenNow() { return openNow;}
    public void setOpenNow(boolean openNow) {this.openNow = openNow;}

}