package com.example.weatherapp.network

import android.location.Location
import com.example.weatherapp.dataClass.MainWeather
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val base_url: String = "https://api.openweathermap.org/data/2.5/"
const val api_key: String = "fb47fa398ad290f6e16e655512d6e8d5"

//https://api.openweathermap.org/data/2.5/onecall?lat=29.2773&lon=77.7338&appid=fb47fa398ad290f6e16e655512d6e8d5&units=metric&exclude=minutely

interface RetroInstance {

    @GET("onecall?appid=$api_key&exclude=minutely")
    fun getWeather(@Query("units") unit : String, @Query("lat") latitude : Float, @Query("lon") longitude : Float): Call<MainWeather>

}

object RetroService{
      val retroInstance : RetroInstance
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        retroInstance = retrofit.create(RetroInstance::class.java)

    }
}