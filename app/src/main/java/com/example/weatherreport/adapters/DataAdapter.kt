package com.example.weatherreport.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherreport.R

class DataAdapter (val forecasts : List<Map<String, String>>) : RecyclerView.Adapter<DataAdapter.MyViewHolder>(){

    class MyViewHolder(item : View) : RecyclerView.ViewHolder(item){

        val forecastIcon = item.findViewById(R.id.avgIcon) as ImageView
        val forecastDesc = item.findViewById(R.id.descr) as TextView
        val forecastDate = item.findViewById(R.id.dayDate) as TextView
        val forecastTemp = item.findViewById(R.id.avgTemp) as TextView
        val forecastWind = item.findViewById(R.id.maxWind) as TextView
        val forecastHumidity = item.findViewById(R.id.avgHum) as TextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.forecast_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Log.v("HERE", forecasts[position].toString())

        Glide.with(holder.forecastIcon.context).asBitmap().load(forecasts[position]["icon"]).into(holder.forecastIcon)
        holder.forecastDesc.text = forecasts[position]["desc"]
        holder.forecastDate.text = forecasts[position]["date"]

        holder.forecastTemp.text = forecasts[position]["avgtemp"]
        holder.forecastWind.text = forecasts[position]["maxwind"]
        holder.forecastHumidity.text = forecasts[position]["avghumidity"]
    }

    override fun getItemCount() = forecasts.size

}