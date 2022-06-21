package com.ganeshgundu.scalertest.api


import com.ganeshgundu.scalertest.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BuildConfig.BASE_URL)
    .build()

interface VideosApiService {
    @GET("/b/26L2")
    suspend fun getVideos(): Response<VideosApiData>
}


object VideosApiNetworkHelper {
    val videosApiService: VideosApiService = retrofit.create(VideosApiService::class.java)
}