package com.example.flightinfo.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    fun getInstance(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private const val BASE_URL = "https://api.freecurrencyapi.com/v1/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: FlightApi by lazy {
        retrofit.create(FlightApi::class.java)
    }

    val flightApi: FlightApi = getInstance("https://www.kia.gov.tw/").create(FlightApi::class.java)

    val exchangeRateApi: ExchangeRateApi = getInstance("https://api.freecurrencyapi.com/v1/").create(ExchangeRateApi::class.java)
}