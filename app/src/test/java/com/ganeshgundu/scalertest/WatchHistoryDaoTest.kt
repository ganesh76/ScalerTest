package com.ganeshgundu.scalertest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ganeshgundu.scalertest.db.WatchHistoryData
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(sdk = [29])
open class WatchHistoryDaoTest : DatabaseTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()



    @Test
    fun queryAllRowsByMostViewsTest()  = runBlocking {
        val id = appDatabase.watchHistoryDao().getAllHistoryByRecentViews()?.get(0).id
        assertEquals(id, 102)
    }

    @Test
    fun queryAllRowsByMostRecentTest() = runBlocking {
        val id = appDatabase.watchHistoryDao().getAllHistoryByRecentViews()?.get(0).id
        assertEquals(id, 102)
    }

    @Test
    fun updateNewWatchRowTest() = runBlocking {
        val date = Date()
        var row =
            WatchHistoryData(id = 1, date, 10, "Ray Wenderlich", "Desc", "sources", "thumb")
        appDatabase.watchHistoryDao().updateWatchHistory(row)
        assertEquals(
            appDatabase.watchHistoryDao().getAllHistoryByMostViews().get(0)?.title, "Ray Wenderlich"
        )
    }

    @Test
    fun hasRowTest() = runBlocking {
        appDatabase.watchHistoryDao().hasRow(1)
        assertEquals(
            appDatabase.watchHistoryDao().hasRow(1), true
        )
    }

    @Test
    fun getHistoryByIdRowTest() = runBlocking {
        appDatabase.watchHistoryDao().getHistoryById(1)
        assertEquals(
            appDatabase.watchHistoryDao().getHistoryById(1).id, 1
        )
    }
}