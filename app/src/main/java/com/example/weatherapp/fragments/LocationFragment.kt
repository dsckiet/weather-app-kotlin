package com.example.weatherapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.LocateAdapter
import com.example.weatherapp.databinding.FragmentLocationBinding
import com.example.weatherapp.dataclass.LocalNamesX
import com.example.weatherapp.dataclass.Locations
import com.example.weatherapp.dataclass.LocationsItem
import com.example.weatherapp.viewModel.LocationViewModel
import kotlinx.android.synthetic.main.search.*
import retrofit2.Call


class LocationFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var LviewModel: LocationViewModel
    private lateinit var locateAdapter: LocateAdapter
    lateinit var city: Call<LocationsItem>
    var location_item : ArrayList<LocationsItem> = ArrayList()
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

        locateAdapter.notifyDataSetChanged()

        LviewModel.cityName.observe(viewLifecycleOwner,{
            val temp = LocationsItem("ghaziabad",
                0.0,
                LocalNamesX("ghaziabad","ghaziabad","ghaziabad"),
                0.0,
                "ghaziabad")
            location_item.clear()
//            location_item = it.Locations as ArrayList<LocationsItem>
            location_item.add(temp)
//            Log.d("batao", it!!.toString())
            locateAdapter.setCity(location_item)

        })
        searchView.setOnClickListener { searchView.isIconified = false }
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        LviewModel.getCity(query.toString())
        Log.d("error", query.toString())
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        LviewModel.getCity(newText.toString())
        Log.d("error", "qtc")
        return false
    }

}


