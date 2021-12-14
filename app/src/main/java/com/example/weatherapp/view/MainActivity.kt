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
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.fragments.TodayFragment
import com.example.weatherapp.fragments.WrapperFragment
import com.example.weatherapp.util.LocalKeyStorage
import com.example.weatherapp.viewModel.LocationViewModel
import com.example.weatherapp.viewModel.MainViewModel
import com.google.android.gms.location.*
//import com.example.weatherapp.utils.permissionUtils
import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController


        navController.addOnDestinationChangedListener{_, destination, _ ->

            if (destination.id == R.id.aboutFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


            } else if (destination.id == R.id.feedbackFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            }  else if (destination.id == R.id.locationFragment) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            }else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }
        setSupportActionBar(topAppBar)
        val actionBar = supportActionBar
        actionBar?.title = " "
        txtlocation.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_locationFragment)
        }
        icsrch.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_locationFragment)
        }
        localKeyStorage = LocalKeyStorage(this)
        binding.txtlocation.text = localKeyStorage.getValue(LocalKeyStorage.cityName)

        //Navigation Drawer Toggle
        val drawerToggle : ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            topAppBar,
            (R.string.open),
            (R.string.close)
        ){

        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        val menu = binding.navView.menu
        val menuItem = menu.findItem(R.id.conversion)
//        if(menuItem.isChecked){
//            localKeyStorage.saveValue(LocalKeyStorage.FAHRENHEIT , "true")
//        }
//        else{
//            localKeyStorage.saveValue(LocalKeyStorage.FAHRENHEIT , "false")
//        }

//        val view = MenuItemCompat.getActionView(menuItem)
        val view = menuItem.actionView
        val switch : SwitchCompat = view.findViewById(R.id.switch_id)
        switch.isChecked = localKeyStorage.getValue("isFahrenheit") == "true"
        switch.setOnCheckedChangeListener { _, isChecked ->

            localKeyStorage.saveValue(LocalKeyStorage.FAHRENHEIT , isChecked.toString())

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
            R.id.license ->{
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))

            }
            R.id.feedback ->{

                navController.navigate(R.id.action_homeFragment_to_feedbackFragment)
                drawerLayout.close()

            }
//            else -> Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
        }
        return true
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}