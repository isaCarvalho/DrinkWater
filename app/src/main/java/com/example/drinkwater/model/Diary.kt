package com.example.drinkwater.model

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.drinkwater.dao.DiaryDao
import com.example.drinkwater.util.DiaryHelper
import com.example.drinkwater.util.INFO_TAG
import java.time.LocalDate

data class Diary(
    private var totalWater : Float,
    private var totalWaterML : Float,
    private var qtWater : Float,
    private var percent : Float,
    private var date: String
)
{
    @RequiresApi(Build.VERSION_CODES.O)
    constructor() : this(0F, 0F, 0F, 0F, LocalDate.now().toString())

    @RequiresApi(Build.VERSION_CODES.O)
    private fun save(context: Context)
    {
        DiaryDao(context).insert(this)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setTotalWater(qt : Float, context: Context)
    {
        this.totalWater = qt
        this.totalWaterML = qt * 1000
        this.date = LocalDate.now().toString()

        val diary = DiaryDao(context).getByDate(LocalDate.now().toString())
        if (diary == null)
        {
            this.save(context)
        }
        else
        {
            DiaryDao(context).update(this)
        }

        Log.i(INFO_TAG, "this $this")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTotalWater(context: Context) : Float
    {
        val diary = DiaryDao(context).getByDate(LocalDate.now().toString())
        return diary?.totalWater ?: 0F
    }

    fun getTotalWater() = totalWater

    fun getTotalWaterML() = totalWaterML

    @RequiresApi(Build.VERSION_CODES.O)
    fun getTotalWaterML(context: Context) : Float
    {
        updateThis(context)

        val diary = DiaryDao(context).getByDate(LocalDate.now().toString())
        return diary?.totalWater?.times(1000) ?: 0F
    }

    fun getQtWater() = qtWater

    fun getPercent() = percent

    fun getDate() = date

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculatePercent(context: Context) : Float {

        updateThis(context)
        this.percent = if (totalWaterML != 0F)
            qtWater * 100 / totalWaterML
        else
            0F

        DiaryDao(context).update(this)

        return this.percent
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun clearValues(context: Context)
    {
        updateThis(context)

        qtWater = 0F
        totalWater = 0F
        totalWaterML = 0F

        DiaryDao(context).update(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun incrementWater(context: Context)
    {
        updateThis(context)

        this.qtWater = DiaryDao(context).getByDate(LocalDate.now().toString())?.qtWater ?: 0F
        if (qtWater <= totalWaterML)
            qtWater += 100

        DiaryDao(context).update(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPercent(context: Context) : Float
    {
        val diary = DiaryDao(context).getByDate(LocalDate.now().toString())
        return diary?.percent ?: 0F
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAll(context: Context) = DiaryDao(context).getAll()

    @RequiresApi(Build.VERSION_CODES.O)
    fun deleteAll(context: Context)
    {
        DiaryDao(context).deleteAll()
        updateThis(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateThis(context: Context)
    {
        val diary = DiaryDao(context).getByDate(LocalDate.now().toString())
        if (diary != null)
        {
            this.qtWater = diary.qtWater
            this.percent = diary.percent
            this.totalWater = diary.totalWater
            this.totalWaterML = diary.totalWaterML
        }
    }
}