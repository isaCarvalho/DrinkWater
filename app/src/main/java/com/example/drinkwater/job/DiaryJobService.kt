package com.example.drinkwater.job

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.drinkwater.MainActivity
import com.example.drinkwater.R
import com.example.drinkwater.notification.NotificationService
import com.example.drinkwater.util.DiaryHelper
import com.example.drinkwater.util.JobHelper

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class DiaryJobService : JobService()
{
    private var jobCancelled = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartJob(params: JobParameters?): Boolean {
        doBackgroundWork(params)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        jobCancelled = true
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun doBackgroundWork(params : JobParameters?)
    {
        Thread(Runnable {
            if (jobCancelled)
                return@Runnable

            if (DiaryHelper.isGoalAccomplished(applicationContext))
                return@Runnable

            if (JobHelper.isIntervalValid(JobHelper.initialHour, JobHelper.finalHour))
            {
                if (JobHelper.verifyInterval(JobHelper.initialHour, JobHelper.finalHour))
                {
                    NotificationService.getInstance(applicationContext)
                        .pushNotification(
                            "You have to drink 100 ml of water now",
                            "It's time to DrinkWater",
                            BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_foreground),
                            R.mipmap.ic_launcher_foreground,
                            MainActivity::class.java
                        )
                }
            }

            jobFinished(params, false)
        })
    }
}