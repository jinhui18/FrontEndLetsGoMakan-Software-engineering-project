package com.example.application.backend.control.others;

import com.example.application.backend.enums.PreferredModeOfTransport;

/**
 * @author Pratham
 * This class converts various enum and String values to numeric ones for the app to display.
 * @version 1.0
 * @since 2022-11-10
 */

public class StuffParser {
    /**
     * Converts objects of the enum PreferredModeOfTransport to integers that return the radius for the nearby search in metres.
     * @param obj An object of enum type PreferredModeOfTransport.
     * @return An integer value for the radius.
     */
    public int convertToSpeed(PreferredModeOfTransport obj){
        if (obj == PreferredModeOfTransport.PUBLIC_TRANSPORT) return 25000;
        else if (obj == PreferredModeOfTransport.CAR) return 40000;
        else return 5000;
    }
    /**
     * Converts objects of the enum PreferredModeOfTransport to lowercase Strings.
     * @param obj An object of enum type PreferredModeOfTransport.
     * @return A lowercase String value corresponding to each mode of transport.
     */
    public String convertToLower(PreferredModeOfTransport obj){
        if(obj == PreferredModeOfTransport.PUBLIC_TRANSPORT) return "pt";
        else if(obj == PreferredModeOfTransport.CAR) return "drive";
        else return "walk";
    }
    /**
     * Converts the results of the popular times scraper to an integer value that serves as a measure of how crowded a restaurant is.
     * @param pt A String result from the scraper.
     * @return An integer value for the crowd level.
     */
    public int getCrowdLevelFromPT(String pt){
        if (pt.equals("Usually not too busy") || pt.equals("Usually not busy")) return 1;
        else if (pt.equals("Usually a little busy")) return 2;
        else if (pt.equals("Usually as busy as it gets")) return 3;
        else return 0;
    }
}
