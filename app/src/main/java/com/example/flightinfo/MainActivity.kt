package com.example.flightinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flightinfo.databinding.ActivityMainBinding
import com.example.flightinfo.ui.FlightsFragment
import com.example.flightinfo.ui.rate.ExchangeRatesFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        switchFragment(FlightsFragment())
        binding.bottomNavigationView.selectedItemId = R.id.navFlights

        initListener()
    }

    private fun initListener() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navFlights -> {
                    switchFragment(FlightsFragment())
                    true
                }
                R.id.navExchangeRate -> {
                    switchFragment(ExchangeRatesFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun switchFragment(target: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, target)

        transaction.commitAllowingStateLoss()
    }
}