package com.example.myweatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.models.WeatherResponse
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

    init {
        getCurrentWeather()
    }

    private fun getCurrentWeather() = viewModelScope.launch {
        weatherRepository.getCurrentWeather("St. Louis", "imperial").let{ response ->

            if(response.isSuccessful){
                _weatherData.postValue(response.body())
            } else{
                Log.d(TAG, "getCurrentWeather Error Response: ${response.message()}")
            }
        }
    }
}