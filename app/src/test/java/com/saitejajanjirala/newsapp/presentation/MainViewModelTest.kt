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
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.saitejajanjirala.newsapp.domain.models.*
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle


@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private lateinit var getNewsUseCase: GetNewsUseCase
    private lateinit var mainViewModel: MainViewModel
    private lateinit var stateHandle: SavedStateHandle
    private lateinit var dispatcher: DispatcherProvider

    @Before
    fun setUp() {
        getNewsUseCase = mockk()
        stateHandle = mockk()
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

        coEvery {  getNewsUseCase.invoke("health")}returns flowOf(successResult)

        mainViewModel = MainViewModel(getNewsUseCase, stateHandle,dispatcher)

        advanceUntilIdle()
        val state = mainViewModel.items.take(1)
        val result = state.toList()
        assert(result[0] == successResult)
    }

    @Test
    fun `test updates items with error result`()= runTest {
        val error = "Unexpected error Occured"
        val errorResult = Result.Error<List<Article>>(error)
        coEvery {  getNewsUseCase.invoke("health")} returns  flowOf(errorResult)
        mainViewModel = MainViewModel(getNewsUseCase, stateHandle,dispatcher)
        val state = mainViewModel.items.take(1)
        val result = state.toList()
        assert(result[0] == errorResult)
    }


}

