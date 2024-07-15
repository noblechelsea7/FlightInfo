package com.example.flightinfo.ui.flights.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightinfo.api.ApiRepository

class FlightViewModelFactory(private val apiRepository: ApiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightViewModel::class.java)) {
            return FlightViewModel(apiRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}