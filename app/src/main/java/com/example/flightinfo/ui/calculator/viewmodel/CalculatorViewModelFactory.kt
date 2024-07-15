package com.example.flightinfo.ui.calculator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightinfo.ui.rate.viewmodel.ExchangeRatesViewModel

class CalculatorViewModelFactory(private val exchangeRatesViewModel: ExchangeRatesViewModel)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(exchangeRatesViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}