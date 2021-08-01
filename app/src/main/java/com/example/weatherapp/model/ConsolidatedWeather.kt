package com.example.weatherapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConsolidatedWeather {
    @SerializedName("id")
    @Expose
    var id: Long = 0

    @SerializedName("weather_state_name")
    @Expose
    var weatherStateName: String? = null

    @SerializedName("weather_state_abbr")
    @Expose
    var weatherStateAbbr: String? = null

    @SerializedName("wind_direction_compass")
    @Expose
    var windDirectionCompass: String? = null

    @SerializedName("created")
    @Expose
    var created: String? = null

    @SerializedName("applicable_date")
    @Expose
    var applicableDate: String? = null

    @SerializedName("min_temp")
    @Expose
    var minTemp = 0.0

    @SerializedName("max_temp")
    @Expose
    var maxTemp = 0.0

    @SerializedName("the_temp")
    @Expose
    var theTemp = 0.0

    @SerializedName("wind_speed")
    @Expose
    var windSpeed = 0.0

    @SerializedName("wind_direction")
    @Expose
    var windDirection = 0.0

    @SerializedName("air_pressure")
    @Expose
    var airPressure = 0.0

    @SerializedName("humidity")
    @Expose
    var humidity: Long = 0

    @SerializedName("visibility")
    @Expose
    var visibility = 0.0

    @SerializedName("predictability")
    @Expose
    var predictability: Long = 0
}