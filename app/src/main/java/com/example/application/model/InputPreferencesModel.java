package com.example.application.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * This class extends the abstract Model class and implements its service() method to get the user's preferences at registration.
 * @author Pratham
 * @version 1.0
 * @since 2022-11-10
 */

public class InputPreferencesModel extends Model{

    /**
     * Constructor to initialize database references.
     * @param mAuth The firebase authentication reference.
     * @param mDatabase A reference to the Firebase database.
     * @param context The InputPreferencesUI activity page.
     */

    public InputPreferencesModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }
    @Override
    /**
     * This method implements the service() method from its super class to allow the user to input their preferences at account creation.
     * The Account object with these preferences is then stored on the database.
     */
    public void service(){
        //Get current profile
        Profile changedProfile = (Profile) super.attributeList.get(0);
        String userID = mAuth.getCurrentUser().getUid(); //changed FirebaseAuth.getInstance() to mAuth

        //Getting Account object
        final ArrayList<Account> arrayList = new ArrayList<>();
        mDatabase.child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Iterable<DataSnapshot> children = snapshot.getChildren();

                        for (DataSnapshot child : children) {
                            Account account = child.getValue(Account.class);
                            arrayList.add(account);
                        }
                        System.out.println("Size of arrayList 000 :" + arrayList.size());
                        Account userAccount = arrayList.get(0);
                        userAccount.setProfile(changedProfile); //addCurrentProfile to account object

                        //add entire account object
                        mDatabase.child(userID).child("Account").setValue(userAccount).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(context, "Profile has been created", Toast.LENGTH_SHORT).show();

                                //startActivity(new Intent(InputPreferences.this, CreateNewAccountVerifyEmail.class));
                            } else {
                                Toast.makeText(context, "Failed to create profile", Toast.LENGTH_SHORT).show();
                            }
                        });

                        return;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
    };
}