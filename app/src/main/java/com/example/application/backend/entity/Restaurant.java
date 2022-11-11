package com.example.application.backend.entity;

import com.example.application.backend.enums.TypesOfDietaryRequirements;

/**
 *This is our Restaurant entity class that stores all related information about Restaurants
 * @author Isaac
 * @version 1.0
 * @since 2022-11-11
 */
public class Restaurant {
    /**
     * Crowd level refers to how crowded the restaurant is
     */
    private int crowdLevel;
    /**
     * priceLevel refers to how pricey the food is at the restaurant on scale of 1 to 5
     */
    private int priceLevel;
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

    /**
     * website of restaurant
     */
    private String website;

    /**
     * This is the default constructor
     */
    public Restaurant() {
    }

    /**
     *
     * @param cl refers to the crowd level at the restaurant
     * @param pl refers to the price level of the restaurant
     * @param rtngs refers ot the restaurant's ratings
     * @param tt refers to the travelling time to the restaurant from user's location
     * @param name refers to the name of the restaurant
     * @param adrs refers to the address of the restaurant
     * @param lati refers to the latitude of the restaurant
     * @param longi refers to the longitude of the restarant
     * @param img refers to the image url of the restaurant
     * @param openNow refers to the open close status of the restaurant
     * @param takeOut refers to the availability of take outs at the restaurant
     */
    public Restaurant(int cl, int pl, String phoneNumber, float rtngs, double tt, String name, String adrs, double lati, double longi, String img, boolean openNow, boolean takeOut, String website) {
        this.crowdLevel = cl;
        this.priceLevel = pl;
        this.phoneNumber = phoneNumber;
        this.ratings = rtngs;
        this.travellingTime = tt;
        this.name = name;
        this.address = adrs;
        this.latitude = lati;
        this.longitude = longi;
        this.image = img;
        this.openNow = openNow;
        this.takeOut = takeOut;
        this.website = website;
    }


    // CrowdLevel

    /**
     * get method for restaurant crowd level
     * @return crowd level at the restaurant
     */
    public int getCrowdLevel() {
        return crowdLevel;
    }

    /**
     * set method for restaurant crowd level
     * @param crowdLevel is the crowd level at the restaurant
     */
    public void setCrowdLevel(int crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    //priceLevel

    /**
     * get method for the restaruant price level
     * @return the price level of the restaurant
     */
    public int getPriceLevel() { return priceLevel;}

    /**
     * set the price level of the restaurant
     * @param priceLevel is the price level of the restaurant
     */
    public void setPriceLevel(int priceLevel) {this.priceLevel=priceLevel;}

    //Ratings

    /**
     * get method for the restaurant ratings
     * @return the restaurant ratings
     */
    public float getRatings() {
        return ratings;
    }

    /**
     * set method for the restaurant ratings
     * @param ratings is the restaurant ratings
     */
    public void setRatings(float ratings) {
        this.ratings = ratings;
    }

    //TravelTime

    /**
     * get method for the travelling time to the restaurant
     * @return the travelling time to the restaurant
     */
    public Double getTravellingTime() {
        return travellingTime;
    }

    /**
     * set method for the travelling time to the restaurant
     * @param travelTime is the travelling time to the restaurant
     */
    public void setTravellingTime(Double travelTime) {
        this.travellingTime = travelTime;
    }

    //Name

    /**
     * get method for the restaurant name
     * @return restaurant name
     */
    public String getName() {
        return name;
    }

    /**
     * set method for the restaurant name
     * @param name is the restaurant name
     */
    public void setName(String name) {
        this.name = name;
    }

    //Address

    /**
     * get method for the restaurant address
     * @return the restaurant address
     */
    public String getAddress() {
        return address;
    }

    /**
     * set method for the restaurant address
     * @param address is the restaurant address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    //phoneNumber

    /**
     * get method for the restaurant phone number
     * @return restaurant phone number
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }

    /**
     * set method for the restaurant phone number
     * @param phoneNumber restaurant phone number
     */
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }

    //Latitude

    /**
     * get method for the restaurant latitude
     * @return restaurant latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * set method for the restaurant latitude
     * @param latitude restaurant latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    //Longitude

    /**
     * get method for the restaurant longitude
     * @return restaurant longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * set method for the restaurant longitude
     * @param longitude restaurant longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    //Restaurant Image (string format)

    /**
     * get method for the restaurant image
     * @return restaurant image
     */
    public String getImage() {
        return image;
    }

    /**
     * set method for the restaurant image
     * @param image restaurant image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * get method for the restaurant open close status
     * @return restaurant open close status
     */
    public boolean isOpenNow() { return openNow;}

    /**
     * set method for the restaurant open close status
     * @param openNow restaurant open close status
     */
    public void setOpenNow(boolean openNow) {this.openNow = openNow;}

    /**
     * get method for the restaurant take out availability
     * @return restaurant take out availability
     */
    public boolean isTakeOut() {return takeOut;}

    /**
     * set method for the restaurant take out availability
     * @param takeOut restaurant take out availability
     */
    public void setTakeOut(boolean takeOut) {this.takeOut = takeOut;}

    /**
     * get method for the restaurant website url
     * @return restaurant website url
     */
    public String getWebsite() {return website;}

    /**
     * set method for the restaurant website url
     * @param website restaurant website url
     */
    public void setWebsite(String website){this.website = website;}

}
