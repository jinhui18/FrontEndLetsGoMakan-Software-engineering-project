package com.example.application.controller;

import android.content.Context;

import com.example.application.model.Model;

import java.util.ArrayList;

/**
 * Controller class, part of the MVC architecture, captures user input from the View classes (UI display activity classes) and handles these events
 * It handles these events through its handleEvent()
 * @author Isaac
 * @version 1.0
 * @since 2022-11-10
 */
public class Controller {
    /**
     * refers to the Model object in MVC architecture that contains the service logic to modify the database in response to the event
     */
   protected Model model;
    /**
     * This is the arraylist that stores any parameters needed to perform the service logic of the Model class
     */
   protected ArrayList<Object> list;

    /**
     * This is the overridden controller class constructor
     * @param model is the model object
     * @param list is the arraylist of parameters needed by the Model object in its service() method
     */
    public Controller(Model model, ArrayList<Object> list){
        this.model = model; this.list = list;
    }

    /**
     * This is the method that handles any event from the View classes
     */
    public void handleEvent(){
        model.addAttributeList(list);
        model.service();
    }
}

