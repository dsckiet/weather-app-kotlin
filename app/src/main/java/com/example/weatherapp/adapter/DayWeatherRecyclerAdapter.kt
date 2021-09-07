package com.example.weatherapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.dataClass.DaysWeatherListType

class DayWeatherRecyclerAdapter(private val context: Context) :
    RecyclerView.Adapter<DayWeatherRecyclerAdapter.WeatherViewHolder>() {

    var listOfWeatherDataDaily: ArrayList<DaysWeatherListType> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.days_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val indexOfList = listOfWeatherDataDaily[position]
        holder.date.text = indexOfList.date
        holder.description.text = indexOfList.description
        holder.maxTemp.text = indexOfList.maxTemp
        holder.minTemp.text = indexOfList.minTemp
        holder.rain.text = indexOfList.pop
        holder.cloud.text = indexOfList.clouds
        holder.humidity.text = indexOfList.humidity
        holder.wind.text = indexOfList.wind_speed_degree
        holder.sunriset.text = indexOfList.sunriset
        Glide.with(context).load(indexOfList.weatherIcon).into(holder.weatherImage)

        val isExpandable :Boolean = indexOfList.expandable
        holder.expandableLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE
        holder.relativeLayout.setOnClickListener {
            indexOfList.expandable = !indexOfList.expandable
            notifyItemChanged(position)
        }


    }

    override fun getItemCount(): Int {
        return listOfWeatherDataDaily.size
    }

    class WeatherViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val description: TextView = itemView.findViewById(R.id.description)
        val maxTemp: TextView = itemView.findViewById(R.id.maxTemp)
        val minTemp: TextView = itemView.findViewById(R.id.minTemp)
        val rain: TextView = itemView.findViewById(R.id.rain)
        val cloud: TextView = itemView.findViewById(R.id.cloudiness)
        val humidity: TextView = itemView.findViewById(R.id.humidity)
        val wind: TextView = itemView.findViewById(R.id.wind)
        val sunriset: TextView = itemView.findViewById(R.id.sunRiset)
        val weatherImage: ImageView = itemView.findViewById(R.id.image)
        var expandableLayout : TableLayout = itemView.findViewById(R.id.expandableLayout)
        var relativeLayout : RelativeLayout = itemView.findViewById(R.id.relativeLayout)
    }

    fun setWeather(weather: ArrayList<DaysWeatherListType>) {
        this.listOfWeatherDataDaily = weather
        Log.d("progress", "NRA")
        notifyDataSetChanged()
    }
}