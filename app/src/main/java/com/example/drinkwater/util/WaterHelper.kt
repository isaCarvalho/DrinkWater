package com.example.drinkwater.util

class WaterHelper
{
    companion object
    {
        private var totalWater = 0F // total of water in L that a person will drink
        private var totalWaterML = 0F // total of water in ML that a person will drink
        private var qtWater = 0F // quantity of water in ML that the person already drunk

        fun setTotalWater(qt : Float)
        {
            totalWater = qt
            totalWaterML = qt * 1000
        }

        fun getTotalWater() = totalWater

        fun calculatePercent() : Float {
            return if (totalWaterML != 0F)
                qtWater * 100 / totalWaterML
            else
                0F
        }

        fun clearValues()
        {
            qtWater = 0F
            totalWater = 0F
            totalWaterML = 0F
        }

        fun incrementWater()
        {
            if (qtWater <= totalWaterML)
                qtWater += 100
        }
    }
}