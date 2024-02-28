package com.example.myweatherapp

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun changeUnixToStandardClock(unixTimestamp: Long, timeZoneOffset: Int): String {
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(unixTimestamp),
        ZoneOffset.ofTotalSeconds(timeZoneOffset)
    )

    val formatter = DateTimeFormatter.ofPattern("h:mm a")

    return localDateTime.format(formatter)
}

fun getIcon(icon: String): Int{
    return when(icon){
        "01d" -> R.drawable.day
        "01n" -> R.drawable.night
        "02d" -> R.drawable.cloudy_day_1
        "02n" -> R.drawable.cloudy_night_1
        "03d" -> R.drawable.cloudy_day_2
        "03n" -> R.drawable.cloudy_night_2
        "04d" -> R.drawable.cloudy_day_3
        "04n" -> R.drawable.cloudy_night_3
        "09d" -> R.drawable.rainy_1
        "09n" -> R.drawable.rainy_4
        "10d" -> R.drawable.rainy_2
        "10n" -> R.drawable.rainy_5
        "11d" -> R.drawable.thunder
        "11n" -> R.drawable.thunder
        "13d" -> R.drawable.snowy_1
        "13n" -> R.drawable.snowy_4
        "50d" -> R.drawable.snowy_6
        "50n" -> R.drawable.snowy_6
        else -> R.drawable.day
    }
}