package com.example.application.backend.control.others;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


/**
 * This class is to provide an interface to get the results from all the APIs in the FirebaseForAPI class.
 * @author Pratham
 * @version 1.0
 * @since 2022-11-11
 */
public interface AsyncResponse {

    ////Place Details
    void processFinishPD(ArrayList<JSONObject> placeDetails);

    //Popular Times
    void processFinishPT(ArrayList<ArrayList<JSONArray>> popTimes);

    void processFinishOneMap(ArrayList<Double> result);


}
