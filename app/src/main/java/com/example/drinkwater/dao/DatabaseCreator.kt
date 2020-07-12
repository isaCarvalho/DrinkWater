package com.example.drinkwater.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.drinkwater.util.*

class DatabaseCreator(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
){
    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE $TABLE_NAME (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY NOT NULL, " +
                "$TOTAL_WATER FLOAT NOT NULL, " +
                "$TOTAL_WATER_ML FLOAT NOT NULL, " +
                "$QT_WATER FLOAT NOT NULL, " +
                "$PERCENT FLOAT NOT NULL, " +
                "$DATE TEXT NOT NULL )"

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        var sql = "DROP TABLE IF EXISTS $TABLE_NAME"

        db?.execSQL(sql)
        onCreate(db)
    }
}