package com.example.myweatherapp.repo

import com.example.myweatherapp.api.SimpleWeatherAppAPI
import com.example.myweatherapp.api.model.WeatherResponse
import javax.inject.Inject

class SimpleWeatherAppRepoImpl @Inject constructor(
    private val api: SimpleWeatherAppAPI
): SimpleWeatherAppRepo {
    override suspend fun getWeather(
        lat: Double, lon: Double,
        exclude: String, appid: String,
        units: String
    ) : WeatherResponse {
        return api.getWeather(lat, lon, exclude, appid, units)
    }
}