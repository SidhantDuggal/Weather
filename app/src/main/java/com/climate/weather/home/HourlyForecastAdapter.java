package com.climate.weather.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.climate.weather.R;

import java.util.ArrayList;

/**
 * Adapter for hourly forecast
 */

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastViewHolder> {

    private ArrayList<String> items;
    private Context context;

    public HourlyForecastAdapter(ArrayList<String> items, Context context){
        this.items = items;
        this.context = context;
    }


    @Override
    public HourlyForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.hourly_forecast_item, parent, false);
        return new HourlyForecastViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(HourlyForecastViewHolder holder, int position) {
        holder.bindData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
