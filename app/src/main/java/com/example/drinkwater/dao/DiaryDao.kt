package com.example.drinkwater.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.example.drinkwater.model.Diary
import com.example.drinkwater.util.*
import java.util.*
import kotlin.collections.ArrayList

class DiaryDao(private val context: Context)
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
            put(DATE, diary.getDate().toString())
        }

        db?.insert(TABLE_NAME, null, contentValues)
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

        db?.update(TABLE_NAME, contentValues, "${BaseColumns._ID} = ?", arrayOf(diary.getId().toString()))
    }

    fun deleteAll()
    {
        dbHelper.writableDatabase.execSQL("DELETE FROM $TABLE_NAME")
    }

    fun getByDate(date : Date) : Diary?
    {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, TOTAL_WATER, TOTAL_WATER_ML, QT_WATER,
            PERCENT, DATE)

        val cursor = db?.query(TABLE_NAME, projection, "$DATE = ?", arrayOf(date.toString()),
            null, null, null )

        return getSelection(cursor!!, false) as Diary
    }

    fun getAll() : ArrayList<Diary>?
    {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(BaseColumns._ID, TOTAL_WATER, TOTAL_WATER_ML, QT_WATER,
            PERCENT, DATE)

        val cursor = db?.query(TABLE_NAME, projection, null, null,
            null, null, null )

        return getSelection(cursor!!) as ArrayList<Diary>
    }

    private fun getSelection(cursor : Cursor, isArray : Boolean = true) : Any?
    {
        var diary : Any? = if (isArray) ArrayList<Diary>() else Diary()

        with(cursor)
        {
            while (moveToNext())
            {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val totalWater = getFloat(getColumnIndexOrThrow(TOTAL_WATER))
                val totalWaterML = getFloat(getColumnIndexOrThrow(TOTAL_WATER_ML))
                val qtWater = getFloat(getColumnIndexOrThrow(QT_WATER))
                val percent = getFloat(getColumnIndexOrThrow(PERCENT))
                val dateDiary = getString(getColumnIndexOrThrow(DATE))

                if (diary!!.javaClass.isInstance(Diary()))
                    diary = Diary(id, totalWater, totalWaterML, qtWater, percent, Date(dateDiary))
                else
                    (diary as ArrayList<Diary>).add(Diary(id, totalWater, totalWaterML, qtWater, percent, Date(dateDiary)))
            }
        }

        cursor.close()
        return diary
    }
}