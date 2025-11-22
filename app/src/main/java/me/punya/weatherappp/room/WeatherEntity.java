package me.punya.weatherappp.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather_table")
public class WeatherEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String city;
    private double temperature;
    private String condition;
    private int humidity;
    private double windSpeed;
    private long timestamp;

    public WeatherEntity(String city, double temperature, String condition, int humidity, double windSpeed, long timestamp) {
        this.city = city;
        this.temperature = temperature;
        this.condition = condition;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCity() { return city; }
    public double getTemperature() { return temperature; }
    public String getCondition() { return condition; }
    public int getHumidity() { return humidity; }
    public double getWindSpeed() { return windSpeed; }
    public long getTimestamp() { return timestamp; }
}
