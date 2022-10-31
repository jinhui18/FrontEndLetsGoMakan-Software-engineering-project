package com.example.application.backend.entity;

public class Account {

        /**
         * The name of the user.
         */
        private String name;

        /**
         * The profile of the user
         */
        private Profile profile;

        public Account () {
        }

        public Account(String name,  Profile profile) {
            this.name = name;
            this.profile = profile;
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
