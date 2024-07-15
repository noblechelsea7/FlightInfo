package com.example.flightinfo.ui.flights.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightinfo.api.FlightStatus
import com.example.flightinfo.api.ApiRepository
import com.example.flightinfo.api.data.FlightObj
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlightViewModel(private val apiRepository: ApiRepository) : ViewModel() {
    private val _departureFlights = MutableLiveData<List<FlightObj>>()
    val departureFlights: LiveData<List<FlightObj>> get() = _departureFlights

    private val _arrivalFlights = MutableLiveData<List<FlightObj>>()
    val arrivalFlights: LiveData<List<FlightObj>> get() = _arrivalFlights

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private var fetchJob: Job? = null

    val processBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)


    init {
        fetchFlights(FlightStatus.DEPARTURE.value)
    }

    fun fetchFlights(flightStatus: Int = FlightStatus.DEPARTURE.value) {
        processBarVisibility.postValue(true)

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {

                    val response = withContext(Dispatchers.IO) {
                        apiRepository.getFlight(flightStatus).execute()
                    }

                    processBarVisibility.postValue(false)

                    if (response.isSuccessful) {
                        when (flightStatus) {
                            FlightStatus.DEPARTURE.value -> {
                                _departureFlights.postValue(response.body()?.instantSchedule)
                            }
                            FlightStatus.ARRIVAL.value -> {
                                _arrivalFlights.postValue(response.body()?.instantSchedule)
                            }
                        }
                    } else {
                        _errorMessage.postValue(response.message())
                    }

                } catch (e: Exception) {
                    processBarVisibility.postValue(false)
                    _errorMessage.postValue(e.message)
                }

                delay(10000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
    }
}