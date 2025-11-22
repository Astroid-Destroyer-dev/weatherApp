package me.punya.weatherappp.view;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import me.punya.weatherappp.room.WeatherRepository;

public class WeatherViewModelFactory implements ViewModelProvider.Factory {

    private WeatherRepository repository;

    public WeatherViewModelFactory(WeatherRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WeatherViewModel.class)) {
            return (T) new WeatherViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

