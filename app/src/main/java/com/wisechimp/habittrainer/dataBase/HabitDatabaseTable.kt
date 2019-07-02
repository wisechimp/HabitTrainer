package com.wisechimp.habittrainer.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.util.Log
import com.wisechimp.habittrainer.recyclerView.Habit
import java.io.ByteArrayOutputStream

class HabitDatabaseTable (context: Context) {

    private val TAG = HabitDatabaseTable::class.java.simpleName
    private val databaseHelper = HabitTrainerDatabase(context)

    fun store(habit: Habit): Long {
        val database = databaseHelper.writableDatabase

        val values = ContentValues()
        with(values) {
            put(HabitEntry.TITLE_COLUMN, habit.title)
            put(HabitEntry.DESCRIPTION_COLUMN, habit.description)
            put(HabitEntry.IMAGE_COLUMN, imageToByteArray(habit.image))
        }

        val id = database.newTransaction {
            insert(HabitEntry.TABLE_NAME, null, values)
        }

        Log.d(TAG, "Stored new habit to the Database $habit")

        return id
    }

    private fun imageToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
}

private inline fun <T> SQLiteDatabase.newTransaction(function: SQLiteDatabase.() -> T): T {
    beginTransaction()
    val result = try {
        val returnValue = function()
        setTransactionSuccessful()
        returnValue
    } finally {
        endTransaction()
    }
    close()
    return result
}