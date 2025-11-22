package me.punya.weatherappp.models;

public class ForecastDayModel {
    private String dayName;
    private String condition;
    private String iconKey;
    private double temperature;


    public ForecastDayModel(String dayName, String condition, String iconKey, double temperature) {
        this.dayName = dayName;
        this.condition = condition;
        this.iconKey = iconKey;
        this.temperature = temperature;
    }


    public String getDayName() { return dayName; }
    public String getCondition() { return condition; }
    public String getIconKey() { return iconKey; }
    public double getTemperature() { return temperature; }
}
