package com.example.weatherapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.dataclass.Locations
import com.example.weatherapp.dataclass.LocationsItem
import com.example.weatherapp.dataclass.Test
import com.example.weatherapp.repository.LocationRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel constructor(application: Application) : AndroidViewModel(application) {


    val repoInstance = LocationRepo(application)
    lateinit var city: Call<Locations>
    val cityName = MutableLiveData<Locations>()
    lateinit var name : MutableLiveData<Test>

    init {
        Log.d("flow", "init")
    }

    fun getCity(cities : String): MutableLiveData<Locations> {
        this.city = repoInstance.getServicesApiCall(cities)
        city.enqueue(object : Callback<Locations> {

            override fun onResponse(
                call: Call<Locations>,
                response: Response<Locations>
            ) {
//                val city = response.body()
//                cityName as MutableList<Locations>
                cityName.value = response.body()
//                cityName.value = Locations()
                Log.d("error", cityName.value.toString())
            }

            override fun onFailure(call: Call<Locations>, t: Throwable) {
                Log.d("err", "Failure", t)
            }



        })

        return cityName
    }

}

