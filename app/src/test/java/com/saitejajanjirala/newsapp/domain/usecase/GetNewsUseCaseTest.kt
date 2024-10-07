package com.saitejajanjirala.newsapp.domain.usecase

import app.cash.turbine.test
import com.saitejajanjirala.newsapp.data.models.Article
import com.saitejajanjirala.newsapp.data.models.Source
import com.saitejajanjirala.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import com.saitejajanjirala.newsapp.domain.models.Result
import kotlinx.coroutines.flow.flow
import org.junit.Assert.assertEquals
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetNewsUseCaseTest {

    private lateinit var newsRepository: NewsRepository
    private lateinit var getNewsUseCase: GetNewsUseCase

    @Before
    fun setUp() {
        // Here, we mock the repository
        newsRepository = mock()
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
        whenever(newsRepository.getNews("health")).thenReturn(flow {
            emit(successResult)
        })

        // Act: Call the use case
        val resultFlow = getNewsUseCase.invoke("health")

        // Assert: Verify the result
        resultFlow.test {
            val item = awaitItem()
            assertEquals(successResult,item)
            awaitComplete()
        }
    }


}
