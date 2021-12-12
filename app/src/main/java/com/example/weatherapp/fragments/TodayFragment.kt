package com.example.weatherapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.TodayWeatherRecyclerAdapter
import com.example.weatherapp.dataClass.CityName
import com.example.weatherapp.dataClass.HourlyWeatherListType
import com.example.weatherapp.databinding.FragmentTodayBinding
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.util.InternetConnectivity
import com.example.weatherapp.util.LocalKeyStorage
import com.example.weatherapp.util.hideKeyboard
import com.example.weatherapp.view.MainActivity
import com.example.weatherapp.viewModel.MainViewModel
import com.google.android.gms.location.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodayFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var todayWeatherAdapter: TodayWeatherRecyclerAdapter
    var todayWeatherHourly: ArrayList<HourlyWeatherListType> = ArrayList()
    var tomorrowWeatherHourly: ArrayList<HourlyWeatherListType> = ArrayList()
    lateinit var localKeyStorage: LocalKeyStorage
    private var mLastLocation: Location? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    lateinit var response: Call<List<CityName>>
    private val repoInstance = WeatherRepository(Application())
    lateinit var cityName : String
    private val ctx = this

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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

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
        requireActivity().findViewById<ImageView>(R.id.myLocationBtn).setOnClickListener {
            getMyLocation()
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
        requireActivity().findViewById<ImageView>(R.id.myLocationBtn).visibility = View.VISIBLE
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar).navigationIcon =
            resources.getDrawable(R.drawable.ic_icon)
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar).title =
            null
    }

    @SuppressLint("MissingPermission")
    fun getMyLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation
                    .addOnCompleteListener { task ->
                        mLastLocation = task.result
                        if (mLastLocation != null) {
                            val lat = mLastLocation!!.latitude.toString()
                            val lon = mLastLocation!!.longitude.toString()
                            viewModel.getCityName(lat, lon).observe(viewLifecycleOwner, Observer {
                                if(it != null) {
                                    cityName = it[0].name
                                    Log.d("batao" , " hello to $cityName")
                                    localKeyStorage.saveValue(LocalKeyStorage.latitude, lat)
                                    localKeyStorage.saveValue(LocalKeyStorage.longitude, lon)
                                    Log.d("batao" , " hello to $cityName people")
                                    localKeyStorage.saveValue(LocalKeyStorage.cityName, cityName)
                                    val view = ctx.requireActivity().findViewById<TextView>(R.id.txtlocation)
                                    view.text = localKeyStorage.getValue(LocalKeyStorage.cityName)
                                    findNavController().navigate(R.id.action_homeFragment_self)
                                }
                            })

                            Log.d("values", " latlon $lat $lon")
                        } else {
                            Log.d("values", "location null and call requestNewLocationData")
                            requestNewLocationData()
                        }
                    }
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setCancelable(false)
                    .setTitle("Enable Location ?")
                    .setMessage("Let us help apps determine location")
                    .setNeutralButton("CANCEL") { dialog, which ->
                    }
                    .setPositiveButton("Ok") { dialog, which ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startForResult.launch(intent)
                    }
                    .show()
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    // if permission is not given we request for it.
    private fun requestPermissions() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    // here is the result of the request.
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
                Log.d("values", "on result permission granted")
                getMyLocation()

            } else {
                Log.d("values", "on result permission denied")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            val lat1 = mLastLocation.latitude.toString()
            val lon1 = mLastLocation.longitude.toString()
            localKeyStorage.saveValue(LocalKeyStorage.latitude, lat1)
            localKeyStorage.saveValue(LocalKeyStorage.longitude, lon1)
            Log.d("cityname" , "CityName")
            Log.d("values", " lat1lon1 $lat1 $lon1")
        }
    }

    // check if location is turned on or not of the device.
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                getMyLocation()
                Log.d("hogyaa", "if part")
            }else{
                getMyLocation()
                Log.d("hogyaa", "else part")
            }
        }

}