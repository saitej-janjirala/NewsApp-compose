package com.saitejajanjirala.newsapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.saitejajanjirala.newsapp.BuildConfig
import com.saitejajanjirala.newsapp.data.local.DatabaseService
import com.saitejajanjirala.newsapp.data.local.NewsDao
import com.saitejajanjirala.newsapp.data.remote.ApiService
import com.saitejajanjirala.newsapp.data.repository.NewsRepositoryImpl
import com.saitejajanjirala.newsapp.domain.repository.NewsRepository
import com.saitejajanjirala.newsapp.domain.usecase.GetNewsUseCase
import com.saitejajanjirala.newsapp.domain.usecase.NewsUseCases
import com.saitejajanjirala.newsapp.util.Keys
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{
    @Provides
    @Singleton
    fun providesMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun getOkHttpClient(): OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder().apply {
            addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val originalRequest = chain.request()
                    val originalUrl = originalRequest.url
                    val newRequest=originalRequest.newBuilder().apply {
                        url(originalUrl.newBuilder()
                            .addQueryParameter("apiKey", BuildConfig.API_KEY)
                            .addQueryParameter("language","en")
                            .addQueryParameter("pageSize","20")
                            .addQueryParameter("country","us").build())
                    }.build()
                    return chain.proceed(newRequest)
                }
            })
        }
        httpClient.addInterceptor(logging)
        return httpClient.build()
    }

    @Singleton
    @Provides
    fun providesApiService(moshi: Moshi,okHttpClient: OkHttpClient): ApiService{
        return  Retrofit.Builder()
            .baseUrl(Keys.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsUseCase(newsRepository: NewsRepository) : NewsUseCases{
        return NewsUseCases(
            getNewsUseCase = GetNewsUseCase(newsRepository)
        )
    }

    @Provides
    @Singleton
    fun provideDatabaseService(application: Application):DatabaseService{
        return Room.databaseBuilder(application,
            DatabaseService::class.java,
            DatabaseService.DB_NAME).build()
    }

    @Provides
    @Singleton
    fun provideNewsDao(databaseService: DatabaseService):NewsDao{
        return databaseService.getNewsDao()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }


}