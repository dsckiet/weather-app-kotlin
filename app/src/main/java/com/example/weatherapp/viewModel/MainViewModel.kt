package com.example.newzify.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.newzify.repository.WeatherRepository
import com.example.weatherapp.dataClass.MainWeather
import com.example.weatherapp.dataClass.HourlyWeatherListType
import com.example.weatherapp.dataClass.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel constructor(application: Application) : AndroidViewModel(application) {

    val repoInstance = WeatherRepository(application)
    lateinit var weather: Call<MainWeather>
    val weather_hourly = MutableLiveData<WeatherData>()
    var listOfWeatherHourly: MutableList<HourlyWeatherListType> = mutableListOf()
    var flag = true

    fun getWeatherHourly(): MutableLiveData<WeatherData> {
        this.weather = repoInstance.getServicesApiCall()
        listOfWeatherHourly.clear()
        weather.enqueue(object : Callback<MainWeather> {
            override fun onResponse(call: Call<MainWeather>, response: Response<MainWeather>) {
                val weather = response.body()
                if (weather != null) {

                    val hourly = weather.hourly
                    val currentTemp = (weather.current.temp).toInt().toString()
                    val feelsLikeTemp = (weather.current.feels_like).toInt().toString()

                    for (element in hourly) {
                        val temperature = (element.temp).toInt()
                        val index2 = element.weather
                        val icon = index2[0].icon
                        val weatherImage = "https://openweathermap.org/img/wn/$icon@2x.png"
                        // val unixTime = (index.dt).toLong()
                        val time = SimpleDateFormat("h:mm a", Locale.ENGLISH).format(
                            Date((element.dt).toLong() * 1000))
                        if(time != "6:30 AM"){
                            flag = true
                        }
                        else{
                           break
                        }

                        Log.d("progressing", "weather list = ${time.toString()}")
                        val listElements =
                            HourlyWeatherListType("$temperature°C", weatherImage, time.toString())
                        listOfWeatherHourly.add(listElements)
                    }
                    weather_hourly.value =
                        WeatherData(
                            listOfWeatherHourly,
                            "$currentTemp°C",
                            "Feels Like $feelsLikeTemp°",
                            weather.current
                        )
                }
            }

            override fun onFailure(call: Call<MainWeather>, t: Throwable) {
                Log.d("batao", "Error in fetching", t)
            }
        })
        return weather_hourly
    }
}
