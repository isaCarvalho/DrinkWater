package com.example.drinkwater.util

import android.content.Context
import com.example.drinkwater.model.Diary

class WaterHelper
{
    companion object
    {
        private var diary = Diary()

        fun setTotalWater(context : Context, qt : Float)
        {
            diary.setTotalWater(qt, context)
        }

        fun getTotalWater(context: Context) = diary.getTotalWater(context)

        fun calculatePercent(context : Context) : Float {
            return diary.calculatePercent(context)
        }

        fun clearValues(context : Context)
        {
            diary.clearValues(context)
        }

        fun incrementWater(context : Context)
        {
            diary.incrementWater(context)
        }
    }
}