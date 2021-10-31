package com.example.myweatherapp.models

import com.example.myweatherapp.models.Responses.WeatherResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
const val API_KEY = "cf002751564a4c78f5f7ed479f1b9ba3"

//api.openweathermap.org/data/2.5/weather?q=Saint Louis&units=imperial&appid=cf002751564a4c78f5f7ed479f1b9ba3

interface OpenWeatherApi {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") location: String,
        @Query("units") units: String = "imperial"
    ): Response<WeatherResponse>

    @GET("weather")
    suspend fun getWeather(
        @Query( "lon") longitude: String,
        @Query("lat") latitude: String,
        @Query("units") units: String = "imperial"
    ): Response<WeatherResponse>

    companion object {
        operator fun invoke(
            //connectivityInterceptor: IConnectivityInterceptor
        ): OpenWeatherApi {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                //.addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                //.addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherApi::class.java)
        }
    }
}