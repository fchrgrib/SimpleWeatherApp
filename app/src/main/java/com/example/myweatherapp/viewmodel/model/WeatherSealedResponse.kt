package com.example.myweatherapp.viewmodel.model

import com.example.myweatherapp.api.model.WeatherResponse

sealed class WeatherSealedResponse {
    data class Success(val data: WeatherResponse): WeatherSealedResponse()
    data class Error(val error: String): WeatherSealedResponse()
    data class Loading(val isLoading: Boolean): WeatherSealedResponse()
}