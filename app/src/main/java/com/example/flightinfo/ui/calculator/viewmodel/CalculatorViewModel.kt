package com.example.flightinfo.ui.calculator.viewmodel

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flightinfo.ui.rate.viewmodel.ExchangeRatesViewModel
import java.text.DecimalFormat

class CalculatorViewModel(private val exchangeRatesViewModel: ExchangeRatesViewModel) : ViewModel() {
    private val _displayText = MutableLiveData<String>().apply { value = "0" }
    val displayText: LiveData<String> get() = _displayText

    private var canAddOperation = false

    fun onNumberClick(number: String) {
        if (_displayText.value.isNullOrEmpty()
            || _displayText.value == "0") {
            _displayText.value = if (number == "00") "0" else number
        } else {
            _displayText.value += number
        }

        canAddOperation = true

        updateAmountWithoutCalculate()
    }

    fun onDecimalPointClick() {
        if (_displayText.value?.contains(".") == false) {
            if (_displayText.value.isNullOrEmpty()) {
                _displayText.value = "0."
            } else {
                if (_displayText.value?.last()?.isDigit() == true)
                    _displayText.value += "."
            }
        }
    }

    fun onOperatorClick(operator: String) {
        if (canAddOperation) {
            _displayText.value += operator
            canAddOperation = false
        } else {
            if (!_displayText.value.isNullOrEmpty()
                && _displayText.value?.last()?.isDigit() != true) {
                _displayText.value = _displayText.value?.dropLast(1)?.ifEmpty { "" }
                _displayText.value += operator
            }
        }
    }

    fun onEqualClick() {
        if (_displayText.value.isNullOrEmpty()
            || _displayText.value?.last()?.isDigit() != true) return

        val result = calculateResults()
        result.let {
            if (canAddOperation)
                _displayText.value = it

            exchangeRatesViewModel.setSelectedAmount(it.toDouble())
        }
    }

    fun onClearClick() {
        _displayText.value = ""
        canAddOperation = false
    }

    fun onDeleteClick() {
        _displayText.value = _displayText.value?.dropLast(1)
        canAddOperation = !_displayText.value.isNullOrEmpty()
                && displayText.value?.last()?.isDigit() == true

        updateAmountWithoutCalculate()
    }

    private fun updateAmountWithoutCalculate() {
        if (!_displayText.value.isNullOrEmpty()
            && _displayText.value?.isDigitsOnly() == true
            && _displayText.value != "0") {
            _displayText.value?.toDouble()?.let { exchangeRatesViewModel.setSelectedAmount(it) }
        }
    }

    private fun calculateResults(): String
    {
        val decimalFormat = DecimalFormat("#.####")

        // 分隔數字跟操作符
        val digitsOperators = getDigitsOperators()
        if(digitsOperators.isEmpty()) return ""

        // 數字先乘除
        val addSubTractList = timesDivisionCalculate(digitsOperators)
        if(addSubTractList.isEmpty()) return ""

        // 數字後加減
        val result = addSubtractCalculate(addSubTractList)
        return decimalFormat.format(result)
    }

    private fun addSubtractCalculate(addSubTractList: MutableList<Any>): Float
    {
        var result = addSubTractList[0] as Float

        for(i in addSubTractList.indices)
        {
            if(addSubTractList[i] is Char && i != addSubTractList.lastIndex)
            {
                val operator = addSubTractList[i]
                val nextDigit = addSubTractList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(digitsOperators: MutableList<Any>): MutableList<Any>
    {
        var addSubTractList = digitsOperators
        while (addSubTractList.contains('×') || addSubTractList.contains('÷'))
        {
            addSubTractList = calcTimesDiv(addSubTractList)
        }
        return addSubTractList
    }

    private fun calcTimesDiv(digitsOperators: MutableList<Any>): MutableList<Any>
    {
        val addSubTractList = mutableListOf<Any>()
        var restartIndex = digitsOperators.size

        for(i in digitsOperators.indices)
        {
            if(digitsOperators[i] is Char && i != digitsOperators.lastIndex && i < restartIndex)
            {
                val operator = digitsOperators[i]
                val prevDigit = digitsOperators[i - 1] as Float
                val nextDigit = digitsOperators[i + 1] as Float
                when(operator)
                {
                    '×' ->
                    {
                        addSubTractList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '÷' ->
                    {
                        addSubTractList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        addSubTractList.add(prevDigit)
                        addSubTractList.add(operator)
                    }
                }
            }

            if(i > restartIndex)
                addSubTractList.add(digitsOperators[i])
        }

        return addSubTractList
    }

    private fun getDigitsOperators(): MutableList<Any>
    {
        val digitsOperators = mutableListOf<Any>()
        var currentDigit = ""

        displayText.value?.forEach { character ->
            if(character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                digitsOperators.add(currentDigit.toFloat())
                currentDigit = ""
                digitsOperators.add(character)
            }
        }

        if(currentDigit != "")
            digitsOperators.add(currentDigit.toFloat())

        return digitsOperators
    }

}