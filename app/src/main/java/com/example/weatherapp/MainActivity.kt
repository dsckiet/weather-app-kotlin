package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.NavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(topAppBar)
        val actionBar = supportActionBar
        actionBar?.title = "Weather App"

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

            }
            R.id.license ->{
                Toast.makeText(this,"Currently not working", Toast.LENGTH_SHORT).show()

            }
            R.id.feedback ->{
                navController.navigate(R.id.action_homeFragment_to_feedbackFragment)
            }
            else -> Toast.makeText(this,"Error", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}