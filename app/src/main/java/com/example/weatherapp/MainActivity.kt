package com.example.weatherapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.utils.permissionUtils
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(topAppBar)
        val actionBar = supportActionBar
        actionBar?.title = " "

        //on click of location text
        txtlocation.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_locationFragment)
        }

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


    }


    //Implemented Item Selected listener

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId){
            R.id.conversion ->{
                Toast.makeText(this,"Currently not working", Toast.LENGTH_SHORT).show()
            }

            R.id.about ->{
                navController.navigate(R.id.action_homeFragment_to_aboutFragment)
                drawerLayout.close()
                menuItem.setChecked(false)

            }
            R.id.license ->{
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                menuItem.setChecked(false)
            }
            R.id.feedback ->{
                navController.navigate(R.id.action_homeFragment_to_feedbackFragment)
                drawerLayout.close()
                menuItem.setChecked(false)
            }
            else -> Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
        }
        return true
    }
    
}