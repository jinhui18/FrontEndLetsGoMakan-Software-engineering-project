package com.example.application.backend.entity;

import java.util.ArrayList;

/**
 *This is our Account entity class that stores all related information about an account
 * @author Isaac
 * @version 1.0
 * @since 2022-11-11
 */
public class Account {

    /**
    * name refers to the name of the user
    */
    private String name;
    /**
    * email refers to the email of the user
    */
    private String email;
    /**
    * profile refers to the profile object of the user
    */
    private Profile profile;
    /**
    * fullRestaurantlist refers to the full restaurnt list retrieved from our APIs
    */
    private ArrayList<Restaurant> fullRestaurantList;
    /**
    * recommendedList refers to the list of restaurants recommended to the user after being filtered and sorted
    */
    private ArrayList<Restaurant> recommendedList;
    /**
    * chosenLocation refers to the location that is chosen by the user
    */
    private String chosenLocation;
    /**
    * currentLocation is the user's current location
    */
    private String currentLocation;
    /**
    * useCurrentLocation is the boolean value indicating whether the user wants a list of recommendations from his current location
    */
    private boolean useCurrentLocation;
    /**
    * useCurrentDateTime is the boolean value indicating whetehr the user wants a list of recommendations at the time of making the search
    */
    private boolean useCurrentDateTime;
    /**
    * currentDateTime is the current date and time
    */
    private String currentDateTime;
    /**
    * Chosen time refers to the time the user wants the recommendations to be based on
    */
    private String chosenTime;
    /**
    * chosenDate refers to the date the user wants the recommendations to be based on
    */
    private String chosenDate;

    /**
    * This is the default constructor
    */
    public Account(){}

    /**
    * This is the overridden constructor for Account
    * @param name refers to the user's name
    * @param email refers to the user's email
    * @param profile refers to the user's profile
    */
    public Account(String name, String email, Profile profile) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.fullRestaurantList = this.recommendedList = null;
    }

    public Account(String name, String email, Profile profile, ArrayList<Restaurant> fullRestaurantList) {
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.fullRestaurantList = fullRestaurantList;
        this.recommendedList = null;
    }

    /**
     * this is the get method for the user's name
     * @return user's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * this is the set method for the user's name
     * @param name refers to the user's name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * this is the get method for the user's email
     * @return user's email
     */
    public String getEmail() {return email;}
    /**
     * this is the set method for the user's email
     * @param email refers to the user's email to be set
     */
    public void setEmail(String email) {this.email=email;}

    /**
     * this is the get method for the user's profile
     * @return user's profile
     */
    public Profile getProfile(){return this.profile;}
    /**
     * this is the set method for the user's profile
     * @param profile refers to the user's profile to be set
     */
    public void setProfile(Profile profile){this.profile = profile;}
    /**
     * this is the get method for the fullRestaurantList
     * @return fullRestaurantList
     */
    public ArrayList<Restaurant> getFullRestaurantList() {return this.fullRestaurantList;}
    /**
     * this is the set method for the fullRestaurantList
     * @param fullRestaurantList refers to the fullRestaurantList to be set
     */
    public void setFullRestaurantList(ArrayList<Restaurant> fullRestaurantList) {
        this.fullRestaurantList = fullRestaurantList;
    }
    /**
     * this is the get method for the getRecommendedList
     * @return getRecommendedList
     */
    public ArrayList<Restaurant> getRecommendedList(){return this.recommendedList;}
    /**
     * this is the set method for the recommendedList
     * @param recommendedList refers to the recommendedList to be set
     */
    public void setRecommendedList(ArrayList<Restaurant> recommendedList) {
        this.recommendedList = recommendedList;
    }

    /**
     * this is the get method for the boolean variable indicating if current date and time is to be used
     * @return the boolean variable indicating if current date and time is to be used
     */
    public boolean getUseCurrentDateTime(){
        return this.useCurrentDateTime;
    }
    /**
     * this is the set method for the boolean variable indicating if current date and time is to be used
     * @param useCurrentDateTime refers to the boolean variable indicating if current date and time is to be used
     */
    public void setUseCurrentDateTime(boolean useCurrentDateTime){
        this.useCurrentDateTime = useCurrentDateTime;
    }

    /**
     * this is the get method for the current date and time
     * @return the current date and time
     */
    public String getCurrentDateTime(){
        return this.currentDateTime;
    }
    /**
     * this is the set method for the current date and time
     * @param currentDateTime refers to the current date and time
     */
    public void setCurrentDateTime(String currentDateTime){
        this.currentDateTime = currentDateTime;
    }

    /**
     * this is the get method for the boolean variable indicating if current location is to be used
     * @return the boolean variable indicating if current location is to be used
     */
    public boolean getUseCurrentLocation(){
        return this.useCurrentLocation;
    }
    /**
     * this is the set method for the boolean variable indicating if current location is to be used
     * @param useCurrentLocation refers to the boolean variable indicating if current location is to be used
     */
    public void setUseCurrentLocation(boolean useCurrentLocation){
        this.useCurrentLocation = useCurrentLocation;
    }

    /**
     * this is the get method for chosen location
     * @return the chosen location
     */
    public String getChosenLocation(){
        return this.chosenLocation;
    }
    /**
     * this is the set method for the chosen location
     * @param chosenLocation refers to the chosen location
     */
    public void setChosenLocation(String chosenLocation){
        this.chosenLocation = chosenLocation ;
    }

    /**
     * this is the get method for current location
     * @return the current location
     */
    public String getCurrentLocation(){
        return this.currentLocation;
    }
    /**
     * this is the set method for the current location
     * @param currentLocation refers to the current location
     */
    public void setCurrentLocation(String currentLocation){
        this.currentLocation = currentLocation;
    }

    /**
     * this is the get method for the chosen time
     * @return the chosen time
     */
    public String getChosenTime(){
        return this.chosenTime;
    }
    /**
     * this is the set method for the chosen time
     * @param chosenTime refers to the chosen time
     */
    public void setChosenTime(String chosenTime){
        this.chosenTime = chosenTime;
    }

    /**
     * this is the get method for the chosen date
     * @return the chosen date
     */
    public String getChosenDate() {
    return chosenDate;
    }
    /**
     * this is the set method for the chosen date
     * @param chosenDate refers to the chosen date
     */
    public void setChosenDate(String chosenDate) {
    this.chosenDate = chosenDate;
    }
}