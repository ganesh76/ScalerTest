package com.ganeshgundu.scalertest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ganeshgundu.scalertest.db.utils.TimeStampConverter

@Database(entities = [WatchHistoryData::class], version = 1, exportSchema = false)
@TypeConverters(TimeStampConverter::class)
abstract class WatchHistoryDatabase : RoomDatabase() {
    abstract fun watchHistoryDao(): WatchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: WatchHistoryDatabase? = null

        fun getDatabase(context: Context): WatchHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WatchHistoryDatabase::class.java,
                    "watchHistory_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
