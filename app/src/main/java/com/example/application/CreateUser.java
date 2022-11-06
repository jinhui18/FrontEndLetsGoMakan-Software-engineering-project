package com.example.application;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.control.others.FormatChecker;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Restaurant;
import com.example.application.backend.enums.TypesOfDietaryRequirements;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateUser extends CreateNewAccount {
    private static final String TAG = "CreateUser";
    private String name;
    private String email;
    private String password;
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;


    public CreateUser(String name, String email, String password,Context context){
        this.name = name;
        this.email = email;
        this.password = password;
        this.context = context;

    }
    /**
     * name setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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


    public void handleEvent(String name, String email, String password, TextInputLayout textInputEmail, TextInputLayout textInputPassword) {


        if (FormatChecker.isValidEmail(email, textInputEmail) && FormatChecker.isValidPassword(password, textInputPassword)) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Account account = new Account(name, email, null);
                            //Testing
                                Restaurant r1 = new Restaurant(true, (float) 1, 5, (float) 5, "a", "Punggol", "Sedapz", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Green_Dot_logo.svg/1200px-Green_Dot_logo.svg.png", 12, TypesOfDietaryRequirements.NONE);
                                Restaurant r2 = new Restaurant(true, (float) 2, 4, (float) 4, "b", "Admiralty", "Sedapz", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Green_Dot_logo.svg/1200px-Green_Dot_logo.svg.png", 12, TypesOfDietaryRequirements.HALAL);
                                Restaurant r3 = new Restaurant(true, (float) 3, 3, (float) 3, "c", "Bishan", "Sedapz", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Green_Dot_logo.svg/1200px-Green_Dot_logo.svg.png", 12, TypesOfDietaryRequirements.VEGETARIAN);
                                Restaurant r4 = new Restaurant(true, (float) 4, 2, (float) 2, "d", "Woodlands", "Sedapz", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Green_Dot_logo.svg/1200px-Green_Dot_logo.svg.png", 12, TypesOfDietaryRequirements.BOTH);

                                ArrayList<Restaurant> restaurantArrayList = new ArrayList<>();
                                restaurantArrayList.add(r1);
                                restaurantArrayList.add(r2);
                                restaurantArrayList.add(r3);
                                restaurantArrayList.add(r4);

                                account.setFullRestaurantList(restaurantArrayList);
                                account.setRecommendedList(restaurantArrayList);
                            //Testing ends

                                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                mDataBase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
                                mDataBase.child(userID).child("Account").setValue(account).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if (task1.isSuccessful()){

                                            // send email verification link
                                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Intent i = new Intent(context, InputPreferences.class);
                                                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        context.startActivity(i);
                                                    }
                                                    else{
                                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            //Toast.makeText(CreateNewAccount.this, "Account added successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(context, "Failed to create an account 1", Toast.LENGTH_SHORT).show();
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

                                //startActivity(new Intent(CreateNewAccount.this, MainActivity.class));
                            } else { //when user account was already created before
                                Toast.makeText(context, "Failed to create an account 2", Toast.LENGTH_LONG).show();
                                //Toast.makeText(CreateNewAccount.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        }
    }
}
