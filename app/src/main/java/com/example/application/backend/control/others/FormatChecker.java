package com.example.application.backend.control.others;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

/**
 * FormatChecker class is solely responsible for checking the format of emails and passwords
 * It contains two static methods to do so.
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public class FormatChecker {
    /**
     * This static method checks whether the format of the email is valid according to our functional requirements
     * The email must have at least one character and have a valid domain
     * @param email refers to the email to be format checked
     * @param textInputEmail refers to the UI field widget where users enter their email address on the UI activity page
     * @return a boolean value indicating whether the format of the email is valid or not
     */
    public static boolean isValidEmail(String email, TextInputLayout textInputEmail) {

        if (email.isEmpty()) {
            textInputEmail.setError("Field can't be empty");
            textInputEmail.requestFocus();
            return false;
        }
        // use function to check email address pattern
        if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputEmail.setError("Enter valid Email address");
            textInputEmail.requestFocus();
            return false;
        }
        else {
            textInputEmail.setError(null);
            return true;
        }
    }

    /**
     * This static method checks whether the format of the password is valid according to our functional requirements
     * The password must contain at least 8 characters, with at least 1 capital letter, 1 non-capital letter, 1 number and 1 special character
     * @param password refers to the password that needs to be format checked
     * @param textInputPassword refers to the UI field widget where users enter their email address on the UI activity page
     * @return a boolean value indicating whether the format of the password is valid or not
     */
    public static boolean isValidPassword(String password, TextInputLayout textInputPassword)
    {
        boolean isValid = true;
        if ( password.length() < 8)
        {
            textInputPassword.setError("Password must contain at least 8 characters,one letter, one upper and lowercase character and one special character.\n\nInvalid password");
            textInputPassword.requestFocus();
            isValid = false;
            return isValid;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars ))
        {
            textInputPassword.setError("Password must contain at least 8 characters,one letter, one upper and lowercase character and one special character.\n\nInvalid password");
            textInputPassword.requestFocus();
            isValid = false;
            return isValid;
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars ))
        {
            textInputPassword.setError("Password must contain at least 8 characters,one letter, one upper and lowercase character and one special character.\n\nInvalid password");
            textInputPassword.requestFocus();
            isValid = false;
            return isValid;
        }
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers ))
        {
            textInputPassword.setError("Password must contain at least 8 characters,one letter, one upper and lowercase character and one special character.\n\nInvalid password");
            textInputPassword.requestFocus();
            isValid = false;
            return isValid;
        }
        String specialChars = "(.*[@,#,$,%,!,&,*].*$)";
        if (!password.matches(specialChars ))
        {
            textInputPassword.setError("Password must contain at least 8 characters,one letter, one upper and lowercase character and one special character.\n\nInvalid password");
            textInputPassword.requestFocus();
            isValid = false;
            return isValid;
        }
        return isValid;
    }
}
