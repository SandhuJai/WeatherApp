package com.mkbhd.weather.data.service;

import android.net.Uri;
import android.os.AsyncTask;
import com.mkbhd.weather.data.Channel;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by mkbhd on 25/3/18.
 */

public class YahooWeatherService {
    private WeatherServiceCallBack callBack;
    private String location;
    private Exception error;

    public YahooWeatherService(WeatherServiceCallBack call) {
        callBack = call;
    }

    public String getLocation() {
        return location;
    }

    public void refreshWeather(final String location) {
        this.location = location;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='c'", strings[0]);
                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder result = new StringBuilder();
                    String line;

                    while((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();
                } catch (Exception e) {
                    error = e;
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                if(s == null && error != null) {
                    callBack.serviceFailure(error);
                    return ;
                }

                try {
                    JSONObject data = new JSONObject(s);

                    JSONObject queryResults = data.optJSONObject("query");
                    int count = queryResults.getInt("count");

                    if(count == 0) {
                        callBack.serviceFailure(new LocationNotFoundException("Can't find this Location"));
                        return ;
                    }

                    Channel channel = new Channel();
                    channel.populate(queryResults.optJSONObject("results").optJSONObject("channel"));
                    callBack.serviceSuccess(channel);
                } catch (JSONException e) {
                    callBack.serviceFailure(e);
                }

            }
        }.execute(location);
    }

    public class LocationNotFoundException extends Exception {
        public LocationNotFoundException(String msg) {
            super(msg);
        }
    }

}
