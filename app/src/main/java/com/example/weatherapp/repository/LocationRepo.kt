package com.example.weatherapp.repository

import android.app.Application
import com.example.weatherapp.Interface.RetroService
import com.example.weatherapp.dataClass.Locations

import retrofit2.Call



class LocationRepo constructor(val application: Application) {

    fun getServicesApiCall(name : String) : Call<Locations> {
        return RetroService.LocationInstance.getCity(name)
    }
}

//class LocationRepo (var context : Context) {
//    suspend fun getName(name: String): Call<SearchLocationItem> {
//        return RetroService.LocationInstance.getCity(name)
//    }
//}
