package com.saitejajanjirala.newsapp.di

import com.saitejajanjirala.newsapp.data.repository.NewsRepositoryImpl
import com.saitejajanjirala.newsapp.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DispatcherProviderModule {

    @Binds
    @Singleton
    abstract fun bindsNewsRepository(dispatcherProvider: DefaultDispatcherProvider): DispatcherProvider
}