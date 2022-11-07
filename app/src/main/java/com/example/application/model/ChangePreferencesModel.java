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

public class ChangePreferencesModel extends Model {

    public ChangePreferencesModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    @Override
    public void service() {
        //Get newly changed profile
        Profile changedProfile = (Profile) super.attributeList.get(0);

        //Get UserID
        String userID = mAuth.getCurrentUser().getUid();

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
                        userAccount.setProfile(changedProfile); //add changedProfile to account object

                        //add entire account object
                        mDatabase.child(userID).child("Account").setValue(userAccount).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(context, "Profile has been changed", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(context, "Failed to change profile", Toast.LENGTH_SHORT).show();
                            }
                        });

                        return;
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

//NOTE: There is no observer pattern for this
    /*
Map<String, Object> map = new HashMap<>();
        map.put("Profile", changedProfile);
                //Update database (This means update "Profile" key in "Account" child)
                mDatabase.child(userID).child("Account").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
@Override
public void onComplete(@NonNull Task<Void> task) {
        Toast.makeText(context, "Preferences updated!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(context, LoginUI.class); //Change to HomePage class
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        }
        }).addOnFailureListener(new OnFailureListener() {
@Override
public void onFailure(@NonNull Exception e) {
        Toast.makeText(context, "Failed to change preferences", Toast.LENGTH_SHORT).show();
        }
        });

     */