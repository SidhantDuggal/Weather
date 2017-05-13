package com.climate.weather.home;

import android.content.SharedPreferences;

import com.climate.weather.mvp.BasePresenter;

import java.util.ArrayList;

/**
 *  Presenter for Home View
 */

public class HomePresenter extends BasePresenter<HomeActivity> implements WeatherContract.Presenter{

    private static final String TAG = HomePresenter.class.getName();
    private WeatherDataSource weatherDataSource;
    private ImageDataSource imageDataSource;

    @Override
    public void attachView(HomeActivity mvpView) {
        super.attachView(mvpView);
        getMvpView().initViews();
        weatherDataSource = new WeatherDataSource();
        weatherDataSource.attachPresenter(this);
        imageDataSource = new ImageDataSource();
        imageDataSource.attachPresenter(this);
        imageDataSource.getImage(getMvpView().getScreenSize(), getMvpView().getImagesPrefs(),
                getMvpView().getPicassoInstance());
    }

    @Override
    public void detachView() {
        super.detachView();
        weatherDataSource.detachPresenter();
    }

    public void onLocationReceived(double lat, double lon, SharedPreferences preferences,
                                   boolean isFromDeeplink){
        weatherDataSource.getWeather(lat, lon, preferences, isFromDeeplink);
        weatherDataSource.getDailyForecast(lat, lon, preferences, isFromDeeplink);
        weatherDataSource.getHourlyForecast(lat, lon, preferences, isFromDeeplink);
    }

    public void onWeatherReceived(String city, String temp, String minTemp,
                                  String maxTemp, String humidity, String visibility){
        if (isViewAttached()){
            getMvpView().setWeather(city, temp, minTemp, maxTemp, humidity, visibility);
        }
    }

    public void onDailyForecastReceived(ArrayList<String> dailyForecasts){
        if (isViewAttached()){
            getMvpView().setDailyForecast(dailyForecasts);
        }
    }

    public void onHourlyForecastReceived(ArrayList<String> dailyForecasts){
        if (isViewAttached()){
            getMvpView().setHourlyForecast(dailyForecasts);
        }
    }

    public void onImageReceived(String url){
        if (isViewAttached()){
            getMvpView().setBackground(url);
        }
    }

    public void onLocationReceived(String location, SharedPreferences preferences,
                                   boolean isFromDeeplink) {
        weatherDataSource.getWeather(location, preferences, isFromDeeplink);
        weatherDataSource.getDailyForecast(location, preferences, isFromDeeplink);
        weatherDataSource.getHourlyForecast(location, preferences, isFromDeeplink);
    }
}
