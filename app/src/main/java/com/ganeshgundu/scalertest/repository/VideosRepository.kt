package com.ganeshgundu.scalertest.repository

import android.content.Context
import com.ganeshgundu.scalertest.api.VideoResult
import com.ganeshgundu.scalertest.api.VideosApiNetworkHelper
import com.ganeshgundu.scalertest.db.WatchHistoryDao
import com.ganeshgundu.scalertest.db.WatchHistoryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*


class VideosRepository(val watchHistoryDao: WatchHistoryDao, val context: Context) {

    enum class ResponseStatus() {
        API_ERROR,
        SUCCESS
    }

    suspend fun getVideosFromApi(): VideosRepositoryModel {
        val response = VideosApiNetworkHelper.videosApiService.getVideos()
        response.body()?.let {
            if (response.isSuccessful) {
                return VideosRepositoryModel(ResponseStatus.SUCCESS, response.body()!!)
            } else {
                return VideosRepositoryModel(ResponseStatus.API_ERROR, null)
            }
        } ?: return VideosRepositoryModel(ResponseStatus.API_ERROR, null)
    }

    suspend fun getMostRecentWatchHistory(): List<VideoResult>? {
        lateinit var videoResultDb: List<VideoResult>
        withContext(Dispatchers.IO) {
            videoResultDb = watchHistoryDao.getAllHistoryByRecentViews()
        }
        if (videoResultDb.isEmpty()) {
            return null
        } else {
            return videoResultDb
        }
    }

    suspend fun getMostViewedWatchHistory(): List<VideoResult>? {
        lateinit var videoResultDb: List<VideoResult>
        withContext(Dispatchers.IO) {
            videoResultDb = watchHistoryDao.getAllHistoryByMostViews()
        }
        if (videoResultDb.isEmpty()) {
            return null
        } else {
            return videoResultDb
        }
    }

    suspend fun insertOrUpdateDb(videoResult: VideoResult) {

        if (!watchHistoryDao.hasRow(videoResult.id)) {
            val watchHistoryData = WatchHistoryData(
                videoResult.id,
                Date(),
                1,
                videoResult.title,
                videoResult.description,
                videoResult.sources,
                videoResult.thumb
            )
            watchHistoryDao.insertWatchHistory(watchHistoryData)
        } else {
            val watchHistoryData = watchHistoryDao.getHistoryById(videoResult.id)
            val newCount = watchHistoryData.totalViewsCount + 1
            watchHistoryData.totalViewsCount = newCount
            watchHistoryData.lastViewedTimeStamp = Date()
            watchHistoryDao.updateWatchHistory(watchHistoryData)
        }
    }

}

