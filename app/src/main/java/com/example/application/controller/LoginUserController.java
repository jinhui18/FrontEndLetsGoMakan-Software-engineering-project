package com.example.application.controller;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.ShowMap;
import com.example.application.backend.control.others.FormatChecker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginUserController {

    private String email;
    private String password;
    private int counter;

    private Context context;


    public LoginUserController(String email, String password, Context context) {
        this.email = email;
        this.password = password;
        this.context = context;
    }

    /**
     * name setter
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * dateOfBirth setter
     * @param password
     */
    public void setPassword(String password){
        this.password = password;
    }


    /**
     * name getter
     * @return email
     */

    public String getEmail() {
        return this.email;
    }
    /**
     * DOB getter
     * @return password
     */

    public String getPassword() {
        return this.password;
    }


    public void handleEvent(String email, String password, TextInputLayout textInputEmail, TextInputLayout textInputPassword) {
        if (counter <0){
            counter = 5;
            Toast toast = Toast.makeText(context, "Please reset your password", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
        }

        // check email and password to make sure the formatting is correct before checking with database
        if (FormatChecker.isValidEmail(email, textInputEmail) && FormatChecker.isValidPassword(password, textInputPassword) )
        {
            FirebaseAuth mAuth;
            mAuth = FirebaseAuth.getInstance();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (mAuth.getCurrentUser().isEmailVerified() ) {
                            Toast.makeText(context, "User logged in successfully", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, ShowMap.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(context, "Please verify your email address", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //e.getMessage().toString().equals("")
                    // some message cannot be read, need to fix
                    if (e.getMessage().equals("The password is invalid or the user does not have a password.")){
                        Toast.makeText(context, "That's not the right password! Please try again or click on \"Forgot password\". ", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        Toast.makeText(context, "Invalid account. Please try a new email address or create a new account.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        counter--;
    }
}
