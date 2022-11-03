package com.example.application.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.entity.Account;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Observable;

public abstract class Model extends Observable{
    protected FirebaseAuth mAuth;
    protected DatabaseReference mDatabase;
    protected Context context;
    public Model(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context){
        this.mAuth=mAuth; this.mDatabase=mDatabase; this.context=context;
    }
    public abstract void service();
    public abstract void addAttribute(Object object);
    public Account getAccountObject(){
        final Account[] account = {null};
        String userID = mAuth.getCurrentUser().getUid();
        mDatabase.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> children = snapshot.getChildren();
                for (DataSnapshot child : children) {
                    account[0] = child.getValue(Account.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
            }
        });
        return account[0]; //return the sole account a user has
    }
}
