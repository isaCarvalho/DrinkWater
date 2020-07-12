package com.example.drinkwater.viewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.drinkwater.util.WaterHelper

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(app: Application) : AndroidViewModel(app)
{
    var percent = MutableLiveData<Float>() // percent of water

    private val context = app
    private val waterHelper = WaterHelper

    init {
        percent.value = waterHelper.getPercent(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun clearValues()
    {
        percent.value = 0F
        waterHelper.clearValues(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun incrementWater()
    {
        waterHelper.incrementWater(context)
        publishPercent()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun publishPercent()
    {
        percent.value = waterHelper.calculatePercent(context)
    }
}