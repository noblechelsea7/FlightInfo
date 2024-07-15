package com.example.flightinfo.ui.flights

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.flightinfo.R
import com.example.flightinfo.api.data.FlightObj
import com.example.flightinfo.databinding.ItemFlightBinding
import com.example.flightinfo.ui.flights.viewmodel.FlyStatus

class FlightsAdapter(private var flights: List<FlightObj>) : RecyclerView.Adapter<FlightsAdapter.FlightViewHolder>() {

    inner class FlightViewHolder(private val binding: ItemFlightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(flight: FlightObj) {
            binding.tvExpectedTime.text = flight.expectTime
            binding.tvRealTime.text = flight.realTime
            binding.tvFlightNumber.text = "航機班號：${flight.airLineNum}"
            binding.tvBoardingGate.text = "登機門：${flight.airBoardingGate}"

            if (!flight.goalAirportCode.isNullOrEmpty() && !flight.goalAirportName.isNullOrEmpty()) {
                binding.tvDepartureCode.text = "KHH"
                binding.tvDepartureName.text = "高雄國際機場"
                binding.tvArrivalCode.text = flight.goalAirportCode
                binding.tvArrivalName.text = flight.goalAirportName
            } else {
                binding.tvDepartureCode.text = flight.upAirportCode
                binding.tvDepartureName.text = flight.upAirportName
                binding.tvArrivalCode.text = "KHH"
                binding.tvArrivalName.text = "高雄國際機場"
            }

            binding.tvStatus.text = flight.airFlyStatus
            val statusColorId = when (flight.airFlyStatus) {
                FlyStatus.DEPARTURE.value -> R.color.secondary_blue1
                FlyStatus.ARRIVED.value -> R.color.secondary_red
                FlyStatus.ON_TIME.value -> R.color.secondary_green
                else -> R.color.secondary_blue1
            }
            val color = ContextCompat.getColor(itemView.context, statusColorId)
            binding.tvStatus.setTextColor(color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val binding = ItemFlightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlightViewHolder(binding)
    }

    override fun getItemCount(): Int = flights.size

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        holder.bind(flights[position])
    }

    fun updateFlights(newFlights: List<FlightObj>) {
        flights = newFlights
        notifyDataSetChanged()
    }

}