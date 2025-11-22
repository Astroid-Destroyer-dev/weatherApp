package me.punya.weatherappp.view;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import me.punya.weatherappp.models.WeatherResponse;
import me.punya.weatherappp.room.WeatherRepository;

public class WeatherViewModel extends ViewModel {

    private final WeatherRepository repository;
    private final LiveData<WeatherResponse> weatherLiveData;

    public WeatherViewModel(WeatherRepository repository) {
        this.repository = repository;
        this.weatherLiveData = repository.getWeatherLiveData();
    }

    public LiveData<WeatherResponse> getWeatherLiveData() {
        return weatherLiveData;
    }

    public void fetchWeather(String api, String city, int days) {
        repository.fetchWeather(api, city, days);
    }

    public void loadWeather() {
        repository.loadLastWeather();
    }
}
