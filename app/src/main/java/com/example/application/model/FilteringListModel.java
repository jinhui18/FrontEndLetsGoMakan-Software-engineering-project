package com.example.application.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.control.filtering.FilteringCriteria;
import com.example.application.backend.entity.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class FilteringListModel extends Model{
    private FilteringCriteria filteringCriteria;

    public FilteringListModel(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context) {
        super(mAuth, mDatabase, context);
    }

    @Override
    public void service() {
        //Retrieve account object and sort recommended list
        Account account = this.getAccountObject();
        account.setRecommendedList(filteringCriteria.filter(account.getRecommendedList()));

        //Hash Map to store string data
        Map<String, Object> map = new HashMap<>();
        map.put("Account", account);

        //Get UserID
        String userID = mAuth.getCurrentUser().getUid();

        //Update database (This means update "Account" key in "userID" child)
        mDatabase.child(userID).child("Account").updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "List filtered and updated!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failure to sort list", Toast.LENGTH_SHORT).show();
            }
        });

        //Informing observers to update
        setChanged();
        notifyObservers();
    }

    @Override
    public void addAttribute(Object object) {
        this.filteringCriteria = (FilteringCriteria) filteringCriteria;
    }
}
