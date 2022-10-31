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

        public Account(String name, String email, Profile profile) {
            this.name = name;
            this.email = email;
            this.profile = profile;
            this.fullRestaurantList = this.recommendedList = null;
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
}