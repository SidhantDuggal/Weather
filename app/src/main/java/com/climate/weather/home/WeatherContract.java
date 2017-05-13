package com.climate.weather.home;

import android.content.SharedPreferences;

import com.climate.weather.utils.ScreenDimens;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Contract that specifies the interaction between view, presenter and datasource
 */

public class WeatherContract {

    public interface View{
        Picasso getPicassoInstance();
        void setBackground(String url);
        SharedPreferences getImagesPrefs();
        void setWeather(String city, String temp, String minTemp, String maxTemp, String humidity,
                        String visibility);
        void initViews();
        ScreenDimens getScreenSize();
        void setDailyForecast(ArrayList<String> dailyForecasts);
        void setHourlyForecast(ArrayList<String > hourlyForecasts);
    }

    public interface Presenter{
        void onLocationReceived(double lat, double lon, SharedPreferences preferences,
                                boolean isFromDeeplink);
        void onWeatherReceived(String city, String temp, String minTemp,
                          String maxTemp, String humidity, String visibility);
        void onDailyForecastReceived(ArrayList<String> dailyForecasts);
        void onHourlyForecastReceived(ArrayList<String> dailyForecasts);
        void onImageReceived(String url);
        void onLocationReceived(String location, SharedPreferences preferences,
                               boolean isFromDeeplink);
    }

    public interface DataSource{
        void getWeather(String location, SharedPreferences preferences, boolean isFromDeeplink);
        void getWeather(Double lat, Double lon, SharedPreferences preferences,
                        boolean isFromDeeplink);
        void getDailyForecast(String location, SharedPreferences preferences,
                              boolean isFromDeeplink);
        void getDailyForecast(Double lat, Double lon, SharedPreferences preferences,
                              boolean isFromDeeplink);
        void getHourlyForecast(String location, SharedPreferences preferences,
                               boolean isFromDeeplink);
        void getHourlyForecast(Double lat, Double lon, SharedPreferences preferences,
                               boolean isFromDeeplink);
    }
}
