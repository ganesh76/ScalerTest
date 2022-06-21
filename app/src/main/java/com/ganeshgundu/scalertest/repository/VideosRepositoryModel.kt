package com.ganeshgundu.scalertest.repository

import com.ganeshgundu.scalertest.api.VideosApiData


data class VideosRepositoryModel(
    val responseStatus: VideosRepository.ResponseStatus,
    val response: VideosApiData?
)