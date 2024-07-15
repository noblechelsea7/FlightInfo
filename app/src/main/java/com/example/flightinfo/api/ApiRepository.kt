package com.example.flightinfo.api

class ApiRepository {

    fun getLatestExchangeRates(currency: String?) =
        ApiClient.exchangeRateApi.getLatestExchangeRates(apiKey = API_KEY,
                                                         baseCurrency = currency,
                                                         currencies = "EUR,USD,GBP,JPY,KRW,HKD")

    fun getFlight(flightStatus: Int) =
        ApiClient.flightApi.getInstantSchedule(airFlyLine = 2,
                                               airFlyIO = flightStatus)

    companion object {
        private const val API_KEY = "fca_live_3MZYMvcXLpOr2qvbYOQXh91uP6ZucOdLRwnkVftQ"
    }
}

enum class FlightStatus(val value: Int) {
    DEPARTURE(1),
    ARRIVAL(2)
}