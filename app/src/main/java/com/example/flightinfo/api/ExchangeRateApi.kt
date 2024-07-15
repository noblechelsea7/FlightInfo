package com.example.flightinfo.api

import com.example.flightinfo.api.data.LatestExchangeRatesResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {

    @GET("latest")
    fun getLatestExchangeRates(@Query("apikey") apiKey: String,
                               @Query("base_currency") baseCurrency: String?,
                               @Query("currencies") currencies: String? = null): Call<LatestExchangeRatesResult>
}