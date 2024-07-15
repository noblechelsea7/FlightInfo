package com.example.flightinfo.ui.rate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightinfo.api.ApiRepository

class ExchangeRatesViewModelFactory(private val apiRepository: ApiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExchangeRatesViewModel::class.java)) {
            return ExchangeRatesViewModel(apiRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}