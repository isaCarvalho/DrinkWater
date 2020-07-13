package com.example.drinkwater.viewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.drinkwater.util.DiaryHelper
import com.example.drinkwater.util.JobHelper

class NotificationViewModel(app : Application) : AndroidViewModel(app)
{
    var isNotificationOn = MutableLiveData<String>() // notification status
    var initialHour = MutableLiveData<Int>()
    var finalHour = MutableLiveData<Int>()

    private val context = app
    private val jobHelper = JobHelper

    init {
        isNotificationOn.value = jobHelper.getNotificationStatus(context)
    }

    fun updateHours()
    {
        JobHelper.updateHours(context)
        initialHour.value = JobHelper.initialHour
        finalHour.value = JobHelper.finalHour
    }

    fun updateInterval()
    {
        JobHelper.updateInterval(context, initialHour.value!!, finalHour.value!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculatePeriod(defaultValue : Boolean = true) : Float
    {
        return if (defaultValue)
            JobHelper.calculatePeriod(context, DiaryHelper.getTotalWaterML(context))
        else
            JobHelper.calculatePeriod(context, 2000F)
    }

    fun changeNotificationStatus()
    {
        jobHelper.changeNotificationStatus(context)
        publishStatus()
    }

    private fun publishStatus()
    {
        isNotificationOn.value = jobHelper.getNotificationStatus(context)
    }
}