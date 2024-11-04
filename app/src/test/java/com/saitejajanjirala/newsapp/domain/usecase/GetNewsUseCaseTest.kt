package com.saitejajanjirala.newsapp.domain.usecase

import app.cash.turbine.test
import com.saitejajanjirala.newsapp.data.models.Article
import com.saitejajanjirala.newsapp.data.models.HeadLine
import com.saitejajanjirala.newsapp.data.models.Source
import com.saitejajanjirala.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import com.saitejajanjirala.newsapp.domain.models.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class GetNewsUseCaseTest {

    private lateinit var newsRepository: NewsRepository
    private lateinit var getNewsUseCase: GetNewsUseCase

    @Before
    fun setUp() {
        // Here, we mock the repository
        newsRepository = mockk()
        getNewsUseCase = GetNewsUseCase(newsRepository) // Inject the mock repository into the use case
    }

    @Test
    fun `test GetNewsUseCase returns success result`() = runTest {
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

        // Mock the repository to return a flow emitting the success result
        coEvery { (newsRepository.getNews("health"))} returns flowOf(successResult)

        // Act: Call the use case
        val result = getNewsUseCase.invoke("health").take(1)

        // Assert: Verify the result
        advanceUntilIdle()
        assertEquals(successResult,result.toList()[0])



    }

    @Test
    fun `test GetNewsUseCase returns error result`() = runTest{
        val error = "Unexpected error Occured"
        val errorResult = Result.Error<List<Article>>(error)
        coEvery { (newsRepository.getNews("health"))} returns flowOf(errorResult)
        val result = getNewsUseCase.invoke("health").take(1)
        advanceUntilIdle()
        assertEquals(errorResult,result.toList()[0])
        coVerify { newsRepository.getNews("health") }
    }


}
