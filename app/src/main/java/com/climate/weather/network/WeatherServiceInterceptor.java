package com.climate.weather.network;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Class that appends access token as a query parameter to all weather service requests
 */

public class WeatherServiceInterceptor implements Interceptor {

    private final static String TOKEN_Q_PARAM_NAME = "appid";
    private final static String TOKEN_Q_PARAM_VALUE = "642b7ba800a188c78247cb2fd625a794";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter(TOKEN_Q_PARAM_NAME, TOKEN_Q_PARAM_VALUE)
                .build();
        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
