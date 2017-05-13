package com.climate.weather.home.serviceresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HourlyForecastResponse implements Parcelable{

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
    private List<HourlyWeatherItem> hourlyWeatherItems = null;
    @SerializedName("city")
    @Expose
    private City city;
    public final static Parcelable.Creator<HourlyForecastResponse> CREATOR = new Creator<HourlyForecastResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public HourlyForecastResponse createFromParcel(Parcel in) {
            HourlyForecastResponse instance = new HourlyForecastResponse();
            instance.cod = ((String) in.readValue((String.class.getClassLoader())));
            instance.message = ((Double) in.readValue((Double.class.getClassLoader())));
            instance.cnt = ((Integer) in.readValue((Integer.class.getClassLoader())));
            in.readList(instance.hourlyWeatherItems, (HourlyWeatherItem.class.getClassLoader()));
            instance.city = ((City) in.readValue((City.class.getClassLoader())));
            return instance;
        }

        public HourlyForecastResponse[] newArray(int size) {
            return (new HourlyForecastResponse[size]);
        }

    }
            ;

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

    public List<HourlyWeatherItem> getHourlyWeatherItems() {
        return hourlyWeatherItems;
    }

    public void setHourlyWeatherItems(List<HourlyWeatherItem> hourlyWeatherItems) {
        this.hourlyWeatherItems = hourlyWeatherItems;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(cod);
        dest.writeValue(message);
        dest.writeValue(cnt);
        dest.writeList(hourlyWeatherItems);
        dest.writeValue(city);
    }

    public int describeContents() {
        return 0;
    }
}
