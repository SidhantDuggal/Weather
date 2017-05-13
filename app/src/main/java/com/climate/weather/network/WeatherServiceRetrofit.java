package com.climate.weather.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class that implements Singleton pattern for Retrofit instance
 */

public class WeatherServiceRetrofit {

    private static volatile Retrofit retrofit;
    private final static String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null){
            synchronized (WeatherServiceRetrofit.class){
                if (retrofit == null){
                    HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                    logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new WeatherServiceInterceptor())
                            .addInterceptor(logInterceptor)
                            .build();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(okHttpClient)
                            .build();
                }
            }

        }
        return retrofit;
    }
}
