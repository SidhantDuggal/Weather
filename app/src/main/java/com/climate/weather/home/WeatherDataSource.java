package com.climate.weather.home;

import android.content.SharedPreferences;

import com.climate.weather.home.serviceresponse.DailyForecastResponse;
import com.climate.weather.home.serviceresponse.DailyWeatherItem;
import com.climate.weather.home.serviceresponse.HourlyForecastResponse;
import com.climate.weather.home.serviceresponse.HourlyWeatherItem;
import com.climate.weather.home.serviceresponse.WeatherResponse;
import com.climate.weather.mvp.BaseDataSource;
import com.climate.weather.network.WeatherServiceRetrofit;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Datasource that encapsulates weather and forecast handling
 */
public class WeatherDataSource extends BaseDataSource<HomePresenter>
        implements WeatherContract.DataSource{

    private static final String TAG = WeatherDataSource.class.getName();
    private static final String TEMP_UNIT = "metric";
    private static final String DAILY_FORECAST_FORMAT = "dd-MM";
    private static final String HOURLY_FORECAST_FORMAT = "HH-mm";
    private static final String WEATHER_LAST_UPDATE_KEY = "weather_last_update";
    private static final String HOURLY_FORECAST_LAST_UPDATE_KEY = "hourly_forecast_last_update";
    private static final String DAILY_FORECAST_LAST_UPDATE_KEY = "daily_forecast_last_update";
    private static final String WEATHER_KEY = "weather";
    private static final String HOURLY_FORECAST_KEY = "hourly_forecast";
    private static final String DAILY_FORECAST_KEY = "daily_forecast";
    private static final String LAT_KEY = "latitude";
    private static final String LON_KEY = "longitude";

    private SharedPreferences preferences;
    private boolean isFromDeeplink;
    private WeatherService weatherService;
    private Gson gson;

    public WeatherDataSource(){
        Retrofit retrofit = WeatherServiceRetrofit.getRetrofitInstance();
        weatherService = retrofit.create(WeatherService.class);
        gson = new Gson();
    }

    private Callback<WeatherResponse> weatherCallback = new Callback<WeatherResponse>() {
        @Override
        public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
            if (!response.isSuccessful()){
                return;
            }
            WeatherResponse resp = response.body();
            if (!WeatherDataSource.this.isFromDeeplink){
                long unixTime = System.currentTimeMillis() / 1000L;
                saveWeather(WeatherDataSource.this.preferences, WEATHER_LAST_UPDATE_KEY,
                        String.valueOf(unixTime), WEATHER_KEY, resp, gson);
            }
            if (isPresenterAttached()){
                shareWeatherWithPresenter(resp);
            }
        }

        @Override
        public void onFailure(Call<WeatherResponse> call, Throwable t) {
            t.printStackTrace();
        }
    };

    private Callback<DailyForecastResponse> dailyForecastCallback = new Callback<DailyForecastResponse>() {
        @Override
        public void onResponse(Call<DailyForecastResponse> call, Response<DailyForecastResponse> response) {
            if (!response.isSuccessful()){
                return;
            }
            DailyForecastResponse resp = response.body();
            if (!WeatherDataSource.this.isFromDeeplink){
                long unixTime = System.currentTimeMillis() / 1000L;
                saveDailyForecast(WeatherDataSource.this.preferences, DAILY_FORECAST_LAST_UPDATE_KEY,
                        String.valueOf(unixTime), DAILY_FORECAST_KEY, resp, gson);
            }
            if (isPresenterAttached()){
                shareDailyForecastWithPresenter(resp);

            }
        }

        @Override
        public void onFailure(Call<DailyForecastResponse> call, Throwable t) {
            t.printStackTrace();
        }
    };

    private Callback<HourlyForecastResponse> hourlyForecastCallback = new Callback<HourlyForecastResponse>() {
        @Override
        public void onResponse(Call<HourlyForecastResponse> call, Response<HourlyForecastResponse> response) {
            if (!response.isSuccessful()){
                return;
            }
            HourlyForecastResponse resp = response.body();
            if (!WeatherDataSource.this.isFromDeeplink){
                long unixTime = System.currentTimeMillis() / 1000L;
                saveHourlyForecast(WeatherDataSource.this.preferences, HOURLY_FORECAST_LAST_UPDATE_KEY,
                        String.valueOf(unixTime), HOURLY_FORECAST_KEY, resp, gson);
            }

            if (isPresenterAttached()){
                shareHourlyForecastWithPresenter(resp);

            }
        }

        @Override
        public void onFailure(Call<HourlyForecastResponse> call, Throwable t) {
            t.printStackTrace();
        }
    };

    public void updateData(SharedPreferences preferences){
        Double latitude = Double.valueOf(preferences.getString(LAT_KEY, null));
        Double longitude = Double.valueOf(preferences.getString(LON_KEY, null));
        if (latitude == null || longitude == null) {
            return;
        }
        getWeather(latitude, longitude, preferences, false);
        getDailyForecast(latitude, longitude, preferences, false);
        getHourlyForecast(latitude, longitude, preferences, false);
    }

    public void getWeather(String location, SharedPreferences preferences,
                            boolean isFromDeeplink){
        this.preferences = preferences;
        this.isFromDeeplink = isFromDeeplink;
        Call<WeatherResponse> call = weatherService.getWeather(location, TEMP_UNIT);
        call.enqueue(weatherCallback);
    }

    public void getWeather(Double lat, Double lon, SharedPreferences preferences,
                           boolean isFromDeeplink){
        this.preferences = preferences;
        this.isFromDeeplink = isFromDeeplink;
        if (!this.isFromDeeplink){
            saveCoords(preferences, LAT_KEY, String.valueOf(lat), LON_KEY, String.valueOf(lon));
            String json = preferences.getString(WEATHER_KEY, null);
            if  (json != null){
                WeatherResponse resp = gson.fromJson(json, WeatherResponse.class);
                if (isPresenterAttached()){
                    shareWeatherWithPresenter(resp);
                }
            }
        }
        Call<WeatherResponse> call = weatherService.getWeather(lat, lon, TEMP_UNIT);
        call.enqueue(weatherCallback);
    }

    public void getDailyForecast(String location, SharedPreferences preferences,
                                 boolean isFromDeeplink){
        this.preferences = preferences;
        this.isFromDeeplink = isFromDeeplink;
        Call<DailyForecastResponse> call = weatherService.getDailyForecast(location, TEMP_UNIT);
        call.enqueue(dailyForecastCallback);
    }

    public void getDailyForecast(Double lat, Double lon, SharedPreferences preferences,
                                 boolean isFromDeeplink){
        this.preferences = preferences;
        this.isFromDeeplink = isFromDeeplink;
        if (!this.isFromDeeplink){
            saveCoords(preferences, LAT_KEY, String.valueOf(lat), LON_KEY, String.valueOf(lon));
            String json = preferences.getString(DAILY_FORECAST_KEY, null);
            if  (json != null){
                DailyForecastResponse resp = gson.fromJson(json, DailyForecastResponse.class);
                if (isPresenterAttached()){
                    shareDailyForecastWithPresenter(resp);
                }
            }
        }
        Call<DailyForecastResponse> call = weatherService.getDailyForecast(lat, lon, TEMP_UNIT);
        call.enqueue(dailyForecastCallback);
    }

    public void getHourlyForecast(String location, SharedPreferences preferences,
                                  boolean isFromDeeplink){
        this.preferences = preferences;
        this.isFromDeeplink = isFromDeeplink;
        Call<HourlyForecastResponse> call = weatherService.getHourlyForecast(location, TEMP_UNIT);
        call.enqueue(hourlyForecastCallback);
    }

    public void getHourlyForecast(Double lat, Double lon, SharedPreferences preferences,
                                  boolean isFromDeeplink){
        this.preferences = preferences;
        this.isFromDeeplink = isFromDeeplink;
        if (!this.isFromDeeplink){
            saveCoords(preferences, LAT_KEY, String.valueOf(lat), LON_KEY, String.valueOf(lon));
            String json = preferences.getString(HOURLY_FORECAST_KEY, null);
            if  (json != null){
                HourlyForecastResponse resp = gson.fromJson(json, HourlyForecastResponse.class);
                if (isPresenterAttached()){
                    shareHourlyForecastWithPresenter(resp);
                }
            }
        }
        Call<HourlyForecastResponse> call = weatherService.getHourlyForecast(lat, lon, TEMP_UNIT);
        call.enqueue(hourlyForecastCallback);
    }

    private void saveHourlyForecast(SharedPreferences preferences, String timestampKey,
                                    String timestamp, String hourlyForecastResponseKey,
                                    HourlyForecastResponse hourlyForecastResponse, Gson gson){
        String json = gson.toJson(hourlyForecastResponse);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(timestampKey, timestamp);
        editor.putString(hourlyForecastResponseKey, json);
        editor.apply();
    }

    private void saveDailyForecast(SharedPreferences preferences, String timestampKey,
                                   String timestamp, String dailyForecastResponseKey,
                                   DailyForecastResponse dailyForecastResponse, Gson gson){
        String json = gson.toJson(dailyForecastResponse);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(timestampKey, timestamp);
        editor.putString(dailyForecastResponseKey, json);
        editor.apply();
    }

    private void saveWeather(SharedPreferences preferences, String timestampKey,
                             String timestamp, String weatherResponseKey,
                             WeatherResponse weatherResponse, Gson gson){
        String json = gson.toJson(weatherResponse);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(timestampKey, timestamp);
        editor.putString(weatherResponseKey, json);
        editor.apply();
    }

    private void saveCoords(SharedPreferences preferences, String latKey, String latitude,
                            String lonKey, String longitude){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(latKey, latitude);
        editor.putString(lonKey, longitude);
        editor.apply();
    }

    private void shareWeatherWithPresenter(WeatherResponse resp){
        presenter.onWeatherReceived("City: " + resp.getName(),
                "Current Temp: " + String.valueOf(resp.getMain().getTemp()),
                "Min Temp:: " + String.valueOf(resp.getMain().getTempMin()),
                "Max Temp: " + String.valueOf(resp.getMain().getTempMax()),
                "Humidity: " + String.valueOf(resp.getMain().getHumidity()),
                "Visibility: " + String.valueOf(resp.getVisibility()));

    }

    private void shareDailyForecastWithPresenter(DailyForecastResponse resp){
        ArrayList<String> dailyForecasts = new ArrayList<>();
        for(DailyWeatherItem dailyWeatherItem : resp.getDailyWeatherItems()){
            dailyWeatherItem.convertUnixTime(DAILY_FORECAST_FORMAT);
            dailyForecasts.add(dailyWeatherItem.getDate()
                    + " \n Temp:" + dailyWeatherItem.getTemp().getDay()
                    + " \n Max: " + dailyWeatherItem.getTemp().getMax()
                    + " \n Min: " + dailyWeatherItem.getTemp().getMin());
        }
        presenter.onDailyForecastReceived(dailyForecasts);
    }

    private void shareHourlyForecastWithPresenter(HourlyForecastResponse resp){
        ArrayList<String> hourlyForecasts = new ArrayList<>();
        hourlyForecasts.add("Time: " + " \n " + "Temp: " + " \n" + "Condition");
        for(HourlyWeatherItem weatherItem: resp.getHourlyWeatherItems()){
            weatherItem.convertUnixTime(HOURLY_FORECAST_FORMAT);
            hourlyForecasts.add(weatherItem.getDate()
                    + " \n " + weatherItem.getMain().getTemp()
                    + " \n " + weatherItem.getWeather().get(0).getMain());
        }
        presenter.onHourlyForecastReceived(hourlyForecasts);
    }

}

