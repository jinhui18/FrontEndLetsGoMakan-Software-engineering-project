package com.example.application.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.application.backend.entity.Account;
import com.example.application.backend.entity.Restaurant;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Observable;

public abstract class Model extends Observable{
    protected FirebaseAuth mAuth;
    protected DatabaseReference mDatabase;
    protected Context context;
    protected ArrayList<Object> attributeList;

    public Model(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context){
        this.mAuth=mAuth; this.mDatabase=mDatabase; this.context=context; this.attributeList=null;
    }
    public abstract void service();
    public void addAttributeList(ArrayList<Object> list) {attributeList=list;}
    //Used to retrieve the account object from the database (if user is logged in)
    public Account getAccountObject(){
       ArrayList<Account> accountArray = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null) {
            String userID = user.getUid();
            mDatabase.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    for (DataSnapshot child : children) {
                        accountArray.add(child.getValue(Account.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Failed to retrieve account", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return accountArray.get(0); //return the sole account a user has
    }
}
