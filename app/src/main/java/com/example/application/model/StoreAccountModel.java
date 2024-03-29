package com.example.application.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.control.others.FormatChecker;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Restaurant;
import com.example.application.view.InputPreferencesUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * StoreAccountModel is the model child class that inherits from the abstract Model parent class
 * It inherits and implements the service() method with the logic to create and store the account object with user inputted details in the database.
 * To perform its service logic, SortingListModel needs an attribute list containing the user's name, email, password, and references to the fields for user to enter their email and password in the UI activity class (View class)
 * The sortingList is the attribute list needed by the sortingListModel to perform its service logic of sorting the recommended list
 * The sortingList will contain the sorting criteria as well as the restaurant list to be sorted
 * @author Isaac
 * @version 1.0
 * @since 2022-11-11
 */
public class StoreAccountModel extends Model{

    /**
     * This is the overridden constructor for StoreAccountModel
     * @param mAuth The firebase authentication reference.
     * @param mDatabase A reference to the Firebase database.
     * @param context The InputPreferencesUI activity page.
     */
    public StoreAccountModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    // store data/ interact with firebase
    @Override
    public void service() {
        // FORMAT: attributeList = [name, email, textInputEmail, password, textInputPassword]

        //Getting necessary variables
        String name = ((TextInputEditText) super.attributeList.get(0)).getText().toString().trim();
        String email = ((TextInputEditText) super.attributeList.get(1)).getText().toString().trim();
        TextInputLayout textInputEmail = (TextInputLayout) super.attributeList.get(2);
        String password = ((TextInputEditText) super.attributeList.get(3)).getText().toString().trim();
        TextInputLayout textInputPassword = (TextInputLayout) super.attributeList.get(4);

        final String TAG = "Create User";



        if (FormatChecker.isValidEmail(email, textInputEmail) && FormatChecker.isValidPassword(password, textInputPassword)) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();
                                Restaurant restaurant = new Restaurant(0,0,null, 0,9999999.0,null,null,0,0,null,false,false,null);
                                restaurantList.add(restaurant);
                                Account account = new Account(name, email, null, restaurantList);

                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

                                mDatabase.child(userID).child("Account").setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if (task1.isSuccessful()){
                                            // send email verification link
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Intent i = new Intent(context, InputPreferencesUI.class);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        context.startActivity(i);
                                                    }
                                                    else{
                                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                        else{
                                            Toast.makeText(context, "Failed to create an account", Toast.LENGTH_SHORT).show();
                                            //delete user here if he fails
                                            mAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Log.d(TAG, "User account deleted");
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                            } else { //when user account was already created before
                                Toast.makeText(context, "User already exists", Toast.LENGTH_LONG).show();
                            }
                        }

                    });
        }


    }
}
