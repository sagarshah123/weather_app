package com.example.weatherapp.network

import retrofit2.http.GET
import com.example.weatherapp.model.Weather
import com.example.weatherapp.model.Place
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("/api/location/{woeid}")
    fun queryWeather(@Path("woeid") woeid: String?): Call<Weather?>?

    @GET("/api/location/search")
    fun queryName(@Query("query") name: String?): Call<List<Place?>?>?
}