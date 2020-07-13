package com.example.drinkwater.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.drinkwater.util.JobHelper

class NotificationViewModel(app : Application) : AndroidViewModel(app)
{
    var isNotificationOn = MutableLiveData<String>() // notification status

    private val context = app
    private val jobHelper = JobHelper

    init {
        isNotificationOn.value = jobHelper.getNotificationStatus(context)
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