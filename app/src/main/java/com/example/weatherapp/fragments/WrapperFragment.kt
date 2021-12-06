package com.example.weatherapp.fragments

import android.Manifest
import android.R.attr
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
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
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.util.LocalKeyStorage
import com.example.weatherapp.view.MainActivity
import com.example.weatherapp.viewModel.LocationViewModel
import com.example.weatherapp.viewModel.MainViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.NonCancellable.cancel
import android.R.attr.data
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts

import androidx.constraintlayout.motion.widget.Debug.getLocation
import com.example.weatherapp.util.hideKeyboard


class WrapperFragment : Fragment() {

    private var mLastLocation: Location? = null
    lateinit var localKeyStorage: LocalKeyStorage
    lateinit var contextView: View
    private lateinit var MviewModel: MainViewModel
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wrapper, container, false)
        contextView = view.findViewById(R.id.coordinatorLayout)
        MviewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        localKeyStorage = LocalKeyStorage(requireContext())
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        Log.d("hogyaa"," ${localKeyStorage.getValue(LocalKeyStorage.cityName).toString()} hello")
        getLastLocation()
        return view
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled() && (localKeyStorage.getValue(LocalKeyStorage.latitude) == null || localKeyStorage.getValue(
                    LocalKeyStorage.longitude
                ) == null)
            ) {
                Log.d("hogyaa"," ${localKeyStorage.getValue(LocalKeyStorage.cityName).toString()} hello")
                mFusedLocationClient!!.lastLocation
                    .addOnCompleteListener { task ->
                        mLastLocation = task.result
                        if (mLastLocation != null) {
                            val lat = mLastLocation!!.latitude.toString()
                            val lon = mLastLocation!!.longitude.toString()
                            val cityName = MviewModel.getCityName(lat, lon)
                            localKeyStorage.saveValue(LocalKeyStorage.latitude, lat)
                            localKeyStorage.saveValue(LocalKeyStorage.longitude, lon)
                            localKeyStorage.saveValue(LocalKeyStorage.cityName, cityName)
                            val view = requireActivity().findViewById<TextView>(R.id.txtlocation)
                            view.text = localKeyStorage.getValue(LocalKeyStorage.cityName)
                            hideKeyboard(requireActivity())
                            findNavController().navigate(R.id.action_wrapperFragment_to_homeFragment)
                            Log.d("values", " latlon $lat $lon")
                        } else {
                            Log.d("values", "location null and call requestNewLocationData")
                            requestNewLocationData()
                        }
                    }
            } else {
                if (localKeyStorage.getValue(LocalKeyStorage.latitude) == null || localKeyStorage.getValue(
                        LocalKeyStorage.longitude
                    ) == null
                ) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setCancelable(false)
                        .setTitle("Enable Location ?")
                        .setMessage("Let us help apps determine location")
                        .setNeutralButton("CANCEL") { dialog, which ->
                            findNavController().navigate(R.id.action_wrapperFragment_to_searchFragment)
                        }
                        .setPositiveButton("Ok") { dialog, which ->
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startForResult.launch(intent)
                        }
                        .show()
                } else {
                    findNavController().navigate(R.id.action_wrapperFragment_to_homeFragment)
                }

            }
        } else {
            if (localKeyStorage.getValue(LocalKeyStorage.latitude) == null || localKeyStorage.getValue(
                    LocalKeyStorage.longitude
                ) == null
            ) {
                requestPermissions()
            } else {
                findNavController().navigate(R.id.action_wrapperFragment_to_homeFragment)
            }
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
                getLastLocation()

            } else {
                Log.d("values", "on result permission denied")
                // go to search fragment
                findNavController().navigate(R.id.action_wrapperFragment_to_searchFragment)
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
        private const val REQUEST_CHECK_SETTINGS = 36
    }

    @SuppressLint("CutPasteId")
    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<TextView>(R.id.txtlocation).visibility = View.GONE
        requireActivity().findViewById<ImageView>(R.id.icsrch).visibility = View.GONE
        requireActivity().findViewById<SwitchCompat>(R.id.conSwitch).visibility = View.GONE
        requireActivity().findViewById<ImageView>(R.id.myLocationBtn).visibility = View.GONE
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar).navigationIcon =
            null
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar).title =
            "Weather App"
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.topAppBar)
            .setTitleTextColor(Color.WHITE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("hogyaa", "else part")
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> if (resultCode == Activity.RESULT_OK) {
                getLastLocation()
            } else {
                Log.d("hogyaa", "else part")
            }
            else -> super.onActivityResult(requestCode, resultCode,data)
        }
    }
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                getLastLocation()
                Log.d("hogyaa", "if part")
            }else{
                getLastLocation()
                Log.d("hogyaa", "else part")
            }
        }
}