package com.demuxer.drinkwater

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.demuxer.drinkwater.util.ACTION
import com.demuxer.drinkwater.util.DiaryHelper
import com.demuxer.drinkwater.util.VALIDATION_AMOUNT
import com.google.android.material.snackbar.Snackbar

class SettingsActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val saveBtn = findViewById<Button>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            val value = findViewById<EditText>(R.id.waterAmountEdit).text.toString()

            if (value.isNotEmpty())
            {
                DiaryHelper.setTotalWater(this, value.toFloat())
                finish()
            }
            else {
                Snackbar.make(saveBtn, VALIDATION_AMOUNT , Snackbar.LENGTH_SHORT)
                    .setAction(ACTION, null)
                    .show()
            }
        }
    }

    companion object
    {
        fun start(context: Context)
        {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }
}