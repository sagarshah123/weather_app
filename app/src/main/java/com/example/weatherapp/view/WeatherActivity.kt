package com.example.weatherapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.model.Weather
import com.example.weatherapp.network.RetrofitClient.retrofitInstance
import com.example.weatherapp.network.WeatherService
import com.example.weatherapp.util.GlideApp
import java.io.IOException

/**
 * Main Activity to display weather information for the city list.
 */
class WeatherActivity : AppCompatActivity() {
    private var progressDialog: ProgressBar? = null
    private var woeIdList: MutableList<String> = ArrayList()
    private var locations =
        listOf("Gothenburg", "Stockholm", "Mountain View", "London", "New York", "Berlin")
    private var adapter: CustomAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var weatherViewModel: WeatherInfoViewModel? = null
    private var weatherList = ArrayList<Weather>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(this@WeatherActivity)
        recyclerView!!.layoutManager = layoutManager
        adapter = CustomAdapter(this, weatherList)
        recyclerView!!.adapter = adapter

        // Fetch location data only once.
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val set = sharedPref.getStringSet(getString(R.string.location_list), null)
        if (set != null && set.isNotEmpty()) {
            woeIdList = set.toMutableList()

            weatherViewModel = ViewModelProvider(this).get(WeatherInfoViewModel::class.java)
            weatherViewModel!!.getWeatherList(woeIdList).observe(this, { weatherData ->
                setWeatherInfo(weatherData)
            })
        } else {
            getLocations()
        }
    }

    private fun setWeatherInfo(weatherData: MutableList<Weather>?) {
        if (weatherData != null && weatherData.size > 0) {
            weatherData.sortWith(compareBy { it.title })
            weatherList.clear()
            weatherList.addAll(weatherData)

            adapter?.updateDataList(weatherList)
            adapter?.notifyDataSetChanged()

        }
    }

    private fun getLocations() {
        Thread {
            this.runOnUiThread {
                progressDialog = findViewById(R.id.progressBar)
                progressDialog!!.visibility = View.VISIBLE
            }

            for (location in locations) {
                try {
                    val service = retrofitInstance!!.create(
                        WeatherService::class.java
                    )
                    val call = service.queryName(location)
                    val response = call!!.execute()
                    if (response.body() != null && response.body()!!.isNotEmpty()) {
                        Log.d(TAG, "Response: " + response.body()!![0]!!.woeid)
                        woeIdList.add(response.body()!![0]!!.woeid.toString())
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return@Thread
            with(sharedPref.edit()) {
                val set: MutableSet<String> = HashSet()
                set.addAll(woeIdList)
                putStringSet(getString(R.string.location_list), set)
                apply()
            }

            this.runOnUiThread {
                progressDialog!!.visibility = View.GONE

                weatherViewModel = ViewModelProvider(this).get(WeatherInfoViewModel::class.java)
                weatherViewModel!!.getWeatherList(woeIdList).observe(this, { weatherData ->
                    setWeatherInfo(weatherData)
                })
            }
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.app_bar_refresh) {
            weatherViewModel!!.getWeatherList(woeIdList).observe(this, { weatherData ->
                setWeatherInfo(weatherData)
            })
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    class CustomAdapter(private val context: Context, private var dataList: MutableList<Weather>) :
        RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {

        fun updateDataList(weatherList: MutableList<Weather>) {
            this.dataList = weatherList
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.custom_row, parent, false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
            holder.txtTitle.text = dataList[position].title

            if (dataList[position].consolidatedWeather!!.size > 1) {
                val weather = dataList[position].consolidatedWeather!![1]
                val url =
                    "https://www.metaweather.com/static/img/weather/png/64/" + weather.weatherStateAbbr + ".png"
                GlideApp.with(context).load(url).into(holder.stateIcon)

                holder.date.text = weather.applicableDate
                val cTemp = StringBuilder()
                cTemp.append(weather.theTemp.toInt().toString())
                    .append("°")
                holder.currentTemperature.text = cTemp.toString()

                holder.state.text = weather.weatherStateName

                val rangeTemp = StringBuilder()
                rangeTemp.append(weather.maxTemp.toInt().toString())
                    .append("°").append(" / ").append(
                        weather.minTemp.toInt()
                            .toString()
                    ).append("°")

                holder.range.text = rangeTemp.toString()
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

        inner class CustomViewHolder(mView: View) : RecyclerView.ViewHolder(
            mView
        ) {
            var txtTitle: TextView = mView.findViewById(R.id.title)
            var stateIcon: ImageView = mView.findViewById(R.id.state_icon)
            var date: TextView = mView.findViewById(R.id.date)
            var currentTemperature: TextView = mView.findViewById(R.id.current_temperature)
            var range: TextView = mView.findViewById(R.id.range)
            var state: TextView = mView.findViewById(R.id.state)
        }
    }

    companion object {
        private val TAG = WeatherActivity::class.java.simpleName
    }
}