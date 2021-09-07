package com.example.weatherapp.dataClass

data class DaysWeatherListType(
    val date: String,
    val sunriset: String,
    val humidity: String,
    val wind_speed_degree: String,
    val pop: String,
    val clouds: String,
    val description: String,
    val weatherIcon : String,
    val maxTemp : String,
    val minTemp : String,
    var expandable:Boolean = false
)
