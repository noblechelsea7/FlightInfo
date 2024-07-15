package com.example.flightinfo.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.flightinfo.databinding.FragmentCalculatorBottomSheetBinding
import com.example.flightinfo.ui.calculator.viewmodel.CalculatorViewModel
import com.example.flightinfo.ui.calculator.viewmodel.CalculatorViewModelFactory
import com.example.flightinfo.ui.rate.viewmodel.ExchangeRatesViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CalculatorBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCalculatorBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var calculatorViewModel: CalculatorViewModel
    private lateinit var exchangeRatesViewModel: ExchangeRatesViewModel
    private lateinit var calculatorAdapter: CalculatorAdapter
    private lateinit var selectedCurrency: String

    private var isFetchCurrency: Boolean = false

    private val buttons = listOf(
        "", "C", "⌫", "÷",
        "7", "8", "9", "×",
        "4", "5", "6", "−",
        "1", "2", "3", "+",
        ".", "0", "00", "=")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // 預設顯示全部內容
        dialog !!.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheetInternal = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val bottomSheetBehavior = bottomSheetInternal?.let { BottomSheetBehavior.from(it) }
            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior?.isDraggable = false
        }

        _binding = FragmentCalculatorBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calculatorViewModel = ViewModelProvider(
            this,
            CalculatorViewModelFactory(exchangeRatesViewModel))[CalculatorViewModel::class.java]

        calculatorAdapter = CalculatorAdapter(buttons) { button ->
            when (button) {
                "C" -> calculatorViewModel.onClearClick()
                "⌫" -> calculatorViewModel.onDeleteClick()
                "+" -> calculatorViewModel.onOperatorClick("+")
                "−" -> calculatorViewModel.onOperatorClick("-")
                "×" -> calculatorViewModel.onOperatorClick("×")
                "÷" -> calculatorViewModel.onOperatorClick("÷")
                "=" -> calculatorViewModel.onEqualClick()
                "." -> calculatorViewModel.onDecimalPointClick()
                "" -> {}
                else -> {
                    if(!isFetchCurrency) exchangeRatesViewModel.fetchExchangeRates(selectedCurrency)
                    isFetchCurrency = true
                    calculatorViewModel.onNumberClick(button)
                }
            }
        }

        binding.rvCalculator.adapter = calculatorAdapter

        binding.tvCurrency.text = selectedCurrency

        calculatorViewModel.displayText.observe(viewLifecycleOwner) {
            binding.tvDisplayText.text = it
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(exchangeRatesViewModel: ExchangeRatesViewModel,
                        currency: String,
                        rate: Double): CalculatorBottomSheetFragment {
            val fragment = CalculatorBottomSheetFragment()
            fragment.exchangeRatesViewModel = exchangeRatesViewModel
            fragment.selectedCurrency = currency
            return fragment
        }
    }
}