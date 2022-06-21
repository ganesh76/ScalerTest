package com.ganeshgundu.scalertest.api

import com.squareup.moshi.Json

data class VideosApiData(
    @Json(name = "videos") val videos: List<VideoResult>,
    @Json(name = "name") val name: String
)