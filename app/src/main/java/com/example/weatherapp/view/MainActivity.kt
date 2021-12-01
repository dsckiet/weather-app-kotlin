package com.example.weatherapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.ActionMenuView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.fragments.TodayFragment
import com.example.weatherapp.util.LocalKeyStorage
import com.example.weatherapp.viewModel.LocationViewModel
import com.example.weatherapp.viewModel.MainViewModel
import com.google.android.gms.location.*
//import com.example.weatherapp.utils.permissionUtils
//import com.google.android.gms.location.LocationRequest
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    var mLastLocation: Location? = null
    lateinit var localKeyStorage: LocalKeyStorage
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController


        setSupportActionBar(topAppBar)
        val actionBar = supportActionBar
        actionBar?.title = " "

//        if (!checkPermissions()) {
//            requestPermissions()
//        } else {
//            getLastLocation()
//        }
     //   getLastLocation()

        //on click of location text
        txtlocation.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_locationFragment)
        }
        icsrch.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_locationFragment)
        }
        localKeyStorage = LocalKeyStorage(this)
        binding.txtlocation.text = localKeyStorage.getValue(LocalKeyStorage.cityName)

        //Navigation Drawer Toggle
        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            topAppBar,
            (R.string.open),
            (R.string.close)
        ) {

        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        val switchh: SwitchCompat = findViewById(R.id.conSwitch)
        switchh.isChecked = localKeyStorage.getValue("isFahrenheit") == "true"
        switchh.setOnCheckedChangeListener { _, isChecked ->

            localKeyStorage.saveValue(LocalKeyStorage.FAHRENHEIT, isChecked.toString())
            Log.d("togglemain", isChecked.toString())
            navController.navigate(R.id.action_homeFragment_self)
        }
    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.about -> {

                navController.navigate(R.id.action_homeFragment_to_aboutFragment)
                drawerLayout.close()

            }
            R.id.license -> {
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))

            }
            R.id.feedback -> {

                navController.navigate(R.id.action_homeFragment_to_feedbackFragment)
                drawerLayout.close()

            }
            else -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
        return true
    }

//    @SuppressLint("MissingPermission")
//    private fun getLastLocation() {
//        if(checkPermissions()){
//            if (isLocationEnabled()){
//                mFusedLocationClient!!.lastLocation
//                    .addOnCompleteListener(this) { task ->
//                        mLastLocation = task.result
//                        if (mLastLocation != null) {
//                            val lat = mLastLocation!!.latitude.toString()
//                            val lon = mLastLocation!!.longitude.toString()
//                            localKeyStorage.saveValue(LocalKeyStorage.latitude , lat)
//                            localKeyStorage.saveValue(LocalKeyStorage.longitude , lon)
//                            localKeyStorage.saveValue(LocalKeyStorage.cityName , "testing")
//                            Log.d("values", " latlon $lat $lon")
//                        } else {
//                            Log.d("values", "location null and call requestNewLocationData")
//                            requestNewLocationData()
//                        }
//                    }
//            } else {
//                Log.d("values", "location off")
//
//                Toast.makeText(this, "Please Turn on location", Toast.LENGTH_LONG).show()
//                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                startActivity(intent)
//
//            }
//        }else {
//            requestPermissions()
//        }
//    }
//
//    private fun checkPermissions(): Boolean {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            return true
//        }
//        return false
//    }
//
//    // if permission is not given we request for it.
//    private fun requestPermissions() {
//        ActivityCompat.requestPermissions(
//            this@MainActivity,
//            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
//            REQUEST_PERMISSIONS_REQUEST_CODE
//        )
//    }
//
//    // here is the result of the request.
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Granted. Start getting the location information
//                Log.d("values", "on result permission granted")
//                getLastLocation()
//                startActivity(Intent(this,MainActivity::class.java))
//                finish()
//                overridePendingTransition(0, 0);
//
//            } else {
//                Log.d("values", "on result permission denied")
//                requestPermissions()
//            }
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun requestNewLocationData() {
//        val mLocationRequest = LocationRequest()
//        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        mLocationRequest.interval = 0
//        mLocationRequest.fastestInterval = 0
//        mLocationRequest.numUpdates = 1
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        mFusedLocationClient!!.requestLocationUpdates(
//            mLocationRequest, mLocationCallback,
//            Looper.myLooper()
//        )
//    }
//
//    private val mLocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            val mLastLocation: Location = locationResult.lastLocation
//            val lat1 = mLastLocation.latitude.toString()
//            val lon1 = mLastLocation.longitude.toString()
//            localKeyStorage.saveValue(LocalKeyStorage.latitude , lat1)
//            localKeyStorage.saveValue(LocalKeyStorage.longitude , lon1)
//            Log.d("values", " lat1lon1 $lat1 $lon1")
//        }
//    }
//
//    // check if location is turned on or not of the device.
//    private fun isLocationEnabled(): Boolean {
//        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//            LocationManager.NETWORK_PROVIDER
//        )
//    }
//    companion object {
//        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
//    }
}