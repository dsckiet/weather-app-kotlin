package com.example.weatherapp.dataclass

data class SearchLocationItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String
)