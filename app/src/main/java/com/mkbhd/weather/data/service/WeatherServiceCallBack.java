package com.mkbhd.weather.data.service;

import com.mkbhd.weather.data.Channel;

/**
 * Created by mkbhd on 25/3/18.
 */

public interface WeatherServiceCallBack {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
