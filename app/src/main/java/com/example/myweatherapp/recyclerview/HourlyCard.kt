package com.example.myweatherapp.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.api.model.Hourly
import com.example.myweatherapp.getIcon

class HourlyCard(private val listHourlyWeather: List<Hourly>): RecyclerView.Adapter<HourlyCard.HourlyCardItem>() {

    inner class HourlyCardItem(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val temperature: TextView = itemView.findViewById(R.id.temperature)
        val time: TextView = itemView.findViewById(R.id.time)
        val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyCardItem {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.hourly_card, parent, false)
        return HourlyCardItem(view)
    }

    override fun getItemCount(): Int {
        return listHourlyWeather.size
    }

    override fun onBindViewHolder(holder: HourlyCardItem, position: Int) {
        holder.temperature.text = "${listHourlyWeather[position].temp}°C"
        holder.time.text = listHourlyWeather[position].clock
        holder.weatherIcon.setImageResource(getIcon(listHourlyWeather[position].weather[0].icon))
    }
}