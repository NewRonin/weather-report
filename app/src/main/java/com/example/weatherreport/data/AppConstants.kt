package com.example.weatherreport.data

public class AppConstants {

    val weatherAPIKey: String = "07ca7b8bcdc84bd2b8c112249232108"
    val weatherAPIBase: String = "https://api.weatherapi.com/v1"
    val weatherAPIForecast: String = "/forecast.json"

    val forecastDuration: Int = 5;
    val locationRequestCode = 66;

    val getWeatherAPIUrl =
        { city : String, lang : String -> weatherAPIBase + weatherAPIForecast + "?q=" + city + "&days=" + forecastDuration.toString() + "&lang=" + lang + "&key=" + weatherAPIKey }

}
