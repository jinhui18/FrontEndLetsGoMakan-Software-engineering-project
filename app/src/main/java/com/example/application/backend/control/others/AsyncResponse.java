package com.example.application.backend.control.others;

import org.json.JSONObject;
import java.util.ArrayList;

public interface AsyncResponse {
    void processFinish(ArrayList<JSONObject> output);
}
