package com.example.weatherapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.dataClass.*
import com.example.weatherapp.repository.WeatherRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel constructor(application: Application) : AndroidViewModel(application) {

    private val TAG = "mainViewModel"
    private val repoInstance = WeatherRepository(application)
    lateinit var weather: Call<MainWeather>
    lateinit var response: Call<List<CityName>>
    val weather_hourly = MutableLiveData<WeatherData>()
    val weather_daily = MutableLiveData<WeatherDataDays>()
    var daysData: MutableList<DaysWeatherListType> = mutableListOf()
    var todayHourly: MutableList<HourlyWeatherListType> = mutableListOf()
    var tomorrowHourly: MutableList<HourlyWeatherListType> = mutableListOf()
    var testArray: MutableList<HourlyWeatherListType> = mutableListOf()
    var tempIndex = -1
    val isLoading = MutableLiveData<Boolean>()
    private val isInternet = MutableLiveData<Boolean>()
    val isLoadingMain = MutableLiveData<Boolean>()
    lateinit var temperatureUnit: String
    lateinit var windSpeedUnit: String
    private var apiQueryUnit: String = ""
    var cityName = MutableLiveData<List<CityName>>()

    fun getWeatherHourly(
        isFahrenheit: String?,
        lat: String?,
        lon: String?
    ): MutableLiveData<WeatherData> {
        isLoadingMain.value = true
        apiQueryUnit = if (isFahrenheit == "true") "imperial" else "metric"
        this.weather = repoInstance.getServicesApiCall(apiQueryUnit, lat, lon)
        weather.enqueue(object : Callback<MainWeather> {
            override fun onResponse(call: Call<MainWeather>, response: Response<MainWeather>) {
                val weather = response.body()
                if (weather != null) {

                    temperatureUnit = if (isFahrenheit == "true") {
                        "°F"
                    } else "°C"

                    val hourly = weather.hourly
                    val currentTemp = (weather.current.temp).toInt().toString()
                    val feelsLikeTemp = (weather.current.feels_like).toInt().toString()

                    testArray.clear()
                    tomorrowHourly.clear()
                    todayHourly.clear()
                    for (element in hourly) {
                        val temperature = (element.temp).toInt()
                        val index2 = element.weather
                        val icon = index2[0].icon
                        val weatherImage = "https://openweathermap.org/img/wn/$icon@2x.png"
                        val time = SimpleDateFormat("h:mm a", Locale.ENGLISH).format(
                            Date((element.dt).toLong() * 1000)
                        )
                        val listElements =
                            HourlyWeatherListType(
                                temperature.toString() + temperatureUnit,
                                weatherImage,
                                time.toString()
                            )
                        testArray.add(listElements)
                    }
                    Log.d(TAG, testArray.toString())

                    setData()

                    //send data to fragment
                    weather_hourly.value =
                        WeatherData(
                            todayHourly,
                            tomorrowHourly,
                            currentTemp + temperatureUnit,
                            "Feels Like $feelsLikeTemp°",
                            weather.current
                        )
                    isLoadingMain.value = false
                }
            }

            override fun onFailure(call: Call<MainWeather>, t: Throwable) {
                Log.d("batao", "Error in fetching", t)
            }
        })
        return weather_hourly
    }

    fun getSevenDayWeather(
        isFahrenheit: String?,
        lat: String?,
        lon: String?
    ): MutableLiveData<WeatherDataDays> {
        isLoading.value = true
        apiQueryUnit = if (isFahrenheit == "true") "imperial" else "metric"
        this.weather = repoInstance.getServicesApiCall(apiQueryUnit, lat, lon)
        weather.enqueue(object : Callback<MainWeather> {
            override fun onResponse(call: Call<MainWeather>, response: Response<MainWeather>) {
                val weather = response.body()
                if (weather != null) {

                    windSpeedUnit = if (isFahrenheit == "true") {
                        "mi/h"
                    } else "m/s"

                    val daily = weather.daily
                    for (element in daily) {
                        val date = SimpleDateFormat("EEEE, d MMM", Locale.ENGLISH).format(
                            Date((element.dt).toLong() * 1000)
                        )
                        val sunrise = SimpleDateFormat("h:mm a", Locale.ENGLISH).format(
                            Date((element.sunrise).toLong() * 1000)
                        )
                        val sunset = SimpleDateFormat("h:mm a", Locale.ENGLISH).format(
                            Date((element.sunset).toLong() * 1000)
                        )
                        val pop = "${(element.pop * 100).toInt()}%"
                        val index2 = element.weather
                        val description = index2[0].description
                        val icon = index2[0].icon
                        val weatherIcon = "https://openweathermap.org/img/wn/$icon@2x.png"
                        val weatherDataElement =
                            DaysWeatherListType(
                                date,
                                "$sunrise, $sunset",
                                "${element.humidity}%",
                                "${element.wind_speed.toString() + windSpeedUnit}, ${element.wind_deg}°",
                                pop,
                                "${element.clouds}%",
                                description,
                                weatherIcon,
                                "${element.temp.max.toInt()}°",
                                "${element.temp.min.toInt()}°"
                            )
                        daysData.add(weatherDataElement)
                    }
                    weather_daily.value = WeatherDataDays(daysData)
                    Log.d("daily", weather_daily.value.toString())
                    isLoading.value = false
                }
            }

            override fun onFailure(call: Call<MainWeather>, t: Throwable) {
                Log.d("batao", "Error in fetching", t)
            }
        })
        return weather_daily
    }

    fun getCityName(lat: String?, lon: String?) : MutableLiveData<List<CityName>> {
        this.response = repoInstance.getCityName(lat, lon)
        response.enqueue(object : Callback<List<CityName>> {
            override fun onResponse(call: Call<List<CityName>>, response: Response<List<CityName>>) {
                val response = response.body()
                if (response != null) {
                     cityName.value = response
                }
            }

            override fun onFailure(call: Call<List<CityName>>, t: Throwable) {
                Log.d("batao", "Error in fetching", t)
            }
        })
        return cityName
    }

    fun setData() {
        // For today Hourly data
        for (i in testArray) {
            if (i.time != "6:30 AM") {
                todayHourly.add(i)
                tempIndex = testArray.indexOf(i)
            } else {
                break
            }
        }
        Log.d(TAG, todayHourly.toString())

        // For tomorrow hourly data
        loop@ for (i in testArray) {
            if (testArray.indexOf(i) > tempIndex + 1) {
                for (j in (tempIndex + 2)..testArray.size) {
                    if (testArray[j].time != "6:30 AM") {
                        tomorrowHourly.add(testArray[j])
                    } else {
                        break@loop
                    }
                }
            }
        }
        Log.d(TAG, tomorrowHourly.toString())
    }

    fun isInternet(b: Boolean) {
        isInternet.value = b
        if (!b) {
            isLoadingMain.value = !b
        }
    }
}
