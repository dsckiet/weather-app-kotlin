package com.example.weatherapp.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSearchBinding
import com.example.weatherapp.util.InternetConnectivity
import com.example.weatherapp.util.LocalKeyStorage
import com.example.weatherapp.viewModel.LocationViewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var LviewModel: LocationViewModel
    lateinit var localKeyStorage: LocalKeyStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        localKeyStorage = LocalKeyStorage(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        LviewModel = ViewModelProvider(this).get(LocationViewModel::class.java)
        binding.lViewModel2 = LviewModel
        binding.lifecycleOwner = this
        binding.lottie.visibility = View.GONE
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        if (InternetConnectivity.isNetworkAvailable(requireContext())) {
            LviewModel.isInternet(true)
        } else {
            LviewModel.isInternet(false)
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
        }
        binding.searchCity.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let { str ->
                if (str.isNotEmpty()) {
                    if (InternetConnectivity.isNetworkAvailable(requireContext())) {
                        LviewModel.isInternet(true)
                    } else {
                        LviewModel.isInternet(false)
//                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show()
                    }
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
                            findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
                            }
                        } else {
                            binding.cityNameCard.visibility = View.GONE
                            Log.d("reply", "please enter the valid name")
                        }

                    })
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }
}