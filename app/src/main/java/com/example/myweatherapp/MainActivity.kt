package com.example.myweatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.databinding.ActivityMainBinding
import com.example.myweatherapp.recyclerview.HourlyCard
import com.example.myweatherapp.viewmodel.WeatherViewModel
import com.example.myweatherapp.viewmodel.model.WeatherSealedResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val weatherViewModel: WeatherViewModel by viewModels()
    private var latidude: Double = 0.0
    private var longitude: Double = 0.0
    private var cityName: String = ""
    val PERMISSION_ID = 1010

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherViewModel.weatherResponse.observe(this){
            when(val data = it){
                is WeatherSealedResponse.Success -> {
                    val hourlyRecyclerView: RecyclerView = findViewById(R.id.hourlyRecyclerView)
                    hourlyRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
                    val cardView = HourlyCard(data.data.hourly)
                    hourlyRecyclerView.adapter = cardView

                    val wind = "wind        : ${data.data.current.windSpeed} m/s"
                    val pressure = "pressure : ${data.data.current.pressure} hPa"
                    val visibility = "visibility  : ${data.data.current.visibility/1000} km"
                    
                    val cityText = findViewById<TextView>(R.id.city_text)
                    val temperatureText = findViewById<TextView>(R.id.temperature_text)
                    val clock = findViewById<TextView>(R.id.clock)
                    val weatherIcon = findViewById<ImageView>(R.id.weather_icon)
                    val windText = findViewById<TextView>(R.id.wind_text)
                    val paText = findViewById<TextView>(R.id.pa_text)
                    val visibilityText = findViewById<TextView>(R.id.visibility_text)

                    cityText.text = cityName
                    temperatureText.text = "${data.data.current.temp}°C"
                    clock.text = changeUnixToStandardClock(data.data.current.dt.toLong(),data.data.timezoneOffset)
                    weatherIcon.setImageResource(getIcon(data.data.current.weather[0].icon))
                    windText.text = wind
                    paText.text = pressure
                    visibilityText.text = visibility
                }
                is WeatherSealedResponse.Error -> {
                    Log.d("Debug:","Error ${it}")
                }
                is WeatherSealedResponse.Loading -> {
                    Log.d("Debug:","Loading")
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        RequestPermission()
    }


    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient!!.requestLocationUpdates(
                locationRequest,locationCallback, Looper.myLooper()
            )
            return
        }

    }


    private val locationCallback = object : LocationCallback(){
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation
            latidude = lastLocation.latitude
            longitude = lastLocation.longitude
            cityName = getCityName(latidude,longitude)
            weatherViewModel.getWeather(
                latidude,
                longitude,
                "minutely,daily,alerts",
                getString(R.string.code),
                "metric"
            )
        }
    }

    fun RequestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
                NewLocationData()
            }
        }
    }

    private fun getCityName(lat: Double,long: Double):String{
        var cityName = ""
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)

        cityName = Adress!!.get(0).subAdminArea
        return cityName
    }



}