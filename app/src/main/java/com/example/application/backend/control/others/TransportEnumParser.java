package com.example.application.backend.control.others;

import com.example.application.backend.enums.PreferredModeOfTransport;

public class TransportEnumParser {
    public int convertToSpeed(PreferredModeOfTransport obj){
        if (obj == PreferredModeOfTransport.PUBLIC_TRANSPORT) return 25;
        else if (obj == PreferredModeOfTransport.CAR) return 40;
        else return 5;
    }
    public String convertToLower(PreferredModeOfTransport obj){
        if(obj == PreferredModeOfTransport.PUBLIC_TRANSPORT) return "pt";
        else if(obj == PreferredModeOfTransport.CAR) return "car";
        else return "walking";
    }
}
