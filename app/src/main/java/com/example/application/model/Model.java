package com.example.application.model;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

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
}
