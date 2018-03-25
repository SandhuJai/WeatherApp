package com.mkbhd.weather.data;

import org.json.JSONObject;

/**
 * Created by mkbhd on 25/3/18.
 */

public class Units implements JSONPopulator{
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
