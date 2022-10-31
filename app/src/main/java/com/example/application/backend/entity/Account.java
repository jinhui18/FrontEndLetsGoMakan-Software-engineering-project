package com.example.application.backend.entity;

import java.util.ArrayList;

public class Account {

        /**
         * The name of the user.
         */
        private String name;

        /**
         * The profile of the user
         */
        private Profile profile;
        private ArrayList<Restaurant> fullRestaurantList;
        private ArrayList<Restaurant> recommendedList;

        public Account(String name,  Profile profile) {
            this.name = name;
            this.profile = profile;
            this.fullRestaurantList = this.recommendedList = null;
        }

        /**
         * name setter
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * name getter
         * @return name
         */

        public String getName() {
            return this.name;
        }
}
