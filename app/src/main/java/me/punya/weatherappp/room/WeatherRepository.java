package me.punya.weatherappp.room;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.punya.weatherappp.models.WeatherResponse;
import me.punya.weatherappp.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRepository {

    private final WeatherDao weatherDao;
    private final ApiService apiService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final MutableLiveData<WeatherResponse> weatherLiveData = new MutableLiveData<>();

    public WeatherRepository(WeatherDao weatherDao, ApiService apiService) {
        this.weatherDao = weatherDao;
        this.apiService = apiService;
    }

    public LiveData<WeatherResponse> getWeatherLiveData() {
        return weatherLiveData;
    }

    public void fetchWeather(String apiKey, String city, int days) {
        apiService.getWeather(apiKey, city, days).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    weatherLiveData.postValue(weatherResponse);
                    insertWeather(weatherResponse);
                } else {
                    loadLastWeather();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                loadLastWeather();
            }
        });
    }

    private void insertWeather(WeatherResponse weatherResponse) {
        executorService.execute(() -> {
            WeatherEntity entity = new WeatherEntity(
                    weatherResponse.getLocation().name,
                    weatherResponse.getCurrent().temp_c,
                    weatherResponse.getCurrent().condition.text,
                    weatherResponse.getCurrent().humidity,
                    weatherResponse.getCurrent().wind_kph,
                    System.currentTimeMillis()
            );
            weatherDao.insertWeather(entity);
        });
    }

    public void loadLastWeather() {
        executorService.execute(() -> {
            WeatherEntity lastWeather = weatherDao.getLatestWeather();
            if (lastWeather != null) {
                WeatherResponse response = new WeatherResponse();
                response.location = new WeatherResponse.Location();
                response.location.name = lastWeather.getCity();
                response.current = new WeatherResponse.Current();
                response.current.temp_c = lastWeather.getTemperature();
                response.current.condition = new WeatherResponse.Condition();
                response.current.condition.text = lastWeather.getCondition();
                response.current.humidity = lastWeather.getHumidity();
                response.current.wind_kph = lastWeather.getWindSpeed();
                weatherLiveData.postValue(response);
            } else {
                weatherLiveData.postValue(null);
            }
        });
    }
}
