package com.example.drinkwater.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import com.example.drinkwater.util.CHANNEL_ID
import com.example.drinkwater.util.DESCRIPTION

class NotificationService private constructor(private val context: Context)
{
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder : Notification.Builder

    private val channelId = CHANNEL_ID
    private val description = DESCRIPTION

    fun pushNotification(contentText : CharSequence, contentTitle: CharSequence,
                         largeIcon : Bitmap, smallIcon : Int, cls : Class<*>)
    {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, cls::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                .apply {
                    enableLights(true)
                    lightColor = Color.GREEN
                    enableVibration(true)
                }

            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(context, channelId)
        }
        else
            builder = Notification.Builder(context)

        builder.setContentTitle(contentTitle)
            .setContentText(contentText)
            .setLargeIcon(largeIcon)
            .setSmallIcon(smallIcon)
            .setContentIntent(pendingIntent)

        notificationManager.notify(0, builder.build())
    }

    companion object {
        private var instance : NotificationService? = null

        fun getInstance(context: Context) : NotificationService
        {
            if (instance == null)
                instance = NotificationService(context)

            return instance!!
        }
    }
}