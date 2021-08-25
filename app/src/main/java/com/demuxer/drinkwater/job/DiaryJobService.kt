package com.demuxer.drinkwater.job

import android.app.job.JobParameters
import android.app.job.JobService
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.demuxer.drinkwater.MainActivity
import com.demuxer.drinkwater.R
import com.demuxer.drinkwater.notification.NotificationService
import com.demuxer.drinkwater.util.DiaryHelper
import com.demuxer.drinkwater.util.JobHelper
import com.demuxer.drinkwater.util.NOTIFICATION_SUB_TITLE
import com.demuxer.drinkwater.util.NOTIFICATION_TITLE

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DiaryJobService : JobService()
{
    private val notificationService = NotificationService(applicationContext)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(params: JobParameters?): Boolean {

        if (DiaryHelper.isGoalAccomplished(applicationContext))
            return true

        if (JobHelper.isIntervalValid(JobHelper.initialHour, JobHelper.finalHour) &&
            JobHelper.verifyInterval(JobHelper.initialHour, JobHelper.finalHour))
        {
            notificationService
                .pushNotification(
                    NOTIFICATION_TITLE,
                    NOTIFICATION_SUB_TITLE,
                    BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_foreground),
                    R.mipmap.ic_launcher_foreground,
                    MainActivity::class.java
                )
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }
}