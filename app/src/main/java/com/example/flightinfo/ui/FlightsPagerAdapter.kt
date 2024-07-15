package com.example.flightinfo.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.flightinfo.api.FlightStatus
import com.example.flightinfo.ui.flights.FlightListFragment

class FlightsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return FlightListFragment.newInstance(
            if (position == 1) FlightStatus.ARRIVAL else FlightStatus.DEPARTURE)
    }
}