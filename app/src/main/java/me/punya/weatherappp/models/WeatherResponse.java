package me.punya.weatherappp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {

    public Location location;
    public Current current;
    public Forecast forecast;

    public static class Location {
        public String name;
        public String region;
        public String country;
        public String localtime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Forecast getForecast() {
        return forecast;
    }


    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }

    public static class Current {
        public double temp_c;
        public int humidity;
        public double wind_kph;
        public Condition condition;
    }

    public static class Condition {
        public String text;
        public String icon;
        public int code;
    }

    public static class Forecast {
        public List<ForecastDay> forecastday;

    }

    public static class ForecastDay {
        public String date;
        public Day day;
    }

    public static class Day {
        public double maxtemp_c;
        public double mintemp_c;
        public double avgtemp_c;
        public Condition condition;
        public double maxwind_kph;
        public int avghumidity;
    }
}