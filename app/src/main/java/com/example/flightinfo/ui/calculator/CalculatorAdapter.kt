package com.example.flightinfo.ui.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flightinfo.databinding.ItemCalculatorBinding

class CalculatorAdapter(
    private val buttons: List<String>,
    private val onButtonClick: (String) -> Unit
                       ) : RecyclerView.Adapter<CalculatorAdapter.CalculatorViewHolder>() {

    inner class CalculatorViewHolder(private val binding: ItemCalculatorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String,
                 onButtonClick: (String) -> Unit) {
            binding.button.text = text
            binding.button.setOnClickListener { onButtonClick(text) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculatorViewHolder {
        val binding = ItemCalculatorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalculatorViewHolder(binding)
    }

    override fun getItemCount(): Int = buttons.size

    override fun onBindViewHolder(holder: CalculatorViewHolder, position: Int) {
        holder.bind(buttons[position], onButtonClick)
    }
}