package com.example.drinkwater

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.drinkwater.util.WaterHelper

class HistoryActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        val list = WaterHelper.getAll(this)
        list?.forEach { item ->
            val row =  TableRow(this)
            val lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
            row.layoutParams = lp

            val totalWaterTxt = TextView(this)
            totalWaterTxt.text = "${item.getTotalWater()} L"

            val totalWaterMLTxt = TextView(this)
            totalWaterMLTxt.text = "${item.getTotalWaterML()} ml"

            val percentTxt = TextView(this)
            percentTxt.text = "${item.getPercent()} %"

            val qtWaterTxt = TextView(this)
            qtWaterTxt.text = "${item.getQtWater()} ml"

            val dateTxt = TextView(this)
            dateTxt.text = item.getDate()

            row.addView(totalWaterTxt)
            row.addView(totalWaterMLTxt)
            row.addView(percentTxt)
            row.addView(qtWaterTxt)
            row.addView(dateTxt)

            tableLayout.addView(row, list.indexOf(item))
        }
    }

    companion object
    {
        fun start(context: Context)
        {
            val intent = Intent(context, HistoryActivity::class.java)
            context.startActivity(intent)
        }
    }
}