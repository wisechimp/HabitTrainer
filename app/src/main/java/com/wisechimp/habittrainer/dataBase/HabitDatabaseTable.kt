package com.wisechimp.habittrainer.dataBase

import android.content.ContentValues
import android.content.Context
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
        values.put(HabitEntry.TITLE_COLUMN, habit.title)
        values.put(HabitEntry.DESCRIPTION_COLUMN, habit.description)
        values.put(HabitEntry.IMAGE_COLUMN, imageToByteArray(habit.image))

        val id = database.insert(HabitEntry.TABLE_NAME, null, values)
        database.close()
        Log.d(TAG, "Stored new habit to the Database $habit")

        return id
    }

    private fun imageToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }
}