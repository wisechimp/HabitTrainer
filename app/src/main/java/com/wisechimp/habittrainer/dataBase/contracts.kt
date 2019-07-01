package com.wisechimp.habittrainer.dataBase

import android.provider.BaseColumns

val DATABASE_NAME = "habittrainer.db"
val DATABASE_VERSION = 10

object HabitEntry : BaseColumns {
    val TABLE_NAME = "habit"
    val _ID = "id"
    val TITLE_COLUMN = "title"
    val DESCRIPTION_COLUMN = "description"
    val IMAGE_COLUMN = "image"
}
