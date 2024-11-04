package com.saitejajanjirala.newsapp.data.repository

import app.cash.turbine.test
import com.saitejajanjirala.newsapp.data.models.Article
import com.saitejajanjirala.newsapp.data.models.HeadLine
import com.saitejajanjirala.newsapp.data.models.Source
import com.saitejajanjirala.newsapp.data.remote.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.saitejajanjirala.newsapp.domain.models.Result
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NewsRepositoryImplTest {

    private lateinit var apiService: ApiService
    private lateinit var newsRepository: NewsRepositoryImpl

    @Before
    fun setUp() {
        apiService = mockk()
        newsRepository = NewsRepositoryImpl(apiService)
    }



    @Test
    fun `test getNews() returns success result`()= runTest{
        val category = "health"
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
        val headline = HeadLine(
            articles = articles,
            status = "ok",
            totalResults = 1
        )
        coEvery { apiService.getHeadLines(category) } returns headline
        val result =  newsRepository.getNews(category).last()
        assert(result is Result.Success)
        assert(result.data==articles)
    }

    @Test
    fun `test getNews() returns error result`() = runTest {
        val category = "health"
        val errorMessage = "An unexpected error occurred"
        coEvery { apiService.getHeadLines(category) } throws Exception(errorMessage)
        val result = newsRepository.getNews(category).take(1).toList()
        advanceUntilIdle()
        assert(result[0] is Result.Error)
        assert(result[0].message==errorMessage)
        coVerify { apiService.getHeadLines(category) }

    }


    @After
    fun tearDown() {
    }
}