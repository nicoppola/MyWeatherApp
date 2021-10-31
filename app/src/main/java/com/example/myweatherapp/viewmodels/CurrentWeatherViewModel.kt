package com.example.myweatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.models.Responses.WeatherResponse
import com.example.myweatherapp.models.repository.WeatherRepository
import com.example.myweatherapp.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel
@Inject
constructor(private val weatherRepository: WeatherRepository): ViewModel() {

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse>
        get() = _weatherData

    val weatherStatus = MutableLiveData<String>()
    val location = MutableLiveData<String>()

    init {
        location.value = "St. Louis"
        getCurrentWeather()
    }

    fun getCurrentWeather() = viewModelScope.launch {
        weatherRepository.getCurrentWeather(location.value ?: "", "imperial").let{ response ->

            if(response.isSuccessful){
                _weatherData.postValue(response.body())
                weatherStatus.postValue("success")
            } else{
                Log.d(TAG, "getCurrentWeather Error Response: ${response.message()}")
                weatherStatus.postValue("Error getting weather data: ${response.message()}")
            }
            location.postValue("")
        }
    }

    fun getCurrentLocationWeather(longitude: String, latitude: String) = viewModelScope.launch {
        weatherRepository.getCurrentWeather(longitude, latitude, "imperial").let{ response ->
            if(response.isSuccessful){
                _weatherData.postValue(response.body())
                weatherStatus.postValue("success")
            } else{
                Log.d(TAG, "getCurrentWeather Error Response: ${response.message()}")
                weatherStatus.postValue("Error getting weather data: ${response.message()}")
            }
            location.postValue("")
        }
    }
}