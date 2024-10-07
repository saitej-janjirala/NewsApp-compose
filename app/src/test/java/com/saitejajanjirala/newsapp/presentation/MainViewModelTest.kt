package com.saitejajanjirala.newsapp.presentation

import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.cash.turbine.test
import com.saitejajanjirala.newsapp.data.models.Article
import com.saitejajanjirala.newsapp.data.models.Source
import com.saitejajanjirala.newsapp.di.DispatcherProvider
import com.saitejajanjirala.newsapp.di.TestDispatcherProvider
import com.saitejajanjirala.newsapp.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import com.saitejajanjirala.newsapp.domain.models.Result
import com.saitejajanjirala.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var getNewsUseCase: GetNewsUseCase
    private lateinit var mainViewModel: MainViewModel
    private lateinit var stateHandle: SavedStateHandle
    private lateinit var dispatcher: DispatcherProvider

    @Before
    fun setUp() {
        getNewsUseCase = mock()
        stateHandle = mock()
        dispatcher= TestDispatcherProvider()
    }

    @Test
    fun `test updates items with success result`() = runTest{
        // Mock articles
        val articles = listOf(
            Article(
                author = "John Doe",
                content = "Sample content",
                description = "Sample description",
                publishedAt = "2024-10-02T04:45:00Z",
                source = Source(id = "source-id", name = "Source Name"),
                title = "Sample Title",
                url = "https://example.com",
                urlToImage = "https://example.com/image.jpg",
                id = 1
            )
        )

        val successResult = Result.Success(articles)

        whenever(getNewsUseCase.invoke("health")) doReturn flowOf(successResult)

        mainViewModel = MainViewModel(getNewsUseCase, stateHandle,dispatcher)

        mainViewModel.items.test {

            assertEquals(successResult,awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify(getNewsUseCase).invoke("health")

    }

    @Test
    fun `test updates items with error result`()= runTest {
        val error = "Unexpected error Occured"
        val errorResult = Result.Error<List<Article>>(error)
        whenever(getNewsUseCase.invoke("health")) doReturn flowOf(errorResult)
        mainViewModel = MainViewModel(getNewsUseCase, stateHandle,dispatcher)

        mainViewModel.items.test {
            assertEquals(errorResult,awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        verify(getNewsUseCase).invoke("health")
    }


}

