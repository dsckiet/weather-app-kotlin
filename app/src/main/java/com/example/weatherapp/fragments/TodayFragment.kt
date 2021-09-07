package com.example.weatherapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.TodayWeatherRecyclerAdapter
import com.example.weatherapp.dataClass.HourlyWeatherListType
import com.example.weatherapp.databinding.FragmentTodayBinding
import com.example.weatherapp.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodayFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var todayWeatherAdapter: TodayWeatherRecyclerAdapter
    var todayWeatherHourly: ArrayList<HourlyWeatherListType> = ArrayList()
    var tomorrowWeatherHourly: ArrayList<HourlyWeatherListType> = ArrayList()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding: FragmentTodayBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_today, container, false)
        val view = binding.root

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.lottie.visibility = View.VISIBLE

       viewModel.getWeatherHourly().observe(viewLifecycleOwner, Observer {
           todayWeatherHourly.clear()
           tomorrowWeatherHourly.clear()
           todayWeatherHourly = it.todayHourly as ArrayList<HourlyWeatherListType>
           tomorrowWeatherHourly = it.tomorrowHourly as ArrayList<HourlyWeatherListType>

           binding.currentTemp.text = it.temp
           binding.feelsLikeTemp.text = it.feelsLikeTemp
           binding.sunriseData.text = SimpleDateFormat("h:mm a", Locale.ENGLISH).format(
               Date((it.current.sunrise).toLong() * 1000))
           binding.sunsetData.text = SimpleDateFormat("h:mm a", Locale.ENGLISH).format(
               Date((it.current.sunset).toLong() * 1000))
           binding.humidityData.text = "${it.current.humidity}%"
           binding.uviData.text = it.current.uvi.toString()
           binding.pressureData.text = it.current.pressure.toString()
           binding.windData.text = "${it.current.wind_speed}m/s"
           binding.cloudiness.text = "${it.current.humidity}%"
           binding.dewPoint.text = "${it.current.dew_point.toInt()}°C"
           binding.visible.text = "${it.current.visibility} m"
           binding.windDirection.text = "${it.current.wind_deg}°"

           binding.recyclerV.apply {
               layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
               todayWeatherAdapter = TodayWeatherRecyclerAdapter(requireContext())
               adapter = todayWeatherAdapter
               todayWeatherAdapter.setWeather(todayWeatherHourly)
           }

           binding.recyclerV2.apply {
               layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
               todayWeatherAdapter = TodayWeatherRecyclerAdapter(requireContext())
               adapter = todayWeatherAdapter
               todayWeatherAdapter.setWeather(tomorrowWeatherHourly)
           }

       })




        return view
    }
}