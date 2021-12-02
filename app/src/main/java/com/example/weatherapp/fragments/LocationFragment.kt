package com.example.weatherapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adapter.LocateAdapter
import com.example.weatherapp.dataClass.SearchLocationsItem
import com.example.weatherapp.databinding.FragmentLocationBinding
import com.example.weatherapp.util.InternetConnectivity
import com.example.weatherapp.util.LocalKeyStorage

import com.example.weatherapp.viewModel.LocationViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_about.backbtn
import kotlinx.android.synthetic.main.fragment_location.*
import retrofit2.Call


class LocationFragment : Fragment(), SearchView.OnQueryTextListener,
    androidx.appcompat.widget.SearchView.OnQueryTextListener {

    private lateinit var LviewModel: LocationViewModel
    private lateinit var locateAdapter: LocateAdapter
    lateinit var city: Call<List<SearchLocationsItem>>
    var location_item : ArrayList<List<SearchLocationsItem>> = ArrayList()
    lateinit var binding: FragmentLocationBinding
    lateinit var localKeyStorage : LocalKeyStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_location, container, false)
        val view = binding.root
        localKeyStorage = LocalKeyStorage(requireContext())
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val searchView = binding.searchView
        LviewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        binding.lViewModel = LviewModel
        binding.lifecycleOwner = this
        binding.lottie.visibility = View.GONE

//        nav_view.visibility = View.GONE

        backbtn2.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_homeFragment)
//                findNavController().popBackStack()
        }
        backbtn2.setOnClickListener {
            findNavController().popBackStack()
        }

        if(InternetConnectivity.isNetworkAvailable(requireContext())){
            LviewModel.isInternet(true)
        }else{
            LviewModel.isInternet(false)
            Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show()
        }

        searchView.isIconified = false

        LviewModel.cityName.observe(viewLifecycleOwner,{
            if(!it.isNullOrEmpty()) {
                Log.d("bat", "onViewCreated: $it")

                Log.d("check",it.toString())
            }

        })
        searchView.setOnClickListener {
            searchView.isIconified = false
        }
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        LviewModel.cityName.observe(viewLifecycleOwner,{
            if(!it.isNullOrEmpty()) {
                Log.d("reply",it[0].name.toString())
                Log.d("reply",it[0].lat.toString())
                Log.d("reply",it[0].lon.toString())
                localKeyStorage.saveValue(LocalKeyStorage.latitude,it[0].lat.toString())
                localKeyStorage.saveValue(LocalKeyStorage.longitude,it[0].lon.toString())
                localKeyStorage.saveValue(LocalKeyStorage.cityName,it[0].name.toString())
                val view = requireActivity().findViewById<TextView>(R.id.txtlocation)
                view.text = localKeyStorage.getValue(LocalKeyStorage.cityName)
                 findNavController().popBackStack(R.id.homeFragment,true)
                findNavController().navigate(R.id.homeFragment)

            }
            else{
                Toast.makeText(context,"Please enter valid city name",Toast.LENGTH_SHORT).show()
                Log.d("reply","please enter the valid name")
            }

        })
        LviewModel.getCityName(query.toString())
        Log.d("error", query.toString())
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("error", "qtc")
        return false
    }



}


