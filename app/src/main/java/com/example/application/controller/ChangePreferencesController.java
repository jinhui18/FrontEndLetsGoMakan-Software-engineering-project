package com.example.application.controller;

import com.example.application.Profile;
import com.example.application.model.ChangePreferencesModel;
import com.example.application.model.Model;
import java.util.Map;
import android.content.Context;

public class ChangePreferencesController extends Controller{
    private Profile newProfile;

    public ChangePreferencesController(Model model, Profile profile){
        super(model); this.newProfile = profile;
    }

    @Override
    public void handleEvent() {
        model.addAttribute(newProfile); //For this reason, email and password needs to be in one LoginInformation object
        model.service();
    }
}
