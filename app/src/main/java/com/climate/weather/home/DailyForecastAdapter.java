package com.climate.weather.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.climate.weather.R;

import java.util.ArrayList;

/**
 * Adapter for daily forecast recycler view
 */

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastViewHolder> {

    private ArrayList<String> items;
    private Context context;

    public DailyForecastAdapter(ArrayList<String> items, Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public DailyForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.daily_forecast_item, parent, false);
        return new DailyForecastViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(DailyForecastViewHolder holder, int position) {
        holder.bindData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
