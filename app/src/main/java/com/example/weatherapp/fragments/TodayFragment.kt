package com.example.weatherapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.TodayWeatherRecyclerAdapter
import com.example.weatherapp.dataClass.HourlyWeatherListType
import com.example.weatherapp.databinding.FragmentTodayBinding
import com.example.weatherapp.util.InternetConnectivity
import com.example.weatherapp.util.LocalKeyStorage
import com.example.weatherapp.viewModel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodayFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var todayWeatherAdapter: TodayWeatherRecyclerAdapter
    var todayWeatherHourly: ArrayList<HourlyWeatherListType> = ArrayList()
    var tomorrowWeatherHourly: ArrayList<HourlyWeatherListType> = ArrayList()
    lateinit var localKeyStorage: LocalKeyStorage

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

        localKeyStorage = LocalKeyStorage(requireContext())

        if (InternetConnectivity.isNetworkAvailable(requireContext())) {
            //      val isCelsius = arguments?.getBoolean("isCelsius")
            val isFahrenheit = localKeyStorage.getValue("isFahrenheit")
            val lat = localKeyStorage.getValue("latitude")
            val lon = localKeyStorage.getValue("longitude")
            Log.d("isFahrenheit", isFahrenheit.toString())
            getAndSetData(binding, isFahrenheit, lat, lon)
            viewModel.isInternet(true)
        } else {
            viewModel.isInternet(false)
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
        }

        return view
    }

    private fun getAndSetData(
        binding: FragmentTodayBinding,
        isFahrenheit: String?,
        lat: String?,
        lon: String?
    ) {
        viewModel.getWeatherHourly(isFahrenheit, lat, lon).observe(viewLifecycleOwner, Observer {
            todayWeatherHourly.clear()
            tomorrowWeatherHourly.clear()
            todayWeatherHourly = it.todayHourly as ArrayList<HourlyWeatherListType>
            tomorrowWeatherHourly = it.tomorrowHourly as ArrayList<HourlyWeatherListType>

            binding.currentTemp.text = it.temp
            binding.feelsLikeTemp.text = it.feelsLikeTemp
            binding.sunriseData.text = SimpleDateFormat("h:mm a", Locale.ENGLISH).format(
                Date((it.current.sunrise).toLong() * 1000)
            )
            binding.sunsetData.text = SimpleDateFormat("h:mm a", Locale.ENGLISH).format(
                Date((it.current.sunset).toLong() * 1000)
            )
            binding.uviData.text = it.current.uvi.toString()
            binding.pressureData.text = it.current.pressure.toString()
            binding.humidityData.text = "${it.current.humidity}%"
            if (isFahrenheit == "true") {
                binding.windData.text = "${it.current.wind_speed}mi/h"
                binding.dewPoint.text = "${it.current.dew_point.toInt()}°F"
            } else {
                binding.windData.text = "${it.current.wind_speed}m/s"
                binding.dewPoint.text = "${it.current.dew_point.toInt()}°C"
            }
            binding.cloudiness.text = "${it.current.humidity}%"
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
    }

    @SuppressLint("CutPasteId", "UseCompatLoadingForDrawables")
    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<TextView>(R.id.txtlocation).visibility = View.VISIBLE
        requireActivity().findViewById<ImageView>(R.id.icsrch).visibility = View.VISIBLE
        requireActivity().findViewById<SwitchCompat>(R.id.conSwitch).visibility = View.VISIBLE
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar).navigationIcon =
            resources.getDrawable(R.drawable.ic_icon)
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar).title =
            null
    }
}