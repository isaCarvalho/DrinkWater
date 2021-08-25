package com.demuxer.drinkwater.model

import android.content.Context
import com.demuxer.drinkwater.dao.NotificationDao

data class Notification(
    var isNotificationOn : String,
    var initialHour : Int,
    var finalHour : Int
)
{
    constructor() : this("n", 8, 24)

    fun getNotificationSettings(context: Context) : Notification
    {
        val notification = NotificationDao(context).getNotificationSetting()

        this.isNotificationOn = notification.isNotificationOn
        this.initialHour = notification.initialHour
        this.finalHour = notification.finalHour

        return notification
    }

    fun updateInterval(context: Context, initialHour: Int, finalHour: Int)
    {
        this.initialHour = initialHour
        this.finalHour = finalHour

        NotificationDao(context).update(this)
    }

    fun changeNotificationStatus(context: Context)
    {
        if (this.isNotificationOn == "y")
            this.isNotificationOn = "n"
        else
            this.isNotificationOn = "y"

        NotificationDao(context).update(this)
    }
}