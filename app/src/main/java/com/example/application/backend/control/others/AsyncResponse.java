package com.example.application.backend.control.others;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(ArrayList<JSONObject> output);
    void processFinishJA(ArrayList<JSONArray> output);
}
