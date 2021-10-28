package com.example.weatherapp.repository

import android.app.Application
import com.example.weatherapp.dataClass.MainWeather
import com.example.weatherapp.network.RetroService
import retrofit2.Call

class WeatherRepository constructor(val application: Application) {

    fun getServicesApiCall(apiQueryUnit: String, lat: String?, lon: String?): Call<MainWeather> {
        return RetroService.retroInstance.getWeather(apiQueryUnit, lat!!.toFloat(), lon!!.toFloat())
    }
}