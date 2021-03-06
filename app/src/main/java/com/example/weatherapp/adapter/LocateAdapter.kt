package com.example.weatherapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R

class LocateAdapter(requireContext: Context) :
    RecyclerView.Adapter<LocateAdapter.LocateViewHolder>() {

//     lateinit var LocationItem : Locations
    var LocationItem : String = ""

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocateViewHolder {
        return LocateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.search, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LocateViewHolder, position: Int) {
//        val indexOfList = LocationItem[position]
        holder.cityName.text = LocationItem
    }

    override fun getItemCount(): Int {
        return 1
    }

    class LocateViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cityName: TextView = itemView.findViewById(R.id.searchCity)
    }

    fun setCity(city: String){
        this.LocationItem = city
        Log.d("progress", "NRA")
        notifyDataSetChanged()
    }

}