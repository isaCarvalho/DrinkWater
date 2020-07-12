package com.example.drinkwater.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.drinkwater.model.Diary

class WaterHelper
{
    companion object
    {
        @RequiresApi(Build.VERSION_CODES.O)
        private var diary = Diary()

        @RequiresApi(Build.VERSION_CODES.O)
        fun setTotalWater(context : Context, qt : Float)
        {
            diary.setTotalWater(qt, context)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getTotalWater(context: Context) = diary.getTotalWater(context)

        @RequiresApi(Build.VERSION_CODES.O)
        fun calculatePercent(context : Context) : Float {
            return diary.calculatePercent(context)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun clearValues(context : Context)
        {
            diary.clearValues(context)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun incrementWater(context : Context)
        {
            diary.incrementWater(context)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getAll(context: Context) = diary.getAll(context)

        @RequiresApi(Build.VERSION_CODES.O)
        fun getPercent(context: Context) = diary.getPercent(context)
    }
}