package com.example.weatherreport

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources.Theme
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.weatherreport.adapters.DataAdapter
import com.example.weatherreport.data.AppConstants
import org.json.JSONException
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 66)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val city : String?

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 66)
        }

        val location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        if (location != null) {
            city = getLocation(location.latitude, location.longitude)
            city?.let { displayForecast(city) }
        }

    }

    fun getForecast( city : String) {

        val displayData = mutableListOf<Map<String, String>>()
        val requestUrl = AppConstants().getWeatherAPIUrl(city, Locale.getDefault().language)

        val queue : RequestQueue = Volley.newRequestQueue(applicationContext)
        val forecastReq = JsonObjectRequest(Request.Method.GET, requestUrl, null,
            { response ->
                try {
                    val forecast = response.getJSONObject("forecast")
                    val forecastday = forecast.getJSONArray("forecastday")

                    for (i in 0 until AppConstants().forecastDuration){

                        val day = forecastday.getJSONObject(i)
                        val summInfo = day.getJSONObject("day")
                        val condition = summInfo.getJSONObject("condition")

                        val date = setDateFormat(day.getString("date")).uppercase()
                        val state = condition.getString("text")
                        val icon = "https:" + condition.getString("icon")
                        val avgtemp = summInfo.getString("avgtemp_c") + "°C"
                        val maxwind = summInfo.getString("maxwind_kph") + "km/h"
                        val avghumidity = summInfo.getString("avghumidity") + "%"

                        val dataItem = mapOf<String, String>("icon" to icon, "desc" to state, "date" to date, "avgtemp" to avgtemp, "maxwind" to maxwind, "avghumidity" to avghumidity)
                        displayData.add(dataItem)

                    }

                    val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    recyclerView.adapter = DataAdapter(displayData)


                } catch (e: JSONException) {
                    Toast.makeText(applicationContext, e.stackTraceToString(), Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                // TODO: Handle error
            }
        )

        queue.add(forecastReq)
    }

    fun getLocation(lat : Double, lon : Double): String? {

        val city : String?
        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val address = geocoder.getFromLocation(lat, lon, 1)

        city = address?.get(0)?.getLocality()

        return city

    }

    fun displayForecast(city : String){

        val cityName = findViewById(R.id.cityName) as TextView
        cityName.text = city

        getForecast(city.lowercase())
    }

    fun setDateFormat(date : String) : String{
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val inDate = sdf.parse(date)

        val fDate = SimpleDateFormat("dd, E")
        return fDate.format(inDate)
    }
}
