package com.climate.weather;

import android.content.Context;

import com.squareup.picasso.Picasso;

/**
 * Class that implements singleton pattern for Piaccasso instance
 */

public class CustPicasso {

    private static volatile Picasso instance;
    public static Picasso getPicassoInstance(Context context){
        if (instance == null){
            synchronized (CustPicasso.class){
                if (instance == null){
                    instance = Picasso.with(context);
                    instance.setLoggingEnabled(true);
                }
            }
        }
        return instance;
    }
}
