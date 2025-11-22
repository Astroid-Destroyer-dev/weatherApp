package me.punya.weatherappp.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeather(WeatherEntity weather);

    @Query("SELECT * FROM weather_table ORDER BY timestamp DESC LIMIT 1")
    WeatherEntity getLatestWeather();

    @Query("SELECT * FROM weather_table ORDER BY timestamp DESC")
    LiveData<List<WeatherEntity>> getAllWeather();
}

