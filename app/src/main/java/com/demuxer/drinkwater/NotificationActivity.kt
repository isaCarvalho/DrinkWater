package com.demuxer.drinkwater

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES.O
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.demuxer.drinkwater.job.DiaryJobService
import com.demuxer.drinkwater.util.*
import com.demuxer.drinkwater.viewModel.NotificationViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_notification.*

class NotificationActivity : AppCompatActivity()
{
    private lateinit var viewModel : NotificationViewModel

    @RequiresApi(O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // view model
        viewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)

        viewModel.isNotificationOn.observe(this, Observer {
            if (it == "y")
                notificationBtn.text = STOP_NOTIFICATION
            else
                notificationBtn.text = START_NOTIFICATION
        })

        // Interval
        val interval = viewModel.calculatePeriod(false) * 60
        "Notification Interval: $interval min".also { intervalTxt.text = it }

        val initialHourString = findViewById<EditText>(R.id.initialHourEdit)
        val finalHourString = findViewById<EditText>(R.id.finalHourEdit)

        val initialValue = viewModel.getInitialHour()
        val finalValue = viewModel.getFinalHour()

        initialHourString.setText(initialValue.toString())
        finalHourString.setText(finalValue.toString())

        // buttons
        notificationBtn.setOnClickListener {
            viewModel.updateHours()

            val scheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

            if (viewModel.isNotificationOn.value == "y")
                cancelJob(scheduler)
            else
                scheduleJob(scheduler)

            viewModel.changeNotificationStatus()
        }

        intervalBtn.setOnClickListener {

            if (initialHourString.text.toString().isEmpty() || finalHourString.text.toString().isEmpty()) {
                Snackbar.make(intervalBtn, VALIDATION, Snackbar.LENGTH_SHORT)
                    .setAction(ACTION, null)
                    .show()

                return@setOnClickListener
            }

            viewModel.updateInterval()
            Toast.makeText(this, INTERVAL_UPDATED, Toast.LENGTH_SHORT)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun scheduleJob(scheduler: JobScheduler)
    {
        var period = 0F

        if (Build.VERSION.SDK_INT >= O) {
            period = viewModel.calculatePeriod()
        } else {
            finish()
        }

        val componentName = ComponentName(applicationContext, DiaryJobService::class.java)

        val builder = JobInfo.Builder(JOB_ID, componentName)
            .setRequiresCharging(false)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPeriodic(viewModel.convertToMilliseconds(period).toLong())
            .setPersisted(true)

        val resultCode = scheduler.schedule(builder.build())

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Toast.makeText(this, NOTIFICATION_ACTIVATED, Toast.LENGTH_SHORT).show()
            finish()
        }
        else
            Toast.makeText(this, NOTIFICATION_NOT_ACTIVATED, Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun cancelJob(scheduler : JobScheduler)
    {
        scheduler.cancelAll()
        Toast.makeText(this, NOTIFICATION_CANCELED, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun start(context: Context)
        {
            val intent = Intent(context, NotificationActivity::class.java)
            context.startActivity(intent)
        }
    }
}