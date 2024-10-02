package com.saitejajanjirala.newsapp.domain.repository

import com.saitejajanjirala.newsapp.data.models.Article
import com.saitejajanjirala.newsapp.data.models.HeadLine
import com.saitejajanjirala.newsapp.domain.models.Result
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNews( category : String = "") : Flow<Result<List<Article>>>
    suspend fun insertIntoDatabase(articles : List<Article>)
    suspend fun fetchFromDatabase(): Flow<Result<List<Article>>>
}