package com.ganeshgundu.scalertest

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ganeshgundu.scalertest.db.WatchHistoryData

import com.ganeshgundu.scalertest.db.WatchHistoryDatabase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [29])
abstract class DatabaseTest {
    protected lateinit var appDatabase: WatchHistoryDatabase
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun initDb()  = runBlocking{
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WatchHistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()

            var row1 =
                WatchHistoryData(id = 100, Date(), 1, "Title 1", "Desc", "sources", "thumb")
            var row2 =
                WatchHistoryData(id = 101, Date(), 2, "Title 2", "Desc", "sources", "thumb")
            var row3 =
                WatchHistoryData(id = 102, Date(), 3, "Title 3", "Desc", "sources", "thumb")
            appDatabase.watchHistoryDao().updateWatchHistory(row1)
            appDatabase.watchHistoryDao().updateWatchHistory(row2)
            appDatabase.watchHistoryDao().updateWatchHistory(row3)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }
}