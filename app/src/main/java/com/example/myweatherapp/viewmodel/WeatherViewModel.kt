package com.example.myweatherapp.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.changeUnixToStandardClock
import com.example.myweatherapp.repo.SimpleWeatherAppRepoImpl
import com.example.myweatherapp.viewmodel.model.WeatherSealedResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: SimpleWeatherAppRepoImpl
): ViewModel() {
    private val _weatherResponse = MutableLiveData<WeatherSealedResponse>()

    val weatherResponse: MutableLiveData<WeatherSealedResponse>
        get() = _weatherResponse

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeather(lat: Double, lon: Double, exclude: String, appid: String, units: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _weatherResponse.postValue(WeatherSealedResponse.Loading(true))
            try {
                val response = repository.getWeather(lat, lon, exclude, appid, units)
                response.hourly.forEach {
                    it.clock = changeUnixToStandardClock(it.dt.toLong(), response.timezoneOffset)
                }
                Log.d("WeatherViewModel", "getWeather: ${response}")
                _weatherResponse.postValue(WeatherSealedResponse.Success(response))
            } catch (e: Exception) {
                _weatherResponse.postValue(WeatherSealedResponse.Error(e.message.toString()))
            }
        }
    }
}