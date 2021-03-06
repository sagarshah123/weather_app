package com.example.weatherapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Place {
    @SerializedName("distance")
    @Expose
    var distance: Long = 0

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("location_type")
    @Expose
    var locationType: String? = null

    @SerializedName("woeid")
    @Expose
    var woeid: Long = 0

    @SerializedName("latt_long")
    @Expose
    var lattLong: String? = null
}