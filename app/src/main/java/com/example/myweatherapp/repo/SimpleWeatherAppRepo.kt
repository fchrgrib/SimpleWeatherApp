package com.example.myweatherapp.repo

import com.example.myweatherapp.api.model.WeatherResponse

interface SimpleWeatherAppRepo {
    suspend fun getWeather(
        lat: Double, lon: Double,
        exclude: String, appid: String,
        units: String
    ): WeatherResponse
}