package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.model.Weather
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.view.WeatherInfoViewModel
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito

class WeatherViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var weatherRepository: WeatherRepository

    private lateinit var viewModel: WeatherInfoViewModel

    @Before
    fun setUp() {
        // do something if required
        weatherRepository = WeatherRepository.instance!!
    }

    @After
    fun tearDown() {
        // do something if required
    }


    @Test
    fun getWeatherListTest_success() {
        val weather = Weather()
        val weatherList = MutableLiveData(mutableListOf(weather))

        val woeIdList = listOf(Long.MIN_VALUE)
        Mockito.`when`(weatherRepository.getWeatherList(woeIdList)).thenReturn(weatherList)

        Assert.assertEquals(1, viewModel.getWeatherList(woeIdList).value?.size)
    }
}