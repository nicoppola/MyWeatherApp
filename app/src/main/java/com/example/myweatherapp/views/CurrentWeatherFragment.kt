package com.example.myweatherapp.views

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import dagger.hilt.android.AndroidEntryPoint

import com.example.myweatherapp.R


@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    private lateinit var binding: CurrentWeatherFragmentBinding
    private val viewModel: CurrentWeatherViewModel by viewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<CurrentWeatherFragmentBinding>(
            inflater,
            R.layout.current_weather_fragment,
            container,
            false
        )

        binding.weatherVM = viewModel
        binding.lifecycleOwner = this

        // listen to message for toast updates
        viewModel.weatherStatus.observe(this.viewLifecycleOwner, Observer { status ->
            Toast.makeText(context, status, Toast.LENGTH_LONG).show()
        })

        // handle location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        locationManager =
            activity?.applicationContext?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        requestRequiredPermissions()

        // bind listeners
        binding.currentLocationWeather.setOnClickListener {
            getCurrentLocationWeather()
        }
        binding.etCityZip.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    (activity?.applicationContext?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
                        hideSoftInputFromWindow(view?.windowToken, 0)
                    }
                    binding.etCityZip.clearFocus()
                    //binding.etCityZip.text.clear()
                    //binding.etCityZip.isCursorVisible = false
                    viewModel.getCurrentWeather()

                    return true
                }
                return false
            }
        })


        return binding.root
    }

    fun requestRequiredPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requestLocationPermission.launch(requiredPermissions)
            return
        }
    }

    private fun getCurrentLocationWeather() {

        if (ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.weatherStatus.postValue("Error: Must Provide access to location data")
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                viewModel.getCurrentLocationWeather(
                    location.longitude.toString(),
                    location.latitude.toString()
                )
            } else {
                viewModel.weatherStatus.postValue("Error: Could not get current location")
            }
        }
    }

    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                getCurrentLocationWeather()
            }
        }
}