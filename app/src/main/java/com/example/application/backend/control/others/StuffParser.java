package com.example.application.backend.control.others;

import com.example.application.backend.enums.PreferredModeOfTransport;

public class StuffParser {
    public int convertToSpeed(PreferredModeOfTransport obj){
        if (obj == PreferredModeOfTransport.PUBLIC_TRANSPORT) return 25;
        else if (obj == PreferredModeOfTransport.CAR) return 40;
        else return 5;
    }
    public String convertToLower(PreferredModeOfTransport obj){
        if(obj == PreferredModeOfTransport.PUBLIC_TRANSPORT) return "pt";
        else if(obj == PreferredModeOfTransport.CAR) return "drive";
        else return "walk";
    }
    public int getCrowdLevelFromPT(String pt){
        if (pt.equals("Usually not too busy") || pt.equals("Usually not busy")) return 1;
        else if (pt.equals("Usually a little busy")) return 2;
        else if (pt.equals("Usually as busy as it gets")) return 3;
        else return 0;
    }
}
