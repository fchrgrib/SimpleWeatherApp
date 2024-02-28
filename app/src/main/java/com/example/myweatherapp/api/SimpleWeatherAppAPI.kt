package com.example.myweatherapp.api

import com.example.myweatherapp.api.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SimpleWeatherAppAPI {
    @GET("data/3.0/onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String,
        @Query("units") units: String,
    ): WeatherResponse
}