package com.climate.weather.home.serviceresponse;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HourlyWeatherItem {
    @SerializedName("dt")
    @Expose
    private Integer dt;
    @SerializedName("main")
    @Expose
    private Main main;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("wind")
    @Expose
    private Wind wind;
    @SerializedName("dt_txt")
    @Expose
    private String dtTxt;
    @SerializedName("rain")
    @Expose
    private Rain rain;

    private String date;

    public final static Parcelable.Creator<HourlyWeatherItem> CREATOR = new Parcelable.Creator<HourlyWeatherItem>() {


        @SuppressWarnings({
                "unchecked"
        })
        public HourlyWeatherItem createFromParcel(Parcel in) {
            HourlyWeatherItem instance = new HourlyWeatherItem();
            instance.dt = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.main = ((Main) in.readValue((Main.class.getClassLoader())));
            in.readList(instance.weather, (com.climate.weather.home.serviceresponse.Weather.class.getClassLoader()));
            instance.wind = ((Wind) in.readValue((Wind.class.getClassLoader())));
            instance.dtTxt = ((String) in.readValue((String.class.getClassLoader())));
            instance.rain = ((Rain) in.readValue((Rain.class.getClassLoader())));
            return instance;
        }

        public HourlyWeatherItem[] newArray(int size) {
            return (new HourlyWeatherItem[size]);
        }

    }
            ;

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public void setDtTxt(String dtTxt) {
        this.dtTxt = dtTxt;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(dt);
        dest.writeValue(main);
        dest.writeList(weather);
        dest.writeValue(wind);
        dest.writeValue(dtTxt);
        dest.writeValue(rain);
    }

    public int describeContents() {
        return 0;
    }

    public void convertUnixTime(String format){
        Date date = new Date(this.dt*1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        this.date = simpleDateFormat.format(date);
    }

    public String getDate(){
        return  this.date;
    }
}
