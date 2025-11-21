# 🌤️ Weather Forecast Android App

A modern, feature-rich weather forecast application built with Jetpack Compose and Material Design 3. Get real-time weather updates, 5-day forecasts, and enjoy beautiful weather animations with full offline support.

[![Platform](https://img.shields.io/badge/platform-Android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.10-blue.svg)](http://kotlinlang.org)

## 📱 Features

### Core Functionality
- **Real-time Weather Data**: Current temperature, humidity, wind speed, and conditions
- **5-Day Forecast**: Extended weather predictions with daily highs/lows
- **City Search**: Search for weather in any city worldwide
- **Offline Caching**: Access previously fetched weather data without internet
- **Dynamic Animations**: Beautiful Lottie animations that change based on weather conditions
- **Light/Dark Theme**: Automatically adapts to system theme preferences
- **Error Handling**: Graceful error messages with retry functionality

### Weather Information
- 🌡️ Temperature (Celsius)
- 💧 Humidity percentage
- 💨 Wind speed (km/h)
- 🌈 Weather conditions
- 🤔 "Feels like" temperature
- ☀️ UV index

## 🏗️ Architecture

This app follows modern Android development best practices:

- **MVVM Architecture**: Clean separation of concerns
- **Jetpack Compose**: Modern declarative UI
- **Kotlin Coroutines**: Asynchronous programming
- **Hilt**: Dependency injection
- **Room Database**: Local data persistence
- **Retrofit**: REST API integration
- **Material Design 3**: Modern UI components

### Project Structure

```
app/
├── src/main/java/com/example/weatherapp/
│   ├── models/              # Data models
│   │   └── WeatherData.kt
│   ├── database/            # Room database
│   │   ├── WeatherEntity.kt
│   │   ├── WeatherDao.kt
│   │   └── WeatherDatabase.kt
│   ├── network/             # API service
│   │   └── WeatherApiService.kt
│   ├── repository/          # Data repository
│   │   └── WeatherRepository.kt
│   ├── viewmodel/           # ViewModels
│   │   └── WeatherViewModel.kt
│   ├── ui/                  # Compose UI
│   │   ├── WeatherScreen.kt
│   │   └── theme/
│   │       ├── Theme.kt
│   │       └── Type.kt
│   ├── di/                  # Dependency injection
│   │   └── AppModule.kt
│   ├── MainActivity.kt
│   └── WeatherApplication.kt
└── res/
    └── raw/                 # Lottie animation files
        ├── sunny.json
        ├── cloudy.json
        ├── rainy.json
        ├── snowy.json
        └── stormy.json
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- JDK 11 or higher
- Android SDK API 24+
- Gradle 8.0+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/weather-forecast-app.git
   cd weather-forecast-app
   ```

2. **Get Weather API Key**
   - Visit [WeatherAPI.com](https://www.weatherapi.com/)
   - Sign up for a free account
   - Copy your API key

3. **Configure API Key**
   - Open `app/src/main/java/com/example/weatherapp/repository/WeatherRepository.kt`
   - Replace `YOUR_API_KEY_HERE` with your actual API key:
   ```kotlin
   private val apiKey = "your_actual_api_key_here"
   ```
4. **Sync and Build**
   - Open the project in Android Studio
   - Sync Gradle files
   - Build and run on an emulator or physical device

## 🔧 Configuration

### Gradle Dependencies

The app uses the following key dependencies:

```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui:1.5.4")
implementation("androidx.compose.material3:material3:1.1.2")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// Dependency Injection
implementation("com.google.dagger:hilt-android:2.48")

// Room Database
implementation("androidx.room:room-runtime:2.6.0")
implementation("androidx.room:room-ktx:2.6.0")

// Lottie Animations
implementation("com.airbnb.android:lottie-compose:6.1.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

### Permissions

The app requires the following permissions in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

## 📖 How It Works

### Data Flow

1. **User Input**: User enters a city name or uses current location
2. **API Request**: App sends request to WeatherAPI.com
3. **Data Processing**: Response is parsed and converted to app models
4. **Caching**: Data is stored in Room database for offline access
5. **UI Update**: Weather information is displayed with animations
6. **Offline Mode**: If network fails, cached data is retrieved from database

### Offline Caching Strategy

- Weather data is cached for **30 minutes**
- Automatic fallback to cache when network is unavailable
- Old cache entries are automatically cleaned up
- "Last updated" timestamp tracking

### Theme Support

The app automatically detects system theme preference:

- **Light Mode**: Bright, clean interface with blue accents
- **Dark Mode**: Dark background with enhanced contrast
- Uses Material Design 3 dynamic theming

## 🎨 Screenshots

### Light Theme
| Home Screen | 5-Day Forecast | Search |
|-------------|----------------|--------|
| ![Light Home](screenshots/light_home.png) | ![Light Forecast](screenshots/light_forecast.png) | ![Search](screenshots/search.png) |

### Dark Theme
| Home Screen | 5-Day Forecast | Error State |
|-------------|----------------|-------------|
| ![Dark Home](screenshots/dark_home.png) | ![Dark Forecast](screenshots/dark_forecast.png) | ![Error](screenshots/error.png) |

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Coding Standards

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Write unit tests for new features

## 🙏 Acknowledgments

- [WeatherAPI.com](https://www.weatherapi.com/) - Weather data provider
- [LottieFiles](https://lottiefiles.com/) - Animation resources
- [Material Design 3](https://m3.material.io/) - Design system
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI framework


## ⭐ Support

If you found this project helpful, please give it a ⭐️!

---

Made with ❤️ and ☕ by Astroid-Destroyer-Dev
