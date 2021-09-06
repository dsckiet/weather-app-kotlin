package com.example.weatherapp.dataclass


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocationsItem(
    @Json(name = "country")
    val country: String? = "",
    @Json(name = "lat")
    val lat: Double? = 0.0,
    @Json(name = "local_names")
    val localNames: LocalNamesX? = LocalNamesX(),
    @Json(name = "lon")
    val lon: Double? = 0.0,
    @Json(name = "name")
    val name: String? = ""
)