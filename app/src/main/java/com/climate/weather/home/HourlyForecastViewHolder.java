package com.climate.weather.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.climate.weather.R;

public class HourlyForecastViewHolder extends RecyclerView.ViewHolder {

    private TextView hourlyForecastTextView;

    public HourlyForecastViewHolder(View itemView) {
        super(itemView);
        hourlyForecastTextView = (TextView) itemView.findViewById(R.id.hourly_forecast_item_text);
    }

    public void bindData(String text) {
        hourlyForecastTextView.setText(text);
    }
}
