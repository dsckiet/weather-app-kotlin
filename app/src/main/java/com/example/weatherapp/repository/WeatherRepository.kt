package com.example.newzify.repository

import android.app.Application
import com.example.weatherapp.dataClass.MainWeather
import com.example.weatherapp.network.RetroService
import retrofit2.Call

class WeatherRepository constructor(val application: Application) {

    fun getServicesApiCall() : Call<MainWeather> {
        return RetroService.retroInstance.getWeather(29.2773F, 77.7338F)
    }
}