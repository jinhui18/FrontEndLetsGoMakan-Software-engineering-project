package com.example.application.controller;

import android.content.Context;

import com.example.application.model.Model;

public abstract class Controller {
   protected Model model;
   protected Context context;

    public Controller(Model model, Context context){
        this.model = model; this.context = context;
    }

    public abstract void handleEvent();
}
