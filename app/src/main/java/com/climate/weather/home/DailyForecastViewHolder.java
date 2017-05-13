package com.climate.weather.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.climate.weather.R;

/**
 * Viewholder for daily forecast
 */

class DailyForecastViewHolder extends RecyclerView.ViewHolder{

    private TextView dailyForecastItemTextView;

    public DailyForecastViewHolder(View itemView) {
        super(itemView);
        this.dailyForecastItemTextView = (TextView) itemView.findViewById(R.id.daily_forecast_item_text);
    }

    public void bindData(String text) {
        dailyForecastItemTextView.setText(text);
    }
}
