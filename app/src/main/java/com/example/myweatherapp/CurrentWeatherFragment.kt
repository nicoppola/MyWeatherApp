package com.example.myweatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.myweatherapp.databinding.CurrentWeatherFragmentBinding
import com.example.myweatherapp.viewmodels.CurrentWeatherViewModel
import dagger.Module
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentWeatherFragment : Fragment() {

    private lateinit var binding: CurrentWeatherFragmentBinding
    private val viewModel: CurrentWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
//        return binding.root
        binding = DataBindingUtil.inflate<CurrentWeatherFragmentBinding>(inflater, R.layout.current_weather_fragment, container, false)

        binding.weatherVM = viewModel
        binding.lifecycleOwner = this

        return binding.root
        //return inflater.inflate(R.layout.current_weather_fragment, container, false)
    }
}