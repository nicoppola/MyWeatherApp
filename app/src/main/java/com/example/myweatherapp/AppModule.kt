package com.example.myweatherapp

import android.app.Application
import com.example.myweatherapp.models.API_KEY
import com.example.myweatherapp.models.BASE_URL
import com.example.myweatherapp.models.OpenWeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun provideConnectivityInterceptor(context: Application) = ConnectivityInterceptor(context)

    @Provides
    @Singleton
    fun provideRetrofitInstance(connectivityInterceptor: ConnectivityInterceptor): OpenWeatherApi {
        return OpenWeatherApi.invoke()
    }
}