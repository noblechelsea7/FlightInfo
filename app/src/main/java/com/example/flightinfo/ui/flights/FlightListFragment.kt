package com.example.flightinfo.ui.flights

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flightinfo.api.FlightStatus
import com.example.flightinfo.api.ApiRepository
import com.example.flightinfo.databinding.FragmentFlightListBinding
import com.example.flightinfo.ui.flights.viewmodel.FlightViewModel
import com.example.flightinfo.ui.flights.viewmodel.FlightViewModelFactory
import com.example.flightinfo.ui.utils.DialogUtils

class FlightListFragment : Fragment() {
    private var _binding: FragmentFlightListBinding? = null
    private val binding get() = _binding!!

    private lateinit var flightsAdapter: FlightsAdapter
    private val flightStatus by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(ARG_FLIGHT_STATUS, FlightStatus::class.java)
        } else {
            arguments?.getSerializable(ARG_FLIGHT_STATUS) as FlightStatus
        }
    }

    private lateinit var viewModel: FlightViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFlightListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiRepository = ApiRepository()
        val viewModelFactory = FlightViewModelFactory(apiRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[FlightViewModel::class.java]
        viewModel.fetchFlights(flightStatus?.value ?: FlightStatus.DEPARTURE.value)

        flightsAdapter = FlightsAdapter(emptyList())
        binding.rvFlights.adapter = flightsAdapter
        binding.rvFlights.layoutManager = LinearLayoutManager(requireContext())

        when (flightStatus) {
            FlightStatus.DEPARTURE -> viewModel.departureFlights.observe(viewLifecycleOwner) { flights ->
                flightsAdapter.updateFlights(flights)
            }

            FlightStatus.ARRIVAL -> viewModel.arrivalFlights.observe(viewLifecycleOwner) { flights ->
                flightsAdapter.updateFlights(flights)
            }

            else -> {}
        }

        viewModel.processBarVisibility.observe(viewLifecycleOwner) { isVisible ->
            binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                DialogUtils.showErrorDialog(requireContext(), "Error", it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_FLIGHT_STATUS = "flight_status"

        fun newInstance(flightStatus: FlightStatus) = FlightListFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_FLIGHT_STATUS, flightStatus)
            }
        }
    }
}