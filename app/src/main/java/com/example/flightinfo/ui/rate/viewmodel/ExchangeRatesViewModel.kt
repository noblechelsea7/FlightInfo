package com.example.flightinfo.ui.rate.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightinfo.api.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExchangeRatesViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    private val _exchangeRates = MutableLiveData<Map<String, Double>>()
    val exchangeRates: LiveData<Map<String, Double>> get() = _exchangeRates

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    val processBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _selectedAmount = MutableLiveData<Double>()
    val selectedAmount: LiveData<Double> get() = _selectedAmount

    fun setSelectedAmount(amount: Double) {
        _selectedAmount.value = amount
    }

    fun getConvertedAmount(rate: Double): Double {
        return (_selectedAmount.value ?: 1.0) * rate
    }


    init {
        fetchExchangeRates()
    }

    fun fetchExchangeRates(currency: String?= "USD") {
        processBarVisibility.value = true

        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiRepository.getLatestExchangeRates(currency).execute()
                }

                processBarVisibility.value = false

                if (response.isSuccessful) {
                    val currencyList = response.body()?.data ?: mapOf()
                    val defaultCurrency = currencyList.filterKeys { it == currency }
                    val sortCurrencyList = defaultCurrency + currencyList.filterKeys { it != currency }
                    _exchangeRates.postValue(sortCurrencyList)
                } else {
                    _errorMessage.postValue(response.message())
                }
            } catch (e: Exception) {
                processBarVisibility.value = false
                _errorMessage.postValue(e.message)
            }
        }
    }
}