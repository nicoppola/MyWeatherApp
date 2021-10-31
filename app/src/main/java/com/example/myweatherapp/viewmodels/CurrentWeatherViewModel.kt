package com.example.myweatherapp.viewmodels

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweatherapp.models.Responses.WeatherResponse
import com.example.myweatherapp.models.repository.WeatherRepository
import com.example.myweatherapp.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel
@Inject
constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

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
        if(location.value.isNullOrEmpty()){
            return@launch
        }
        if(location.value?.isDigitsOnly() == true){
            handleWeatherResponse(weatherRepository.getCurrentWeather(location.value?.toInt() ?: 0, "imperial"))
            return@launch
        }
        handleWeatherResponse(weatherRepository.getCurrentWeather(location.value ?: "", "imperial"))
    }

    fun getCurrentLocationWeather(longitude: String, latitude: String) = viewModelScope.launch {
        handleWeatherResponse(weatherRepository.getCurrentWeather(longitude, latitude, "imperial"))
    }

    private suspend fun handleWeatherResponse(response: Response<WeatherResponse>) {
        if (response.isSuccessful) {
            _weatherData.postValue(response.body())
            weatherStatus.postValue("success")
        } else {
            Log.d(TAG, "getCurrentWeather Error Response: ${response.message()}")
            weatherStatus.postValue("Error getting weather data: ${response.message()}")
        }
        location.postValue("")
    }
}