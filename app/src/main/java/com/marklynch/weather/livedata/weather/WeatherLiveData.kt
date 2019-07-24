package com.marklynch.weather.livedata.weather

import androidx.lifecycle.LiveData
import okhttp3.HttpUrl
import org.koin.core.parameter.parametersOf
import org.koin.standalone.KoinComponent
import org.koin.standalone.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class WeatherLiveData : LiveData<WeatherResponse>(), KoinComponent {

    private val appId = "74f01822a2b8950db2986d7e28a5978a"

    fun fetchWeather(lat: Double = 0.0, lon: Double = 0.0) {

        if (lat == 0.0 && lon == 0.0)
            return
        
        val retrofit = getRetrofitInstance("https://api.openweathermap.org")

        val apiService = retrofit.create(RestApiService::class.java)

        val call = apiService.getCurrentWeatherData(lat, lon, appId)

        call.enqueue(object : Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                postValue(null)
            }

            override fun onResponse(call: Call<WeatherResponse>, weatherResponseWrapper: Response<WeatherResponse>) {
                val weatherResponse = weatherResponseWrapper.body()
                postValue(weatherResponse)
            }
        })
    }

    private fun getRetrofitInstance(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(get<HttpUrl>{
                parametersOf(baseUrl)
            })
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    interface RestApiService {
        @GET("data/2.5/weather?")
        fun getCurrentWeatherData(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") app_id: String): Call<WeatherResponse>
    }
}