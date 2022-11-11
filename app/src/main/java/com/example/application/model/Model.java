package com.example.application.model;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Observable;

/**
 * This is the abstract Model class in the MVC architecture that other child model classes will inherit from
 * The model child classes will implement the service() method with their respective service logic
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public abstract class Model extends Observable{
    /**
     * mAuth is the reference to our Firebase Authenticator
     */
    protected FirebaseAuth mAuth;
    /**
     * mDatabase is a reference to our Firebase realtime database
     */
    protected DatabaseReference mDatabase;
    /**
     * context refers to the UI activity page in which the event had occurred (View class)
     */
    protected Context context;
    /**
     * attribute list is the arraylist that refers to the list of parameters needed to perform the service logic in the child classes overridden service() method
     */
    protected ArrayList<Object> attributeList;

    /**
     *
     * @param mAuth is the reference to our Firebase Authenticator
     * @param mDatabase is a reference to our Firebase realtime database
     * @param context refers to the UI activity page in which the event had occurred (View class)
     */
    public Model(FirebaseAuth mAuth, DatabaseReference mDatabase, Context context){
        this.mAuth=mAuth; this.mDatabase=mDatabase; this.context=context; this.attributeList=null;
    }

    /**
     * The abstract service() method that individual model child classes will inherit and implement
     */
    public abstract void service();

    /**
     * get method to add attribute list to the model object
     * @param list refers to the attribute list to be added
     */
    public void addAttributeList(ArrayList<Object> list) {attributeList=list;}
}
