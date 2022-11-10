package com.example.application.backend.entity;

import com.example.application.backend.enums.TypesOfDietaryRequirements;

/**
 *This is our Restaurant entity class that stores all related information about Restaurants
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public class Restaurant {
    /**
     * Crowd level refers to how crowded the restaurant is
     */
    private int crowdLevel;
    /**
     * Ratings are the ratings given to the restaurant
     */
    private float ratings;
    /**
     * Travelling time refers to the time a user needs to get to the restaurant with his chosen mode of transportation
     */
    private Double travellingTime;
    /**
     * Name refers to the restaurant name
     */
    private String name;
    /**
     * Address is the restaurant's address
     */
    private String address;
    /**
     * phoneNumber refers to the restaurant's hotline
     */
    private String phoneNumber;
    /**
     * latitude of the restaurant
     */
    private Double latitude;
    /**
     * longitude of the restaurant
     */
    private Double longitude;
    /**
     * image url of the restaurant
     */
    private String image;
    //ArrayList<LocalTime> openCloseTimings;
    /**
     * openNow is the open and close status of the restaurant
     */
    private boolean openNow;
    /**
     * takeOut tells us if the restaurant support takeouts
     */
    private boolean takeOut;


    public Restaurant() {
    }

    //JSON control class will extract relevant attribute information and convert them into their correct form.

    /**
     *
     * @param cl refers
     * @param rtngs
     * @param tt
     * @param name
     * @param adrs
     * @param lati
     * @param longi
     * @param img
     * @param openNow
     */
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

    //phoneNumber
    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
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

    public boolean isTakeOut() {return takeOut;}
    public void setTakeOut(boolean takeOut) {this.takeOut = takeOut;}

}
