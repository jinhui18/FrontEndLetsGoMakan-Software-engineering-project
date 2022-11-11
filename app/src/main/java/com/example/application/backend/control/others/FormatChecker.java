package com.example.application.backend.control.others;

import android.util.Patterns;

import com.google.android.material.textfield.TextInputLayout;

public class FormatChecker {
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

    public static boolean isValidPassword(String password, TextInputLayout textInputPassword)
    {
        boolean isValid = true;
        if ( password.length() < 8)
        {
            textInputPassword.setError("Invalid password");
            textInputPassword.requestFocus();
            isValid = false;
            return isValid;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars ))
        {
            textInputPassword.setError("Invalid password");
            textInputPassword.requestFocus();
            isValid = false;
            return isValid;
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars ))
        {
            textInputPassword.setError("Invalid password");
            textInputPassword.requestFocus();
            isValid = false;
            return isValid;
        }
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers ))
        {
            textInputPassword.setError("Invalid password");
            textInputPassword.requestFocus();
            isValid = false;
            return isValid;
        }
        String specialChars = "(.*[@,#,$,%,!,&,*].*$)";
        if (!password.matches(specialChars ))
        {
            textInputPassword.setError("Invalid password");
            textInputPassword.requestFocus();
            isValid = false;
            return isValid;
        }
        return isValid;
    }
}
