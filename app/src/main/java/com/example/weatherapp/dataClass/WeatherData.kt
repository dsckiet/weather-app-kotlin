package com.example.weatherapp.dataClass

data class WeatherData(
    val todayHourly: MutableList<HourlyWeatherListType>,
    val tomorrowHourly: MutableList<HourlyWeatherListType>,
    val temp: String,
    val feelsLikeTemp: String,
    val current: Current
)

data class WeatherDataDays(
    val data: MutableList<DaysWeatherListType>
)
