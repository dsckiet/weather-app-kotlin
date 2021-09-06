package com.example.weatherapp.dataclass


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LocalNamesX(
    @Json(name = "ascii")
    val ascii: String? = "",
    @Json(name = "en")
    val en: String? = "",
    @Json(name = "feature_name")
    val featureName: String? = ""
)