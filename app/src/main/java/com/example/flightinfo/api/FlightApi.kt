package com.example.flightinfo.api

import com.example.flightinfo.api.data.FlightResult
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface FlightApi {

    @POST("API/InstantSchedule.ashx")
    fun getInstantSchedule(
        @Query("AirFlyLine") airFlyLine: Int,
        @Query("AirFlyIO") airFlyIO: Int
                          ): Call<FlightResult>
}