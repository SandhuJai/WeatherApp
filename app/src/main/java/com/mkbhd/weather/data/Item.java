package com.mkbhd.weather.data;

import org.json.JSONObject;

/**
 * Created by mkbhd on 25/3/18.
 */

public class Item implements JSONPopulator{
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));
    }
}
