package com.climate.weather.home;

import com.climate.weather.home.serviceresponse.DailyForecastResponse;
import com.climate.weather.home.serviceresponse.HourlyForecastResponse;
import com.climate.weather.home.serviceresponse.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    String API_WEATHER = "weather";
    String API_FORECAST_DAILY = "forecast/daily";
    String API_FORECAST_HOURLY = "forecast";

    @GET(API_WEATHER)
    Call<WeatherResponse> getWeather(@Query("q") String location,
                                     @Query("units") String measurementUnit);

    @GET(API_WEATHER)
    Call<WeatherResponse> getWeather(@Query("lat") Double latitude,
                                     @Query("lon") Double longitude,
                                     @Query("units") String measurementUnit);

    @GET(API_FORECAST_DAILY)
    Call<DailyForecastResponse> getDailyForecast(@Query("q") String location,
                                                 @Query("units") String measurementUnit);

    @GET(API_FORECAST_DAILY)
    Call<DailyForecastResponse> getDailyForecast(@Query("lat") Double latitude,
                                                 @Query("lon") Double longitude,
                                                 @Query("units") String measurementUnit);

    @GET(API_FORECAST_HOURLY)
    Call<HourlyForecastResponse> getHourlyForecast(@Query("q") String location,
                                                   @Query("units") String measurementUnit);

    @GET(API_FORECAST_HOURLY)
    Call<HourlyForecastResponse> getHourlyForecast(@Query("lat") Double latitude,
                                                 @Query("lon") Double longitude,
                                                 @Query("units") String measurementUnit);

}
