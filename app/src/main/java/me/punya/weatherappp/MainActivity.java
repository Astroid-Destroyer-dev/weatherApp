package me.punya.weatherappp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import me.punya.weatherappp.adapters.ForecastAdapter;
import me.punya.weatherappp.models.ForecastDayModel;
import me.punya.weatherappp.models.WeatherResponse;
import me.punya.weatherappp.network.ApiService;
import me.punya.weatherappp.room.WeatherDao;
import me.punya.weatherappp.room.WeatherDatabase;
import me.punya.weatherappp.room.WeatherEntity;
import me.punya.weatherappp.room.WeatherRepository;
import me.punya.weatherappp.view.WeatherViewModel;
import me.punya.weatherappp.view.WeatherViewModelFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final int LOCATION_REQUEST_CODE = 1;
    private static final String API_KEY = "";
    RecyclerView forecastRecycler;
    ForecastAdapter forecastAdapter;
    List<ForecastDayModel> forecastList = new ArrayList<>();
    private WeatherViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    TextInputEditText inputCity;
    TextInputLayout cityInputLayout;
    TextView textCondition, textTemperature, textHumidity, textWind;
    LottieAnimationView weatherAnimation;
    TextView cityname;

    private String lastQuery = null;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main_layout);
        setupForecastRecycler();
        WeatherDatabase db = WeatherDatabase.getDatabase(this);
        WeatherDao dao = db.weatherDao();
        ApiService api = new Retrofit.Builder().baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
        WeatherRepository repository = new WeatherRepository(dao, api);
        WeatherViewModelFactory factory = new WeatherViewModelFactory(repository);

        viewModel = new ViewModelProvider(this, factory).get(WeatherViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        observeViewModel();
        initUI();
        setupNetworkCallback();
        requestLocation();
        inputCity.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String city = inputCity.getText().toString().trim();
                if (!city.isEmpty()) {
                    fetchWeatherForCity(city);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });


    }

    private void setupNetworkCallback() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                if (lastQuery != null) {
                    runOnUiThread(() -> {
                        Toast.makeText(MainActivity.this, "Back online. Refreshing...", Toast.LENGTH_SHORT).show();
                        viewModel.fetchWeather(API_KEY, lastQuery, 5);
                    });
                }
            }
        };
    }


    @SuppressLint("NotifyDataSetChanged")
    private void observeViewModel() {
        viewModel.getWeatherLiveData().observe(this, weather -> {
            if (weather != null) {

                WeatherEntity entity = new WeatherEntity(
                        weather.getLocation().name,
                        weather.getCurrent().temp_c,
                        weather.getCurrent().condition.text,
                        weather.getCurrent().humidity,
                        weather.getCurrent().wind_kph,
                        System.currentTimeMillis());
                updateUI(entity);
                if (weather.getForecast() != null &&
                        weather.getForecast().forecastday != null) {
                    forecastList.clear();
                    for (WeatherResponse.ForecastDay f :
                            weather.getForecast().forecastday) {
                        forecastList.add(
                                new ForecastDayModel(
                                        f.date,
                                        f.day.condition.text,
                                        f.day.condition.icon,
                                        f.day.avgtemp_c
                                )
                        );

                    }
                    updateForecastUI();
                }
            } else {

                Log.d("bumblebee", "observeViewModel: weather is null");
            }
        });


    }

    private void initUI() {
        inputCity = findViewById(R.id.inputCity);
        cityInputLayout = findViewById(R.id.cityInputLayout);
        textCondition = findViewById(R.id.textCondition);
        textTemperature = findViewById(R.id.textTemperature);
        textHumidity = findViewById(R.id.textHumidity);
        textWind = findViewById(R.id.textWind);
        weatherAnimation = findViewById(R.id.weatherAnimation);
        cityname = findViewById(R.id.cityName);

        cityInputLayout.setEndIconOnClickListener(v -> {
            String city = inputCity.getText().toString().trim();
            if (!city.isEmpty()) {
                fetchWeatherForCity(city);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    private void fetchWeatherForCity(String city) {
        if (API_KEY.isEmpty()) {
            Toast.makeText(this, "Please enter your API key in MainActivity.java", Toast.LENGTH_LONG).show();
            return;
        }
        this.lastQuery = city;
        viewModel.fetchWeather(API_KEY, city, 5);
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(WeatherEntity weather) {
        textCondition.setText(weather.getCondition());
        textTemperature.setText(String.format("%.0f°C", weather.getTemperature()));
        textHumidity.setText("Humidity: " + weather.getHumidity() + "%");
        textWind.setText("Wind: " + weather.getWindSpeed() + " km/h");
        loadDynamicAnimation(weather.getCondition());
        cityname.setText(weather.getCity());
        //updateBackground(weather.getCondition());

    }

    private void loadDynamicAnimation(String condition) {
        condition = condition.toLowerCase();
        if (condition.contains("rain")) weatherAnimation.setAnimation(R.raw.rainy);
        else if (condition.contains("cloud") || condition.contains("mist")) weatherAnimation.setAnimation(R.raw.cloudy);
        else if (condition.contains("sun")   || condition.contains("clear"))
            weatherAnimation.setAnimation(R.raw.sunny);
        else if (condition.contains("storm")) weatherAnimation.setAnimation(R.raw.stormy);
        else if (condition.contains("snow")) weatherAnimation.setAnimation(R.raw.snowy);
        else weatherAnimation.setAnimation(R.raw.sunny);
        weatherAnimation.playAnimation();
    }

    private void requestLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                fetchCityByLocation(location);
            }
        });
    }

    private void fetchCityByLocation(Location location) {
        String query = location.getLatitude() + "," + location.getLongitude();
        this.lastQuery = query;
        viewModel.fetchWeather(API_KEY, query, 5);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
    }

    private void setupForecastRecycler() {
        forecastRecycler = findViewById(R.id.recyclerForecast);
        //forecastRecycler.setHasFixedSize(true);
        forecastRecycler.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        forecastAdapter = new ForecastAdapter(this, forecastList);
        forecastRecycler.setAdapter(forecastAdapter);
    }

    // Call this after fetching data and parsing forecast
    private void updateForecastUI() {
        forecastAdapter.notifyDataSetChanged();
        forecastAdapter.getItemCount();
    }
}
