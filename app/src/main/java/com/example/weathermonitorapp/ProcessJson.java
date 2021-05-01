package com.example.weathermonitorapp;

import org.json.JSONException;
import org.json.JSONObject;

public class ProcessJson {
    public ProcessJson() {
    }

    //Returns a value from a json
    public String getValue(String valueToGet, String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        String value = jsonObject.getString(valueToGet);

        return value;
    }
}
