package com.marklynch.weather.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.marklynch.weather.data.manuallocation.ManualLocation
import com.marklynch.weather.livedata.db.ManualLocationRepository
import com.marklynch.weather.livedata.location.LocationLiveData
import com.marklynch.weather.livedata.network.NetworkInfoLiveData
import com.marklynch.weather.livedata.sharedpreferences.booleansharedpreference.BooleanSharedPreferencesLiveData
import com.marklynch.weather.livedata.sharedpreferences.booleansharedpreference.Use24hrClockSharedPreferenceLiveData
import com.marklynch.weather.livedata.sharedpreferences.booleansharedpreference.UseCelsiusSharedPreferenceLiveData
import com.marklynch.weather.livedata.sharedpreferences.booleansharedpreference.UseKmSharedPreferenceLiveData
import com.marklynch.weather.livedata.sharedpreferences.longsharedpreference.CurrentLocationIdSharedPreferenceLiveData
import com.marklynch.weather.livedata.sharedpreferences.longsharedpreference.LongSharedPreferencesLiveData
import com.marklynch.weather.livedata.weather.WeatherLiveData
import com.marklynch.weather.sharedpreferences.SHARED_PREFERENCES_CURRENT_LOCATION_ID
import com.marklynch.weather.sharedpreferences.SHARED_PREFERENCES_USE_24_HR_CLOCK
import com.marklynch.weather.sharedpreferences.SHARED_PREFERENCES_USE_CELSIUS
import com.marklynch.weather.sharedpreferences.SHARED_PREFERENCES_USE_KM
import com.sucho.placepicker.AddressData
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

open class MainViewModel(application: Application) : AndroidViewModel(application), KoinComponent {

    //Location
    val locationLiveData: LocationLiveData by inject()

    //Weather
    val weatherLiveData: WeatherLiveData by inject()

    //Internet Connection
    val networkInfoLiveData: NetworkInfoLiveData by inject()

    //Shared Preference for setting whether to use degrees C or F
    val useCelsiusSharedPreferencesLiveData: UseCelsiusSharedPreferenceLiveData by inject()

    //Shared Preference for setting whether to use km or mi
    val useKmSharedPreferencesLiveData: UseKmSharedPreferenceLiveData by inject ()

    //Shared Preference for setting whether to use 24 or 12 hr clock
    val use24hrClockSharedPreferencesLiveData: Use24hrClockSharedPreferenceLiveData by inject()

    val selectedLocationIdSharedPreferencesLiveData: CurrentLocationIdSharedPreferenceLiveData by inject()

//        val manualLocationRepository = ManualLocationRepository(application)
    private val manualLocationRepository: ManualLocationRepository by inject()
    val manualLocationLiveData = manualLocationRepository.manualLocationLiveData

    fun getLocationInformation() = locationLiveData.value
    fun getWeather() = weatherLiveData.value
    fun isUseCelsius() = useCelsiusSharedPreferencesLiveData.value
    fun isUseKm() = useKmSharedPreferencesLiveData.value
    fun isUse24hrClock() = use24hrClockSharedPreferencesLiveData.value
    fun getSelectedLocationId() = selectedLocationIdSharedPreferencesLiveData.value

    fun setUseCelsius(useCelsius: Boolean) {
        useCelsiusSharedPreferencesLiveData.setSharedPreference(useCelsius)
    }

    fun setUseKm(useKm: Boolean) {
        useKmSharedPreferencesLiveData.setSharedPreference(useKm)
    }

    fun setUse24hrClock(use24hrClock: Boolean) {
        use24hrClockSharedPreferencesLiveData.setSharedPreference(use24hrClock)
    }

    fun setSelectedLocationId(selectedLocationId: Long) {
        selectedLocationIdSharedPreferencesLiveData.setSharedPreference(selectedLocationId)
    }

    fun fetchLocation() {
        locationLiveData.fetchLocation()
    }

    fun fetchWeather(manualLocation: ManualLocation?) {
        if (manualLocation == null) {
            val lat = getLocationInformation()?.location?.latitude
            val lon = getLocationInformation()?.location?.longitude
            if (lat != null && lon != null)
                weatherLiveData.fetchWeather(lat, lon)
        } else {
            weatherLiveData.fetchWeather(manualLocation.latitude, manualLocation.longitude)
        }
    }

    fun addManualLocation(addressData: AddressData?) {
        if (addressData != null)
            manualLocationRepository.insert(addressData)
    }

    fun getCurrentlySelectedLocation(): ManualLocation? {
        return manualLocationLiveData?.value?.firstOrNull { it.id == getSelectedLocationId() }
    }
}