package com.ganeshgundu.scalertest.app

import android.app.Application
import com.ganeshgundu.scalertest.db.WatchHistoryDatabase
import com.ganeshgundu.scalertest.repository.VideosRepository

class ScalerTest : Application() {
    val database by lazy { WatchHistoryDatabase.getDatabase(this) }
    val repository by lazy { VideosRepository(database.watchHistoryDao(), this) }
}