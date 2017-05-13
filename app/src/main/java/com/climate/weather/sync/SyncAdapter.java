package com.climate.weather.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.climate.weather.CustPicasso;
import com.climate.weather.home.ImageDataSource;
import com.climate.weather.home.WeatherDataSource;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;

/**
 * Handle the transfer of data between a server and an app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter{

    private final static String TAG = SyncAdapter.class.getName();

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    /**
     * This method is run on background thread.
     */
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
        WeatherDataSource weatherDataSource = new WeatherDataSource();
        weatherDataSource.updateData(getContext().getSharedPreferences("CLIMATE", MODE_PRIVATE));
        ImageDataSource imageDataSource = new ImageDataSource();
        imageDataSource.updateData(getContext().getSharedPreferences("IMAGES", MODE_PRIVATE),
                CustPicasso.getPicassoInstance(getContext()));
    }

}
