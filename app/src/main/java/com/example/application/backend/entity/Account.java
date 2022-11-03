package com.example.application.backend.entity;

import java.util.ArrayList;

public class Account {

        private String name;
        private String email;
        private Profile profile;
        private ArrayList<Restaurant> fullRestaurantList;
        private ArrayList<Restaurant> recommendedList;


        public Account(String name, String email, Profile profile, Restaurant rest) {
            this.name = name;
            this.email = "email test";
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

        public ArrayList<Restaurant> getFullRestaurantList() {return this.fullRestaurantList;}
        public void setFullRestaurantList(ArrayList<Restaurant> fullRestaurantList) {
            this.fullRestaurantList = fullRestaurantList;
        }
        public ArrayList<Restaurant> getRecommendedList(){return this.recommendedList;}
        public void setRecommendedList(ArrayList<Restaurant> recommendedList) {
            this.recommendedList = recommendedList;
        }
}