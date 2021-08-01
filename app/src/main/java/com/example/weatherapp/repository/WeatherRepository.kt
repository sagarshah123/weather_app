package com.example.weatherapp.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.model.Weather
import com.example.weatherapp.network.RetrofitClient
import com.example.weatherapp.network.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Repository for making API calls using Retrofit.
 */
class WeatherRepository {

    private val mutableLiveData = MutableLiveData<MutableList<Weather>>()
    private val weatherList = ArrayList<Weather>()


    fun getWeatherList(woeIdList: List<String?>?): MutableLiveData<MutableList<Weather>> {
        weatherList.clear()

        for (woeId in woeIdList!!) {
            val query = "$woeId"
            Log.d(TAG, "query: $query")
            val service = RetrofitClient.retrofitInstance!!.create(
                WeatherService::class.java
            )
            val call = service.queryWeather(query)
            call!!.enqueue(object : Callback<Weather?> {
                override fun onResponse(call: Call<Weather?>, response: Response<Weather?>) {
                    if (response.body() != null) {
                        Log.d(TAG, "Weather Response: " + response.body()!!.title)
                        weatherList.add(response.body()!!)
                        mutableLiveData.value = weatherList
                    }
                }

                override fun onFailure(call: Call<Weather?>, t: Throwable) {
                    Log.d(TAG, "Error Response: " + t.localizedMessage)
                }
            })
        }

        return mutableLiveData
    }

    companion object {
        private val TAG = WeatherRepository::class.java.simpleName

        private var weatherRepository: WeatherRepository? = null
        val instance: WeatherRepository?
            get() {
                if (weatherRepository == null) {
                    weatherRepository = WeatherRepository()
                }
                return weatherRepository
            }
    }
}