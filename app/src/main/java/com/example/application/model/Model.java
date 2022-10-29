package com.example.application.model;

import android.content.Context;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Observable;

public abstract class Model extends Observable{
    protected FirebaseAuth mAuth;
    protected DatabaseReference mDatabase;
    protected Context context;
    public Model(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context){
        this.mAuth=mAuth; this.mDatabase=mDatabase; this.context=context;
    }
    public abstract void service();
    public abstract void addAttribute(Object object); //pls make email and password in one single LoginInformation Object
}
