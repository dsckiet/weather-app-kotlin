package com.example.weatherapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.dataClass.SearchLocationsItem
import com.example.weatherapp.repository.LocationRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel constructor(application: Application) : AndroidViewModel(application) {



    val repoInstance = LocationRepo(application)
    lateinit var city: Call<List<SearchLocationsItem>>
    val cityName = MutableLiveData<List<SearchLocationsItem>>()

    init {

        Log.d("flow", "init")

    }

    fun getCityName(cities : String): MutableLiveData<List<SearchLocationsItem>> {
        this.city = repoInstance.getServicesApiCall(cities)
        city.enqueue(object : Callback<List<SearchLocationsItem>> {

            override fun onResponse(
                call: Call<List<SearchLocationsItem>>,
                response: Response<List<SearchLocationsItem>>
            ) {
//                val city = response.body()
                Log.d("rsp", response.toString())
//                if(city!=null){
//                    val name = (city.name).toString()
//                    val lat = (city.lat).toDouble()
//                    val lon = (city.lon).toDouble()
////                    cityName.value = Values(name, lat, lon)
//                }
                cityName.value = response.body()
            }

            override fun onFailure(call: Call<List<SearchLocationsItem>>, t: Throwable) {
                Log.d("err", "Failure", t)
            }
        })

        return cityName
    }

}


