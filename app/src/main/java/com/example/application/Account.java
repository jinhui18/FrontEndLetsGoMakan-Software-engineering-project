package com.example.application;

public class Account {

        /**
         * The name of the user.
         */
        private String name;

        /**
         * The email address of the user.
         */
        protected String email;

        /**
         * The password to the user's account.
         */
        protected String password;

        private Profile profile;



        public Account () {

        }

        public Account(String name,  String email, String password, Profile profile) {
            this.name = name;
            this.email = email;
            this.password = password;
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


        /**
         * Gets the login email address of the user.
         * @return	this user's login email address
         */
        public String getEmail() {
            return email;
        }

        /**
         * Sets the login email address of the user.
         * @param email	This user's login email address
         */
        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * Gets the password to the user's account.
         * @return	this user's account password
         */
        public String getPassword() {
            return password;
        }

        /**
         * Sets the password to the user's account.
         * @param password	This user's account password
         */
        public void setPassword(String password) {
            this.password = password;
        }



}
