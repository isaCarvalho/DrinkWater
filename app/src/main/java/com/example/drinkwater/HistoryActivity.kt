package com.example.drinkwater

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
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

        // list of rows
        val rows = ArrayList<TableRow>()
        var textsContent: ArrayList<String>

        // creating the table header
        textsContent = ArrayList()
        textsContent.add(resources.getString(R.string.total_water))
        textsContent.add(resources.getString(R.string.percent))
        textsContent.add(resources.getString(R.string.quantity))
        textsContent.add(resources.getString(R.string.date))

        rows.add(makeRow(textsContent))


        // creating the table data
        val list = WaterHelper.getAll(this)
        list?.forEach { item ->

            textsContent = ArrayList()
            textsContent.add(item.getTotalWater().toString())
            textsContent.add(item.getPercent().toString())
            textsContent.add(item.getQtWater().toString())
            textsContent.add(item.getDate())

            rows.add(this.makeRow(textsContent, list.indexOf(item) + 1))
        }

        // appending the row
        rows.forEach {
            tableLayout.addView(it)
        }
    }

    private fun makeRow(textsContent : ArrayList<String>, index : Int = 0) : TableRow
    {
        val lp = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )

        val row =  TableRow(this).apply {
            layoutParams = lp

            if ((index % 2) == 0)
                setBackgroundColor(resources.getColor(R.color.colorTextLight))
            else
                setBackgroundColor(resources.getColor(R.color.colorText))
        }

        // creating the text views
        val textViews = ArrayList<TextView>()

        textsContent.forEach { textContent ->
            textViews.add(makeTextView(textContent))
        }

        // appending the text views in the row
        textViews.forEach { textView -> row.addView(textView) }

        return row
    }

    private fun makeTextView(contentText : String) : TextView
    {
        return TextView(this).apply {
            text = contentText
            setTextColor(resources.getColor(R.color.colorBackground))
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(4,4,4,4)
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