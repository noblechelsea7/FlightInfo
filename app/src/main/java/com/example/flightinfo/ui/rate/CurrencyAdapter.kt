package com.example.flightinfo.ui.rate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.flightinfo.R
import com.example.flightinfo.databinding.ItemExchangeRateBinding
import com.example.flightinfo.ui.rate.viewmodel.ExchangeRatesViewModel

class CurrencyAdapter(
    private val viewModel: ExchangeRatesViewModel,
    private val onItemClick: (String, Double) -> Unit
                     ) : RecyclerView.Adapter<CurrencyAdapter.ExchangeRateViewHolder>() {

    private var exchangeRates: Map<String, Double> = mapOf()

    init {
        exchangeRates = viewModel.exchangeRates.value ?: mapOf()
    }

    inner class ExchangeRateViewHolder(private val binding: ItemExchangeRateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: String, rate: Double, position: Int) {
            val colorId = if (position == 0) R.color.secondary_red1 else R.color.white
            val color = ContextCompat.getColor(itemView.context, colorId)
            binding.root.setBackgroundColor(color)

            binding.tvCurrency.text = currency
            binding.tvRate.text = String.format("$%.2f", rate)
            binding.root.setOnClickListener {
                onItemClick(currency, rate)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateViewHolder {
        val binding = ItemExchangeRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExchangeRateViewHolder(binding)
    }

    override fun getItemCount(): Int = viewModel.exchangeRates.value?.size ?: 0

    override fun onBindViewHolder(holder: ExchangeRateViewHolder, position: Int) {
        if (viewModel.exchangeRates.value == null) return
        val (currency, rate) = exchangeRates.toList()[position]
        val amount = viewModel.getConvertedAmount(rate)
        holder.bind(currency, amount, position)
    }

    fun updateExchangeRates(newExchangeRates: Map<String, Double>) {
        exchangeRates = newExchangeRates
        notifyDataSetChanged()
    }
}