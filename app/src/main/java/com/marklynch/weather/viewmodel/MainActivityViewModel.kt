package com.marklynch.weather.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.marklynch.weather.livedata.gps.GpsStatusLiveData
import com.marklynch.weather.livedata.apppermissions.AppPermissionLiveData
import com.marklynch.weather.livedata.apppermissions.locationPermission
import com.marklynch.weather.livedata.location.LocationLiveData
import com.marklynch.weather.livedata.util.CurrentTimeLiveData
import com.marklynch.weather.livedata.weather.WeatherLiveData
import com.marklynch.weather.livedata.webresource.RawWebResourceLiveData

class MainActivityViewModel(application: Application) : BaseActivityViewModel(application) {
    val liveDataFab = MutableLiveData<String>()

    //Time
    val currentTimeLiveData = CurrentTimeLiveData()

    //Raw web resource
    val rawWebResourceLiveData = RawWebResourceLiveData()

    //Location permission
    val locationAppPermissionLiveData = AppPermissionLiveData(application,locationPermission)

    //GPS status
    val gpsStatusLiveData = GpsStatusLiveData(application)

    //Location
    val locationLiveData = LocationLiveData(application)

    //Weather
    val weatherLiveData = WeatherLiveData()
}