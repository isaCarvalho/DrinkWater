package com.example.drinkwater

import android.annotation.SuppressLint
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES.O
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.drinkwater.job.DiaryJobService
import com.example.drinkwater.util.*
import com.example.drinkwater.viewModel.NotificationViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity()
{
    private lateinit var viewModel : NotificationViewModel

    @RequiresApi(O)
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

        // Interval
        val interval = viewModel.calculatePeriod(false) * 60
        intervalTxt.text = "Notification Interval: $interval min"

        // buttons
        notificationBtn.setOnClickListener {
            viewModel.updateHours()

            if (viewModel.isNotificationOn.value == "y")
                cancelJob()
            else
                scheduleJob()

            viewModel.changeNotificationStatus()
        }

        intervalBtn.setOnClickListener {
            val initialHourString = findViewById<EditText>(R.id.initialHourEdit).text.toString()
            val finalHourString = findViewById<EditText>(R.id.finalHourEdit).text.toString()

            if (initialHourString.isEmpty() || finalHourString.isEmpty()) {
                Snackbar.make(intervalBtn, "Please, enter valid values!", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show()

                return@setOnClickListener
            }

            viewModel.updateInterval()
            Toast.makeText(this, "Interval updated", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun scheduleJob()
    {
        var period = 0F

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            period = viewModel.calculatePeriod()
        } else {
            finish()
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