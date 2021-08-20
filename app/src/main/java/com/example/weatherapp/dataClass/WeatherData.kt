package com.example.weatherapp.dataClass

data class WeatherData(
    val hourlyWeatherList: List<HourlyWeatherListType>,
    val temp: String,
    val feelsLikeTemp: String,
    val current: Current
)
