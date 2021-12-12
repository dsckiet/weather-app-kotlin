package com.example.weatherapp.fragments

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.android.synthetic.main.fragment_about.backbtn
import kotlinx.android.synthetic.main.fragment_location.*
//import com.example.weatherapp.dataclass.SearchLocationItem
//import com.example.weatherapp.dataclass.Values
//import com.example.weatherapp.viewModel.LocationViewModel
import retrofit2.Call


class LocationFragment : Fragment() {

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
        LviewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        binding.lViewModel = LviewModel
        binding.lifecycleOwner = this
        binding.lottie.visibility = View.GONE

        backbtn.setOnClickListener {
            findNavController().navigate(R.id.action_locationFragment_to_homeFragment)
        }

        if(InternetConnectivity.isNetworkAvailable(requireContext())){
            LviewModel.isInternet(true)
        }else{
            LviewModel.isInternet(false)
            Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show()
        }
        binding.searchCity.addTextChangedListener(textWatcher)
    }
private val textWatcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let { str ->
            if (str.isNotEmpty()) {
                binding.changeBtn.setImageResource(R.drawable.ic_close)
                binding.changeBtn.setOnClickListener {
                   binding.searchCity.setText("")
                }
                if (InternetConnectivity.isNetworkAvailable(requireContext())) {
                    LviewModel.isInternet(true)
                } else {
                    LviewModel.isInternet(false)
//                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
                }

                object : CountDownTimer(500,1000){
                    override fun onTick(millisUntilFinished: Long) {
                        Log.d("timer testing","h")
                    }

                    override fun onFinish() {
                        Log.d("timer testing","hogya")
                        LviewModel.getCityName(str.toString())
                        LviewModel.cityName.observe(viewLifecycleOwner, {
                            if (!it.isNullOrEmpty()) {
                                binding.cityNameCard.visibility = View.VISIBLE
                                binding.cityName.text = it[0].name
                                binding.cityNameCard.setOnClickListener { lol ->
                                    localKeyStorage.saveValue(LocalKeyStorage.latitude,it[0].lat.toString())
                                    localKeyStorage.saveValue(LocalKeyStorage.longitude,it[0].lon.toString())
                                    localKeyStorage.saveValue(LocalKeyStorage.cityName,it[0].name.toString())
                                    val view = requireActivity().findViewById<TextView>(R.id.txtlocation)
                                    view.text = localKeyStorage.getValue(LocalKeyStorage.cityName)

                                    findNavController().popBackStack(R.id.homeFragment,true)
                                    findNavController().navigate(R.id.homeFragment)
                                }
                            } else {
                                binding.cityNameCard.visibility = View.GONE
                                Log.d("reply", "please enter the valid name")
                            }
                        })

                    }
                }.start()
            }else{
                binding.changeBtn.setImageResource(R.drawable.searchicon)
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
}
}


