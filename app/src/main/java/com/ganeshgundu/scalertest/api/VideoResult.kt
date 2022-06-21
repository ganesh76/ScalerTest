package com.ganeshgundu.scalertest.api

import com.squareup.moshi.Json

data class VideoResult(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String,
    @Json(name = "sources") val sources: String,
    @Json(name = "thumb") val thumb: String,
)