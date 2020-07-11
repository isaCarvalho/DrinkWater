package com.example.drinkwater

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProviders
import com.example.drinkwater.util.WaterHelper
import com.example.drinkwater.viewModel.WaterViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: WaterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // view model
        viewModel = ViewModelProviders.of(this).get(WaterViewModel::class.java)

        val saveBtn = findViewById<Button>(R.id.saveBtn)
        saveBtn.setOnClickListener {
            val value = findViewById<EditText>(R.id.waterAmountEdit).text.toString().toFloat()
            WaterHelper.setTotalWater(value)

            finish()
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