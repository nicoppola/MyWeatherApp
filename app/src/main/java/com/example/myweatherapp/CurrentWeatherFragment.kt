package com.example.myweatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.myweatherapp.databinding.CurrentWeatherFragmentBinding
import com.example.myweatherapp.viewmodels.CurrentWeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.*
import dagger.hilt.android.AndroidEntryPoint
import android.app.AlertDialog

import android.content.Intent

import android.content.DialogInterface


@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    private lateinit var binding: CurrentWeatherFragmentBinding
    private val viewModel: CurrentWeatherViewModel by viewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<CurrentWeatherFragmentBinding>(inflater, R.layout.current_weather_fragment, container, false)

        binding.weatherVM = viewModel
        binding.lifecycleOwner = this

        viewModel.weatherStatus.observe(this.viewLifecycleOwner, Observer { status ->
            Toast.makeText(context, status, Toast.LENGTH_LONG).show()
        })

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        locationManager = activity?.applicationContext?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        binding.currentLocationWeather.setOnClickListener{
            getCurrentLocationWeather()
        }

        return binding.root
    }

    fun getCurrentLocationWeather() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestLocationPermission.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
            return
        }
        doGetCurrentLocationWeather()
    }


    @SuppressLint("MissingPermission")
    private fun doGetCurrentLocationWeather(){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
            if (location != null) {
                viewModel.getCurrentLocationWeather(location.longitude.toString(), location.latitude.toString())
            }
            else{
                viewModel.weatherStatus.postValue("Error: Could not get current location")
            }
        }
    }


    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if(granted){
                doGetCurrentLocationWeather()
                }
            }
}