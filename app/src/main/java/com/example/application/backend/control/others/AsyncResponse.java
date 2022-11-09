package com.example.application.backend.control.others;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public interface AsyncResponse {

    ////Place Details
    void processFinishPD(ArrayList<JSONObject> placeDetails);

    //Popular Times
    void processFinishPT(ArrayList<ArrayList<JSONArray>> popTimes);

    void processFinishOneMap(ArrayList<Double> result);


}
