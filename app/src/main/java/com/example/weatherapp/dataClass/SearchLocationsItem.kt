package com.example.weatherapp.dataClass

data class SearchLocationsItem(

    val country: String? = "",
    val lat: Double? = 0.0,
    val localNames: LocalNamesX? = LocalNamesX(),
    val lon: Double? = 0.0,
    val name: String? = ""
)
