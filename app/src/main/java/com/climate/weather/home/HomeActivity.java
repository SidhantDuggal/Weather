package com.climate.weather.home;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.climate.weather.CustPicasso;
import com.climate.weather.R;
import com.climate.weather.mvp.MvpView;
import com.climate.weather.utils.ScreenDimens;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;


/**
 * Activity that acts as view in MVP pattern.
 */
public class HomeActivity extends AppCompatActivity implements MvpView, WeatherContract.View,
        LocationListener{

    private static final String TAG = HomeActivity.class.getName();

    private static final String AUTHORITY = "com.climate.weather.provider";
    private static final String ACCOUNT_TYPE = "climate.com";
    private static final String ACCOUNT = "default_account";
    private static final long SECONDS_PER_MINUTE = 60L;
    private static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;
//    private static final long SYNC_INTERVAL = 30L;

    private boolean isNewAccountCreated = false;
    private HomePresenter presenter;
    private LocationManager locationManager;

    private LinearLayout rootView;
    private TextView cityName;
    private TextView currentTemp;
    private TextView minTemp;
    private TextView maxTemp;
    private TextView humidity;
    private TextView visibility;
    private RecyclerView hourlyForecast, dailyForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Uri uri = getIntent().getData();

        presenter = new HomePresenter();
        presenter.attachView(this);
        if(uri != null){
            if (uri.getQueryParameter("location") != null){
                presenter.onLocationReceived(uri.getQueryParameter("location"),
                        getSharedPreferences("CLIMATE", MODE_PRIVATE), true);
            }
            if (uri.getQueryParameter("lat") != null && uri.getQueryParameter("lon") != null){
                presenter.onLocationReceived(Double.valueOf(uri.getQueryParameter("lat")),
                        Double.valueOf(uri.getQueryParameter("lon")),
                        getSharedPreferences("CLIMATE", MODE_PRIVATE), true);
            }
        } else{
            initLocationTracking();
        }

    }

    private void initLocationTracking(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            onLocationChanged(location);
        }
        Account account = CreateSyncAccount(this);
        if (isNewAccountCreated){
            schedulePeriodicSync(account);
        }
        locationManager.requestSingleUpdate(provider, this, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    @Override
    public void showMessage(String message) {

    }

    /**
     * Create a new dummy account for the sync adapter
     */
    private Account CreateSyncAccount(Context context) {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager = (AccountManager) context.getSystemService(ACCOUNT_SERVICE);
        isNewAccountCreated = accountManager.addAccountExplicitly(newAccount, null, null);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        return  accounts[0];
    }

    private void schedulePeriodicSync(Account account){
        ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        boolean isFromDeeplink = false;
        locationManager.removeUpdates(this);
        presenter.onLocationReceived(lat, lon, getSharedPreferences("CLIMATE", MODE_PRIVATE), isFromDeeplink);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void setDailyForecast(ArrayList<String> dailyForecasts){
        DailyForecastAdapter adapter = new DailyForecastAdapter(dailyForecasts, this);
        dailyForecast.setAdapter(adapter);
    }

    public void setHourlyForecast(ArrayList<String > hourlyForecasts){
        HourlyForecastAdapter adapter = new HourlyForecastAdapter(hourlyForecasts, this);
        hourlyForecast.setAdapter(adapter);
    }

    public ScreenDimens getScreenSize(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        return new ScreenDimens(height, width);
    }

    public void initViews(){
        rootView = (LinearLayout) findViewById(R.id.root);
        cityName = (TextView) findViewById(R.id.city);
        currentTemp = (TextView) findViewById(R.id.current_temp);
        minTemp = (TextView) findViewById(R.id.temp_min);
        maxTemp = (TextView) findViewById(R.id.temp_max);
        humidity = (TextView) findViewById(R.id.humidity);
        visibility = (TextView) findViewById(R.id.visibility);
        hourlyForecast = (RecyclerView) findViewById(R.id.hourly_forecast);
        dailyForecast = (RecyclerView) findViewById(R.id.daily_forecast);

        LinearLayoutManager dailyForecastManager = new LinearLayoutManager(this);
        dailyForecastManager.setOrientation(LinearLayoutManager.VERTICAL);
        dailyForecast.setLayoutManager(dailyForecastManager);

        LinearLayoutManager hourlyForecastManager = new LinearLayoutManager(this);
        hourlyForecastManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hourlyForecast.setLayoutManager(hourlyForecastManager);
    }

    public void setWeather(String city, String temp, String minTemp,
                                  String maxTemp, String humidity, String visibility){
        cityName.setText(city);
        currentTemp.setText(temp);
        this.minTemp.setText(minTemp);
        this.maxTemp.setText(maxTemp);
        this.humidity.setText(humidity);
        this.visibility.setText(visibility);
    }

    public SharedPreferences getImagesPrefs(){
        return this.getSharedPreferences("IMAGES", MODE_PRIVATE);
    }

    public void setBackground(String url){
        Picasso.with(this).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                if(Build.VERSION.SDK_INT > 16) {
                    rootView.setBackground(bd);
                } else {
                    rootView.setBackgroundDrawable(bd);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    public Picasso getPicassoInstance() {
        return CustPicasso.getPicassoInstance(this);
    }
}
