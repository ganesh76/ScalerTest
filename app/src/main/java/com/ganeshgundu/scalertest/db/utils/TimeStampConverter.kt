package com.ganeshgundu.scalertest.db.utils

import androidx.room.TypeConverter

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class TimeStampConverter {
    val TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss"
    var df: DateFormat = SimpleDateFormat(TIME_STAMP_FORMAT)

    @TypeConverter
    fun fromTimestamp(value: String): Date {
        return df.parse(value)
    }

    @TypeConverter
    fun dateToTimestamp(value: Date): String {
        return df.format(value)
    }
}