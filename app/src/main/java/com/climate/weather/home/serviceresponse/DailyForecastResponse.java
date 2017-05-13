package com.climate.weather.home.serviceresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyForecastResponse implements Parcelable {

    @SerializedName("city")
    @Expose
    private City city;
    @SerializedName("cod")
    @Expose
    private String cod;
    @SerializedName("message")
    @Expose
    private Double message;
    @SerializedName("cnt")
    @Expose
    private Integer cnt;
    @SerializedName("list")
    @Expose
    private List<DailyWeatherItem> dailyWeatherItems = null;
    public final static Parcelable.Creator<DailyForecastResponse> CREATOR = new Creator<DailyForecastResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DailyForecastResponse createFromParcel(Parcel in) {
            DailyForecastResponse instance = new DailyForecastResponse();
            instance.city = ((City) in.readValue((City.class.getClassLoader())));
            instance.cod = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.cnt = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.dailyWeatherItems, (DailyWeatherItem.class.getClassLoader()));
            return instance;
        }

        public DailyForecastResponse[] newArray(int size) {
            return (new DailyForecastResponse[size]);
        }

    }
            ;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public java.util.List<DailyWeatherItem> getDailyWeatherItems() {
        return dailyWeatherItems;
    }

    public void setDailyWeatherItems(List<DailyWeatherItem> dailyWeatherItems) {
        this.dailyWeatherItems = dailyWeatherItems;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(city);
        dest.writeValue(cod);
        dest.writeValue(message);
        dest.writeValue(cnt);
        dest.writeList(dailyWeatherItems);
    }

    public int describeContents() {
        return 0;
    }
}
