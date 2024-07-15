package com.example.flightinfo.api.data

import com.google.gson.annotations.SerializedName

data class FlightResult(
    @field:SerializedName("InstantSchedule")
    val instantSchedule: List<FlightObj>)
