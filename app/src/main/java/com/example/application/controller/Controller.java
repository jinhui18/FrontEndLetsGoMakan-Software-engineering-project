package com.example.application.controller;

import android.content.Context;

import com.example.application.model.Model;

public abstract class Controller {
   protected Model model;

    public Controller(Model model){
        this.model = model;
    }

    public abstract void handleEvent();
}
