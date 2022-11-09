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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InputPreferencesModel extends Model{

    public InputPreferencesModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }
    @Override
    public void service(){
        //Get current profile
        Profile currentProfile = new Profile();
        String userID = mAuth.getCurrentUser().getUid(); //changed FirebaseAuth.getInstance() to mAuth

        //Getting Account object
        final ArrayList<Account> arrayList = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance("https://application-5237c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
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
                        userAccount.setProfile(currentProfile); //addCurrentProfile to account object

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