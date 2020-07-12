package com.example.drinkwater.model

import android.content.Context
import com.example.drinkwater.dao.DiaryDao
import java.util.*

data class Diary(
    private var id : Int,
    private var totalWater : Float,
    private var totalWaterML : Float,
    private var qtWater : Float,
    private var percent : Float,
    private var date: Date
)
{
    constructor() : this(-1, 0F, 0F, 0F, 0F, Date())

    private fun save(context: Context)
    {
        DiaryDao(context).insert(this)
    }

    fun setTotalWater(qt : Float, context: Context)
    {
        totalWater = qt
        totalWaterML = qt * 1000
        date = Date()

        val diary = DiaryDao(context).getByDate(Date())
        if (diary == null)
        {
            this.save(context)
        }
        else
        {
            this.id = diary.id
            DiaryDao(context).update(this)
        }
    }

    fun getTotalWater(context: Context) : Float
    {
        val diary = DiaryDao(context).getByDate(Date())
        return diary?.totalWater ?: 0F
    }

    fun getTotalWater() = totalWater

    fun getTotalWaterML() = totalWaterML

    fun getQtWater() = qtWater

    fun getPercent() = percent

    fun getDate() = date

    fun getId() = id

    fun calculatePercent(context: Context) : Float {
        this.percent = if (totalWaterML != 0F)
            qtWater * 100 / totalWaterML
        else
            0F

        DiaryDao(context).update(this)

        return this.percent
    }

    fun clearValues(context: Context)
    {
        qtWater = 0F
        totalWater = 0F
        totalWaterML = 0F

        DiaryDao(context).update(this)
    }

    fun incrementWater(context: Context)
    {
        if (qtWater <= totalWaterML)
            qtWater += 100

        DiaryDao(context).update(this)
    }
}