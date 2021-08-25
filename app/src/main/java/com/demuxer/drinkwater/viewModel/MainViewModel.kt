package com.demuxer.drinkwater.viewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.demuxer.drinkwater.util.DiaryHelper

@RequiresApi(Build.VERSION_CODES.O)
class MainViewModel(app: Application) : AndroidViewModel(app)
{
    var percent = MutableLiveData<Float>() // percent of water
    var totalWater = MutableLiveData<Float>()

    private val context = app
    private val waterHelper = DiaryHelper

    init {
        percent.value = waterHelper.getPercent(context)
        totalWater.value = waterHelper.getTotalWater(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun clearValues()
    {
        percent.value = 0F
        waterHelper.clearValues(context)
    }

    fun getTotalWater() : Float
    {
        totalWater.value = waterHelper.getTotalWater(context)
        return totalWater.value!!
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