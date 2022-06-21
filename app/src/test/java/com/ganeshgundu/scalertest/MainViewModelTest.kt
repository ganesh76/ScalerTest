package com.ganeshgundu.scalertest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import com.ganeshgundu.scalertest.api.VideoResult
import com.ganeshgundu.scalertest.api.VideosApiData
import com.ganeshgundu.scalertest.db.WatchHistoryDatabase
import com.ganeshgundu.scalertest.repository.VideosRepository
import com.ganeshgundu.scalertest.repository.VideosRepositoryModel
import com.ganeshgundu.scalertest.viewmodel.MainViewModel
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [29])
@ExperimentalCoroutinesApi
class MainViewModelTest  {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val test = MainCoRule()

    private lateinit var videosRepository: VideosRepository
    // 2

    private lateinit var viewModel: MainViewModel



    protected lateinit var appDatabase: WatchHistoryDatabase

    @Mock
    private lateinit var apiVidsObserver: Observer<VideosRepositoryModel>

    @Mock
    private lateinit var loadingObserver: Observer<Boolean>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WatchHistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        videosRepository = VideosRepository(appDatabase.watchHistoryDao(),ApplicationProvider.getApplicationContext())
        viewModel = MainViewModel(
            videosRepository
        ).apply { showLoading.observeForever(loadingObserver)

            responseData.observeForever(apiVidsObserver) }
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() = test.runBlockingTest{
        val list = mutableListOf<VideoResult>()
        val videoResult = VideoResult(1,"title","desc","src","thumb")
        list.add(videoResult)
        val videosApiData = VideosApiData(list,"sample")
        val videosRepositoryModel = VideosRepositoryModel(VideosRepository.ResponseStatus.SUCCESS,
            videosApiData)
            whenever (videosRepository.getVideosFromApi()).thenReturn(videosRepositoryModel)
            viewModel.getMovies()
            verify(apiVidsObserver).onChanged(videosRepositoryModel)
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError()  = test.runBlockingTest {
        val videosRepositoryModel = VideosRepositoryModel(VideosRepository.ResponseStatus.API_ERROR,
            null)

            whenever (videosRepository.getVideosFromApi()).thenReturn(videosRepositoryModel)
            viewModel.getMovies()
            verify(apiVidsObserver).onChanged(videosRepositoryModel)

        }


    @After
    fun tearDown() {
        // do something if required
    }





    // 4


}