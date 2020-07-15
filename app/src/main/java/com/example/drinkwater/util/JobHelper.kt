package com.example.drinkwater.util

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.drinkwater.model.Notification
import java.time.LocalTime

class JobHelper
{
    companion object
    {
        private val notification = Notification()
        var initialHour = 8
        var finalHour = 24

        fun updateHours(context: Context)
        {
            initialHour = notification.getNotificationSettings(context).initialHour
            finalHour = notification.getNotificationSettings(context).finalHour
        }

        fun getNotificationStatus(context: Context) = notification.getNotificationSettings(context).isNotificationOn

        fun updateInterval(context: Context, initialHour : Int, finalHour: Int)
        {
            notification.updateInterval(context, initialHour, finalHour)
        }

        fun calculatePeriod(context: Context, totalWaterML : Float) : Float
        {
            val initialHour = notification.getNotificationSettings(context).initialHour

            val finalHour = notification.getNotificationSettings(context).finalHour

            val interval = finalHour - initialHour
            return ((interval * AMOUNT_ML) / totalWaterML)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun verifyInterval(initialHour: Int, finalHour: Int) : Boolean
        {
            if (LocalTime.now().hour in initialHour..finalHour)
                return true

            return false
        }

        fun isIntervalValid(initialHour: Int, finalHour: Int) : Boolean
        {
            if (initialHour < 8)
                return false

            if (finalHour > 24)
                return false

            return true
        }

        fun changeNotificationStatus(context: Context)
        {
            notification.changeNotificationStatus(context)
        }
    }
}