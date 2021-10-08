package com.example.weatherapp.repository

import android.app.Application
import com.example.weatherapp.Interface.RetroService
import com.example.weatherapp.dataClass.SearchLocationsItem

import retrofit2.Call



class LocationRepo constructor(val application: Application) {

    fun getServicesApiCall(name : String) : Call<List<SearchLocationsItem>> {
        return RetroService.LocationInstance.getCity(name)
    }
}