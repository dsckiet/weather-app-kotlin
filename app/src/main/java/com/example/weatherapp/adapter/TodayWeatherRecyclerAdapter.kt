package com.example.weatherapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.dataClass.HourlyWeatherListType

class TodayWeatherRecyclerAdapter(private val context: Context) :
    RecyclerView.Adapter<TodayWeatherRecyclerAdapter.WeatherViewHolder>() {
    //    private var items: List<Story> = ArrayList()
    var listOfWeatherHourly: ArrayList<HourlyWeatherListType> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.today_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val indexOfList = listOfWeatherHourly[position]
        holder.temperatureText.text = indexOfList.temp
        holder.timeText.text = indexOfList.time
        Glide.with(context).load(indexOfList.image).into(holder.weatherImage)
    }

    override fun getItemCount(): Int {
        return listOfWeatherHourly.size
    }

    class WeatherViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherImage: ImageView = itemView.findViewById(R.id.weather_image)
        val temperatureText: TextView = itemView.findViewById(R.id.temperatureText)
        val timeText: TextView = itemView.findViewById(R.id.timeText)
    }

    fun setWeather(weather: ArrayList<HourlyWeatherListType>) {
        this.listOfWeatherHourly = weather
        Log.d("progress", "NRA")
        notifyDataSetChanged()
    }

}