package com.ganeshgundu.scalertest

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.robolectric.annotation.Config
import kotlin.coroutines.ContinuationInterceptor

@ExperimentalCoroutinesApi
@Config(sdk = [29])
class MainCoRule : TestWatcher(), TestCoroutineScope by TestCoroutineScope() {
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)
    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.()->Unit) {
        testScope.runBlockingTest {block()  }
    }

}