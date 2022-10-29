package com.example.application.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.Profile;
import com.example.application.view.ChangePreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class ChangePreferencesModel extends Model {
    private Profile newProfile;

    public ChangePreferencesModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    @Override
    public void service() {
        //Hash Map to store string data
        Map<String, Object> map = new HashMap<>();
        map.put("Profile", newProfile.getDietaryRequirements().toString());
        //Get UserID
        String userID = mAuth.getCurrentUser().getUid();
        //Update database (This means update "Profile" key in "Account" child)
        mDatabase.child("Account").child(userID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "Preferences updated!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addAttribute(Object profile){
        this.newProfile = (Profile) profile;
    }
}

//NOTE: There is no observer pattern for this
