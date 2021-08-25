package com.demuxer.drinkwater.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.demuxer.drinkwater.model.Diary

class DiaryHelper
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
        fun getTotalWaterML(context: Context) = diary.getTotalWaterML(context)

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

        @RequiresApi(Build.VERSION_CODES.O)
        fun deleteAll(context: Context) = diary.deleteAll(context)

        @RequiresApi(Build.VERSION_CODES.O)
        fun isGoalAccomplished(context: Context) : Boolean
        {
            val percent = getPercent(context)
            if (percent >= 100F)
                return true

            return false
        }
    }
}