package com.example.drinkwater.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.drinkwater.util.WaterHelper

class WaterViewModel(app: Application) : AndroidViewModel(app)
{
    var percent = MutableLiveData<Float>() // percent of water

    private val context = app
    private val waterHelper = WaterHelper

    init {
        clearValues()
    }

    fun clearValues()
    {
        percent.value = 0F
        waterHelper.clearValues()
    }

    fun incrementWater()
    {
        waterHelper.incrementWater()
        publishPercent()
    }

    private fun publishPercent()
    {
        percent.value = waterHelper.calculatePercent()
    }
}