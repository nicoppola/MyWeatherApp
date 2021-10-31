package com.example.myweatherapp.models.repository

import com.example.myweatherapp.models.OpenWeatherApi
import javax.inject.Inject

class WeatherRepository
@Inject
constructor(private val openWeatherApi: OpenWeatherApi) {

    suspend fun getCurrentWeather(location: String, units: String) = openWeatherApi.getWeather(location, units)
}