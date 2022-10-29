package com.example.application.controller;

import com.example.application.model.Model;
import java.util.Map;
import android.content.Context;

public class ChangePreferencesController extends Controller{
    Map map;
    public ChangePreferencesController(Model model, Context context, Map map){
        super(model, context); this.map = map;
    }

    @Override
    public void handleEvent() {
        model.service();
    }
}
