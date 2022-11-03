package com.example.application.controller;

import android.content.Context;

import com.example.application.model.Model;

import java.util.ArrayList;

public class Controller {
   protected Model model;
   protected ArrayList<Object> list;

    public Controller(Model model, ArrayList<Object> list){
        this.model = model; this.list = list;
    }

    public void handleEvent(){
        model.addAttributeList(list);
        model.service();
    }
}
