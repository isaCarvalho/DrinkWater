package com.example.drinkwater.dao

import android.content.ContentValues
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.drinkwater.model.Diary
import com.example.drinkwater.util.*
import kotlin.collections.ArrayList

class DiaryDao(context: Context)
{
    private val dbHelper = DatabaseCreator(context)

    fun insert(diary : Diary)
    {
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues().apply {
            put(TOTAL_WATER, diary.getTotalWater())
            put(TOTAL_WATER_ML, diary.getTotalWaterML())
            put(QT_WATER, diary.getQtWater())
            put(PERCENT, diary.getPercent())
            put(DATE, diary.getDate())
        }

        db?.insert(DIARY_TABLE, null, contentValues)
    }

    fun update(diary: Diary)
    {
        val db = dbHelper.writableDatabase

        val contentValues = ContentValues().apply {
            put(TOTAL_WATER, diary.getTotalWater())
            put(TOTAL_WATER_ML, diary.getTotalWaterML())
            put(QT_WATER, diary.getQtWater())
            put(PERCENT, diary.getPercent())
        }

        db?.update(DIARY_TABLE, contentValues, "$DATE = ?", arrayOf(diary.getDate()))
    }

    fun deleteAll()
    {
        dbHelper.writableDatabase.execSQL("DELETE FROM $DIARY_TABLE")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getByDate(date : String) : Diary?
    {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(TOTAL_WATER, TOTAL_WATER_ML, QT_WATER, PERCENT, DATE)

        val cursor = db?.query(DIARY_TABLE, projection, "$DATE = ?", arrayOf(date),
            null, null, null )

        var diary : Diary? = null
        with(cursor!!)
        {
            while (moveToNext())
            {
                val totalWater = getFloat(getColumnIndexOrThrow(TOTAL_WATER))
                val totalWaterML = getFloat(getColumnIndexOrThrow(TOTAL_WATER_ML))
                val qtWater = getFloat(getColumnIndexOrThrow(QT_WATER))
                val percent = getFloat(getColumnIndexOrThrow(PERCENT))
                val dateDiary = getString(getColumnIndexOrThrow(DATE))

                diary = Diary(totalWater, totalWaterML, qtWater, percent, dateDiary)
            }
        }

        cursor.close()
        return diary
    }

    fun getAll() : ArrayList<Diary>?
    {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(TOTAL_WATER, TOTAL_WATER_ML, QT_WATER, PERCENT, DATE)

        val cursor = db?.query(DIARY_TABLE, projection, null, null,
            null, null, null )

        val diary = ArrayList<Diary>()
        with(cursor!!)
        {
            while (moveToNext())
            {
                val totalWater = getFloat(getColumnIndexOrThrow(TOTAL_WATER))
                val totalWaterML = getFloat(getColumnIndexOrThrow(TOTAL_WATER_ML))
                val qtWater = getFloat(getColumnIndexOrThrow(QT_WATER))
                val percent = getFloat(getColumnIndexOrThrow(PERCENT))
                val dateDiary = getString(getColumnIndexOrThrow(DATE))

                diary.add(Diary(totalWater, totalWaterML, qtWater, percent, dateDiary))
            }
        }

        cursor.close()
        return diary
    }
}