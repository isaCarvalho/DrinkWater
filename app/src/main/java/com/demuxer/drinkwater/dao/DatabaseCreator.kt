package com.demuxer.drinkwater.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.demuxer.drinkwater.util.*

class DatabaseCreator(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
){
    override fun onCreate(db: SQLiteDatabase?) {
        var sql = "CREATE TABLE $NOTIFICATION_TABLE (" +
                "$IS_NOTIFICATION_ON TEXT NOT NULL PRIMARY KEY, " +
                "$INITIAL_HOUR INT NOT NULL, " +
                "$FINAL_HOUR INT NOT NULL )"

        db?.execSQL(sql)

        sql = "CREATE TABLE $DIARY_TABLE (" +
                "$TOTAL_WATER FLOAT NOT NULL, " +
                "$TOTAL_WATER_ML FLOAT NOT NULL, " +
                "$QT_WATER FLOAT NOT NULL, " +
                "$PERCENT FLOAT NOT NULL, " +
                "$DATE TEXT NOT NULL PRIMARY KEY )"

        db?.execSQL(sql)

        sql = "INSERT INTO $NOTIFICATION_TABLE ($IS_NOTIFICATION_ON, $INITIAL_HOUR, $FINAL_HOUR) " +
                "VALUES ('n', 8, 24)"

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val sql = "DROP TABLE IF EXISTS $DIARY_TABLE;" +
                "DROP TABLE IF EXISTS $NOTIFICATION_TABLE;"

        db?.execSQL(sql)
        onCreate(db)
    }
}