package com.example.weatherapp.Interface



import com.example.weatherapp.dataclass.Locations
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASEURL: String = "https://api.openweathermap.org/geo/1.0/"
const val userapi:String = "de4302130ac8b3e82e43d405cfd2c334"

//https://api.openweathermap.org/geo/1.0/direct?q=meerut&limit=5&appid=de4302130ac8b3e82e43d405cfd2c334

interface LocationInterface {

    @GET("/geo/1.0/direct?limit=5&appid=$userapi" )
    fun getCity(@Query("q") name : String): Call<Locations>
}

object RetroService{
    val LocationInstance : LocationInterface
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        LocationInstance = retrofit.create(LocationInterface::class.java)

    }
}

