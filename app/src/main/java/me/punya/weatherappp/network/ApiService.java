package me.punya.weatherappp.network;


import me.punya.weatherappp.models.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("forecast.json")
    Call<WeatherResponse> getWeather(
            @Query("key") String apiKey,
            @Query("q") String city,
            @Query("days") int days
    );

}

