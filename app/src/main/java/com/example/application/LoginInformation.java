package com.example.application;

public class LoginInformation {

    /**
     * The email address of the user.
     */
    protected String email;

    /**
     * The password to the user's account.
     */
    protected String password;

    /**
     * Class constructor.
     * Creates the login information of the user.
     * @param email		This user's email address
     * @param password	This user's account password
     */
    public LoginInformation (String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the login email address of the user.
     * @return	this user's login email address
     */
    public String getEmailID() {
        return email;
    }

    /**
     * Sets the login email address of the user.
     * @param email	This user's login email address
     */
    public void setEmailID(String email) {
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
