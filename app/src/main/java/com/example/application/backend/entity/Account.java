package com.example.application.backend.entity;

import java.util.ArrayList;

public class Account {

        /**
         * The name of the user.
         */
        private String name;
        private String email;

        /**
         * The profile of the user
         */
        private Profile profile;
        private ArrayList<Restaurant> fullRestaurantList;
        private ArrayList<Restaurant> recommendedList;
        private Restaurant testRest;

        public Account(String name, String email, Profile profile, Restaurant rest) {
            this.name = name;
            this.email = "email test";
            this.profile = profile;
            //this.fullRestaurantList = this.recommendedList = null;
            this.testRest = rest;
        }
        /**
         * name getter
         * @return name
         */

        public String getName() {
            return this.name;
        }

        /**
         * name setter
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {return email;}
        public void setEmail(String email) {this.email=email;}

    public ArrayList<Restaurant> getFullRestaurantList() {return this.fullRestaurantList;}
    public void setFullRestaurantList(ArrayList<Restaurant> fullRestaurantList) {
        this.fullRestaurantList = fullRestaurantList;
    }

    public ArrayList<Restaurant> getRecommendedList(){return this.recommendedList;}
    public void setRecommendedList(ArrayList<Restaurant> recommendedList) {
        this.recommendedList = recommendedList;
    }

    public Restaurant getRestaurant() {return this.testRest;}
    public void setTestRest(Restaurant res){this.testRest = res;}
}