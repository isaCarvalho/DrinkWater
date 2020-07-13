package com.example.drinkwater

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.drinkwater.job.DiaryJobService
import com.example.drinkwater.util.DiaryHelper
import com.example.drinkwater.util.JOB_ID
import com.example.drinkwater.util.JOB_TAG
import com.example.drinkwater.util.JobHelper
import com.example.drinkwater.viewModel.NotificationViewModel
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity()
{
    private lateinit var viewModel : NotificationViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // view model
        viewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)

        viewModel.isNotificationOn.observe(this, Observer {
            if (it == "y")
                notificationBtn.text = "Stop Notification"
            else
                notificationBtn.text = "Start Notification"
        })

        // buttons
        notificationBtn.setOnClickListener {
            if (viewModel.isNotificationOn.value == "y")
                cancelJob()
            else
                scheduleJob()

            viewModel.changeNotificationStatus()
        }

        intervalBtn.setOnClickListener {
            val initialHour = findViewById<EditText>(R.id.initialHourEdit).text.toString().toInt()
            val finalHour = findViewById<EditText>(R.id.finalHourEdit).text.toString().toInt()

            JobHelper.updateInterval(this, initialHour, finalHour)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun scheduleJob()
    {
        val period = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            JobHelper.calculatePeriod(this, DiaryHelper.getTotalWaterML(this))
        } else {
            JobHelper.calculatePeriod(this, 2000F)
        }

        val componentName = ComponentName(this, DiaryJobService::class.java)
        val info = JobInfo.Builder(JOB_ID, componentName)
            .setRequiresCharging(false)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPersisted(true)
            .setPeriodic(period.toLong())
            .build()

        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Toast.makeText(this, "Notification scheduled", Toast.LENGTH_SHORT).show()
            finish()
        }
        else
            Toast.makeText(this, "Notification failed to schedule", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun cancelJob()
    {
        val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(JOB_ID)

        Log.i(JOB_TAG, "Canceled")
    }

    companion object {
        fun start(context: Context)
        {
            val intent = Intent(context, NotificationActivity::class.java)
            context.startActivity(intent)
        }
    }
}