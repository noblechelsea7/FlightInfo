package com.example.flightinfo.ui.rate

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.flightinfo.api.ApiRepository
import com.example.flightinfo.databinding.FragmentExchangeRatesBinding
import com.example.flightinfo.ui.calculator.CalculatorBottomSheetFragment
import com.example.flightinfo.ui.rate.viewmodel.ExchangeRatesViewModel
import com.example.flightinfo.ui.rate.viewmodel.ExchangeRatesViewModelFactory
import com.example.flightinfo.ui.utils.DialogUtils

class ExchangeRatesFragment : Fragment() {

    private var _binding: FragmentExchangeRatesBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ExchangeRatesViewModel

    private lateinit var currencyAdapter: CurrencyAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentExchangeRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiRepository = ApiRepository()
        val viewModelFactory = ExchangeRatesViewModelFactory(apiRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ExchangeRatesViewModel::class.java]

        currencyAdapter = CurrencyAdapter(viewModel) { currency, rate ->
            showCalculator(currency, rate)
        }
        binding.rvExchangeRate.adapter = currencyAdapter

        viewModel.exchangeRates.observe(viewLifecycleOwner) { rates ->
            currencyAdapter.updateExchangeRates(rates)
        }

        viewModel.selectedAmount.observe(viewLifecycleOwner) {
            currencyAdapter.notifyDataSetChanged()
        }

        viewModel.processBarVisibility.observe(viewLifecycleOwner) { isVisible ->
            binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                DialogUtils.showErrorDialog(requireContext(),"Error" , it)
            }
        }
    }

    private fun showCalculator(currency: String, rate: Double) {
        val calculatorBottomSheetFragment =
            CalculatorBottomSheetFragment.newInstance(viewModel, currency, rate)
        calculatorBottomSheetFragment.show(parentFragmentManager, calculatorBottomSheetFragment.tag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}