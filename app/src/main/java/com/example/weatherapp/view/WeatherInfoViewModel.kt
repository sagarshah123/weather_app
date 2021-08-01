package com.example.weatherapp.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.model.Weather
import com.example.weatherapp.repository.WeatherRepository

class WeatherInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WeatherRepository? = WeatherRepository.instance

    fun getWeatherList(woeIdList: List<String?>?): MutableLiveData<MutableList<Weather>> {
        return repository!!.getWeatherList(woeIdList)
    }

}