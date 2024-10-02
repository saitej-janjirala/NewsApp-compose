package com.saitejajanjirala.newsapp.data.repository

import android.util.Log
import com.saitejajanjirala.newsapp.data.local.NewsDao
import com.saitejajanjirala.newsapp.data.models.Article
import com.saitejajanjirala.newsapp.data.models.HeadLine
import com.saitejajanjirala.newsapp.data.remote.ApiService
import com.saitejajanjirala.newsapp.domain.models.Result
import com.saitejajanjirala.newsapp.domain.repository.NewsRepository
import com.saitejajanjirala.newsapp.util.Keys
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException
import javax.inject.Inject


class NewsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao
    ) : NewsRepository {

    override suspend fun getNews(category: String): Flow<Result<List<Article>>> = flow {
        // Emit cached data first (if available) before making the network call
        val cachedArticles = newsDao.getAllArticles()
        if (cachedArticles.isNotEmpty()) {
            emit(Result.Success(cachedArticles))
        } else {
            emit(Result.Loading()) // Optionally show loading state if no cached data
        }

        try {
            // Fetch data from the network
            val res = apiService.getHeadLines(category)
            res?.let {
                if (it.status == Keys.STATUS_OK) {
                    val articles = it.articles ?: emptyList()
                    // Insert into the database and emit from the database again
                    insertIntoDatabase(articles)
                    emit(Result.Success(newsDao.getAllArticles())) // Emit the updated articles
                } else {
                    emit(Result.Error("An unexpected error occurred"))
                    emit(Result.Success(newsDao.getAllArticles())) // Fallback to cached data
                }
            } ?: run {
                // Handle null response
                emit(Result.Error("No response from the server"))
                emit(Result.Success(newsDao.getAllArticles())) // Fallback to cached data
            }
        } catch (e: UnknownHostException) {
            // Handle no internet connection
            emit(Result.Error("Couldn't reach the server. Check your internet connection."))
            delay(1000) // Optional delay for retrying
            emit(Result.Success(newsDao.getAllArticles())) // Fallback to cached data
        } catch (e: Exception) {
            // Handle any unexpected errors
            e.printStackTrace()
            Log.e("NewsRepository", e.message.toString())
            emit(Result.Error("An unexpected error occurred: ${e.message}"))
            emit(Result.Success(newsDao.getAllArticles())) // Fallback to cached data
        }
    }


    override suspend fun insertIntoDatabase(articles : List<Article>){
        newsDao.clearAll()
        newsDao.insertAll(articles)
    }

    override suspend fun fetchFromDatabase(): Flow<Result<List<Article>>> = flow{
        val list = newsDao.getAllArticles()
        emit(Result.Success(list))
    }
}