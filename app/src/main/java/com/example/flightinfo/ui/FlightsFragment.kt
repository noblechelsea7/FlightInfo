package com.example.flightinfo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flightinfo.R
import com.example.flightinfo.databinding.FragmentFlightsBinding
import com.google.android.material.tabs.TabLayoutMediator

class FlightsFragment : Fragment() {

    private var _binding: FragmentFlightsBinding? = null
    private val binding get() = _binding!!

    private val tabTitles = arrayOf(R.string.tab_flight_departure,
                                    R.string.tab_flight_arrival)
    private val tabIcons = arrayOf(R.drawable.icon_departures,
                                       R.drawable.icon_arrivals)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentFlightsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val flightsPagerAdapter = FlightsPagerAdapter(this)
        binding.viewPager.adapter = flightsPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(tabIcons[position])
            tab.text = getString(tabTitles[position])
        }.attach()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}