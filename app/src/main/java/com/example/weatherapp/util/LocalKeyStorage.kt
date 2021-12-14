package com.example.weatherapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.R

class LocalKeyStorage(context: Context) {
    private var prefs: SharedPreferences? =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val FAHRENHEIT = "isFahrenheit"
        const val latitude = "latitude"
        const val longitude = "longitude"
        const val cityName = "cityName"
    }

    fun saveValue(key: String, value: String) {
        val editor = prefs?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun getValue(key: String): String? {
        return when(key) {
           "isFahrenheit" -> prefs?.getString(key, "false")
           "latitude" -> prefs?.getString(key, null)
           "longitude" -> prefs?.getString(key, null)
           "cityName" -> prefs?.getString(key, null)
            else -> prefs?.getString(key, null)
        }
    }

    fun deleteValue(key: String) {
        val editor = prefs?.edit()
        editor?.putString(key, null)
        editor?.apply()
    }
}