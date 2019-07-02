package com.wisechimp.habittrainer.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.wisechimp.habittrainer.dataBase.HabitEntry.DESCRIPTION_COLUMN
import com.wisechimp.habittrainer.dataBase.HabitEntry.IMAGE_COLUMN
import com.wisechimp.habittrainer.dataBase.HabitEntry.TABLE_NAME
import com.wisechimp.habittrainer.dataBase.HabitEntry.TITLE_COLUMN
import com.wisechimp.habittrainer.dataBase.HabitEntry._ID
import com.wisechimp.habittrainer.recyclerView.Habit
import java.io.ByteArrayOutputStream

class HabitDatabaseTable (context: Context) {

    private val TAG = HabitDatabaseTable::class.java.simpleName
    private val databaseHelper = HabitTrainerDatabase(context)

    fun store(habit: Habit): Long {
        val database = databaseHelper.writableDatabase

        val values = ContentValues()
        with(values) {
            put(TITLE_COLUMN, habit.title)
            put(DESCRIPTION_COLUMN, habit.description)
            put(IMAGE_COLUMN, imageToByteArray(habit.image))
        }

        val id = database.newTransaction {
            insert(TABLE_NAME, null, values)
        }

        Log.d(TAG, "Stored new habit to the Database $habit")

        return id
    }

    fun readAllHabits(): List<Habit> {
        val columns = arrayOf(
            _ID,
            TITLE_COLUMN,
            DESCRIPTION_COLUMN,
            IMAGE_COLUMN
        )
        val order = "$_ID ASC"
        val database = databaseHelper.readableDatabase
        val cursor = database.defaultQuery(TABLE_NAME, columns, orderBy = order)
        return parseHabitsFromCursor(cursor)
    }

    private fun parseHabitsFromCursor(cursor: Cursor): MutableList<Habit> {
        val habits = mutableListOf<Habit>()
        while (cursor.moveToNext()) {
            val title = cursor.getHabitString(TITLE_COLUMN)
            val description = cursor.getHabitString(DESCRIPTION_COLUMN)
            val bitmap = cursor.getHabitBitmap(IMAGE_COLUMN)
            habits.add(Habit(title, description, bitmap))
        }
        cursor.close()
        return habits
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

private fun SQLiteDatabase.defaultQuery(table: String, columns: Array<String>, selection: String? = null, selectionArgs: Array<String>? = null, groupBy: String? = null, having: String? = null, orderBy: String): Cursor {
    return query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
}

private fun Cursor.getHabitString(columnName: String) = this.getString(getColumnIndex(columnName))

private fun Cursor.getHabitBitmap(columnName: String): Bitmap {
    val byteArray = getBlob(getColumnIndex(columnName))
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}