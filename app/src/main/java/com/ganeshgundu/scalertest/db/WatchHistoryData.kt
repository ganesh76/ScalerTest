package com.ganeshgundu.scalertest.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "watchHistory")
class WatchHistoryData(
    @PrimaryKey() @NonNull val id: Int,
    var lastViewedTimeStamp: Date,
    var totalViewsCount: Int,
    val title: String,
    val description: String,
    var sources: String,
    var thumb: String
)