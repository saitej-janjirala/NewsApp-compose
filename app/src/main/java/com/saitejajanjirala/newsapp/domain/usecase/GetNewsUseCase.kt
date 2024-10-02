package com.saitejajanjirala.newsapp.domain.usecase

import com.saitejajanjirala.newsapp.data.models.Article
import com.saitejajanjirala.newsapp.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import com.saitejajanjirala.newsapp.domain.models.Result
import javax.inject.Inject


class GetNewsUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    suspend operator fun invoke(category : String):Flow<Result<List<Article>>>{
        return newsRepository.getNews(category)
    }
}
