package com.ganeshgundu.scalertest.db

import androidx.room.*
import com.ganeshgundu.scalertest.api.VideoResult

@Dao
interface WatchHistoryDao {

    @Query("SELECT id,title,description,sources,thumb FROM watchHistory ORDER BY totalViewsCount DESC")
    fun getAllHistoryByMostViews(): List<VideoResult>

    @Query("SELECT id,title,description,sources,thumb FROM watchHistory ORDER BY lastViewedTimeStamp  DESC")
    fun getAllHistoryByRecentViews(): List<VideoResult>

    @Query("SELECT * FROM watchHistory WHERE id = :id")
    fun getHistoryById(id: Int): WatchHistoryData

    @Query("SELECT EXISTS(SELECT * FROM watchHistory WHERE id = :id)")
    fun hasRow(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchHistory(watchHistoryData: WatchHistoryData)

    @Update
    suspend fun updateWatchHistory(watchHistoryData: WatchHistoryData)

}