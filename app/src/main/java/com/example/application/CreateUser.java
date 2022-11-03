package com.example.application;

import android.content.Context;
import android.content.Intent;
import android.util.Patterns;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.control.others.FormatChecker;
import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Profile;
import com.example.application.backend.enums.PreferredMaximumTravelTime;
import com.example.application.backend.enums.PreferredModeOfTransport;
import com.example.application.backend.enums.TypesOfDietaryRequirements;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

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
                                //LoginInformation loginInformation = new LoginInformation(email, password);
                                Profile profile = new Profile(TypesOfDietaryRequirements.NONE, PreferredMaximumTravelTime.HALF_HOUR, PreferredModeOfTransport.CAR);
                                Account account = new Account(name, email, profile);
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
