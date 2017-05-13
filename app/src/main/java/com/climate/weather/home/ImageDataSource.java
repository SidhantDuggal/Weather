package com.climate.weather.home;

import android.content.SharedPreferences;
import android.net.Uri;

import com.climate.weather.mvp.BaseDataSource;
import com.climate.weather.utils.ScreenDimens;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Random;

/**
 * Datasource that encapsulate image handling
 */

public class ImageDataSource extends BaseDataSource<HomePresenter> {

    private final static String TAG = ImageDataSource.class.getName();
    private final static String IMAGE_URL = "url";
    private final static String SCREEN_DIMENS_KEY = "screen_dimens";
    private final static int IMAGE_ID_MIN = 0;
    private final static int IMAGE_ID_MAX = 7;

    private final static int[] curatedImageIds = {1, 1077, 1054, 1049, 1051, 1045, 985, 987};
    private Gson gson;

    public void updateData(SharedPreferences preferences, Picasso picasso){
        String json = preferences.getString(SCREEN_DIMENS_KEY, null);
        if (json == null){
            return;
        }
        ScreenDimens dimens = gson.fromJson(json, ScreenDimens.class);
        getImage(dimens, preferences, picasso);
    }

    public ImageDataSource(){
        this. gson = new Gson();
    }

    public void getImage(ScreenDimens screenDimens, final SharedPreferences preferences,
                         final Picasso picasso){
        saveScreenDimens(preferences, screenDimens, this.gson);
        if(isPresenterAttached()){
            String lastUrl = preferences.getString(IMAGE_URL, null);
            if (lastUrl != null){
                getPresenter().onImageReceived(lastUrl);
            }
        }
        Random rn = new Random();
        int randomNum = rn.nextInt((IMAGE_ID_MAX - IMAGE_ID_MIN) + 1) + IMAGE_ID_MIN;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("unsplash.it")
                .appendPath(String.valueOf(screenDimens.getWidth()))
                .appendPath(String.valueOf(screenDimens.getHeight()))
                .appendQueryParameter("image", String.valueOf(curatedImageIds[randomNum]));
        final String url = builder.build().toString();

        picasso.load(url).fetch(new Callback() {
            @Override
            public void onSuccess() {
                saveImageUrl(preferences, url);
                if (isPresenterAttached()){
                    getPresenter().onImageReceived(url);
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void saveScreenDimens(SharedPreferences preferences, ScreenDimens dimens, Gson gson){
        String json = gson.toJson(dimens);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SCREEN_DIMENS_KEY, json);
        editor.apply();
    }

    private void saveImageUrl(SharedPreferences preferences, String url){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(IMAGE_URL, url);
        editor.apply();
    }
}
