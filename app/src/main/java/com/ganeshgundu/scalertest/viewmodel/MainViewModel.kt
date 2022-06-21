package com.ganeshgundu.scalertest.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ganeshgundu.scalertest.api.VideoResult
import com.ganeshgundu.scalertest.repository.VideosRepository
import com.ganeshgundu.scalertest.repository.VideosRepositoryModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel(val repository: VideosRepository) : ViewModel() {

    val responseData = MutableLiveData<VideosRepositoryModel>()
    val showLoading = MutableLiveData<Boolean>()
    val videoRes = MutableLiveData<VideoResult>()
    val watchHistoryList = MutableLiveData<List<VideoResult>>()


    fun getMovies() {
        showLoading.value = true
        viewModelScope.launch {
            val videoRepositoryModel = repository.getVideosFromApi()
            showLoading.value = false
            responseData.value = videoRepositoryModel
        }
    }

    fun getMostRecentWatchHistory() {
        showLoading.value = true
        viewModelScope.launch {
            showLoading.value = false
            watchHistoryList.value = repository.getMostRecentWatchHistory()
        }
    }

    fun getMostViewedWatchHistory() {
        showLoading.value = true
        viewModelScope.launch {
            showLoading.value = false
            watchHistoryList.value = repository.getMostViewedWatchHistory()
        }
    }

    fun sendVideoData(videoResult: VideoResult) {
        videoRes.value = videoResult
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertOrUpdateDb(videoResult)
        }
    }

    class MainViewModelFactory(private val repository: VideosRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
