package com.example.weatherapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.LocateAdapter
import com.example.weatherapp.dataClass.SearchLocationsItem
import com.example.weatherapp.databinding.FragmentLocationBinding

import com.example.weatherapp.viewModel.LocationViewModel
//import com.example.weatherapp.dataclass.SearchLocationItem
//import com.example.weatherapp.dataclass.Values
//import com.example.weatherapp.viewModel.LocationViewModel
import retrofit2.Call


class LocationFragment : Fragment(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var LviewModel: LocationViewModel
    private lateinit var locateAdapter: LocateAdapter
    lateinit var city: Call<List<SearchLocationsItem>>
    var location_item : ArrayList<List<SearchLocationsItem>> = ArrayList()
    lateinit var binding: FragmentLocationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
        val view = binding.root

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchView = binding.searchView
        LviewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        val searchRView = binding.recyclerView
        searchRView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        locateAdapter = LocateAdapter(requireContext())
        searchRView.adapter = locateAdapter
//        LviewModel.getCityName("ghaziabad")
        searchView.isIconified = false
//        searchView.setOnClickListener {
//            LviewModel.getCityName("ghaziabad")
//        }
        LviewModel.cityName.observe(viewLifecycleOwner,{
            if(it!=null) {
                locateAdapter.setCity(it[0].name.toString())
//                locateAdapter.setCity(it.toString())
            }

        })
        searchView.setOnClickListener {
//            LviewModel.getCityName()
            searchView.isIconified = false
        }
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        LviewModel.getCityName(query.toString())
        Log.d("error", query.toString())
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        LviewModel.getCityName(newText.toString())
        Log.d("error", "qtc")
        return false
    }

}


