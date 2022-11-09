package com.example.application.backend.entity;

import java.util.ArrayList;

public class Account {

        private String name;
        private String email;
        private Profile profile;
        private ArrayList<Restaurant> fullRestaurantList;
        private ArrayList<Restaurant> recommendedList;
        private String chosenLocation;
        private String currentLocation;
        private boolean useCurrentLocation;
        private boolean useCurrentTime;
        private String currentTime;
        private String chosenTime;

        public Account(){}
        public Account(String name, String email, Profile profile) {
            this.name = name;
            this.email = email;
            this.profile = profile;
            this.fullRestaurantList = this.recommendedList = null;
        }

        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {return email;}
        public void setEmail(String email) {this.email=email;}

        public Profile getProfile(){return this.profile;}
        public void setProfile(Profile profile){this.profile = profile;}

        public ArrayList<Restaurant> getFullRestaurantList() {return this.fullRestaurantList;}
        public void setFullRestaurantList(ArrayList<Restaurant> fullRestaurantList) {
            this.fullRestaurantList = fullRestaurantList;
        }
        public ArrayList<Restaurant> getRecommendedList(){return this.recommendedList;}
        public void setRecommendedList(ArrayList<Restaurant> recommendedList) {
            this.recommendedList = recommendedList;
        }
        public boolean getuseCurrentTime(){
            return this.useCurrentTime;
        }
        public boolean getuseCurrentLocation(){
            return this.useCurrentLocation;
        }
        public String getChosenLocation(){
            return this.chosenLocation;
        }
        public String getCurrentLocation(){
            return this.currentLocation;
        }
        public void setChosenLocation(String chosenLocation){
            this.chosenLocation = chosenLocation ;
        }
        public void setCurrentLocation(String currentLocation){
            this.currentLocation = currentLocation;
        }
        public void setUseCurrentLocation(boolean useCurrentLocation){
            this.useCurrentLocation = useCurrentLocation;
        }
        public void setUseCurrentTime(boolean useCurrentTime){
            this.useCurrentTime = useCurrentTime;
        }
        public void setCurrentTime(String currentTime){
            this.currentTime = currentTime;
        }
        public String getCurrentTime(){
            return this.currentTime;
        }
        public void setChosenTime(String chosenTime){
            this.chosenTime = chosenTime;
        }
        public String getChosenTime(){
            return this.chosenTime;
        }
}