package com.saitejajanjirala.newsapp.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.saitejajanjirala.newsapp.data.models.Article
import com.saitejajanjirala.newsapp.di.DispatcherProvider
import com.saitejajanjirala.newsapp.domain.models.Result
import com.saitejajanjirala.newsapp.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class MainViewModel @Inject constructor(
    val getNewsUseCase: GetNewsUseCase,
    val savedStateHandle: SavedStateHandle,
    val dispatcher: DispatcherProvider
):ViewModel(){

    private val _items = MutableStateFlow<Result<List<Article>>>(Result.Loading())
    val items: StateFlow<Result<List<Article>>> = _items
    init {
        fetchHeadLines()
    }

    fun fetchHeadLines(){
        viewModelScope.launch(dispatcher.io) {
            getNewsUseCase.invoke("health").collect {
                _items.value = it
            }
        }
    }

    fun getUrl(): String? {
       return savedStateHandle.get<String>("url")
    }

}