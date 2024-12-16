package com.example.newsapp.ui.dashboard

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "DashboardViewModel"

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {
    private val _newsState = mutableStateOf<DashboardViewState<NewsArticle>>(DashboardViewState.Loading())
    val newsState: State<DashboardViewState<NewsArticle>> = _newsState

    private var articles = mutableListOf<NewsArticle>()

    init {
        _newsState.value = DashboardViewState.Loading(loading = true)
        fetchTopHeadlines()
    }

    fun refreshNews() {
        _newsState.value = DashboardViewState.Loading(isRefresh = true)
        fetchTopHeadlines()
    }

    private fun fetchTopHeadlines() {
        viewModelScope.launch {
            val result = newsRepository.getTopHeadlines()
            if(result is Resource.Success) {
                val list = result.data ?: emptyList()
                articles = list.toMutableList()
                _newsState.value = DashboardViewState.Success(data = list)
            } else {
                _newsState.value = DashboardViewState.Error(errorMessage = result.message ?: "An Error Occurred")
            }
        }
    }

    fun onSearchSubmit(searchQuery: String) {
        if (searchQuery.isEmpty()) {
            fetchTopHeadlines()
            return
        }
        _newsState.value = DashboardViewState.Loading(loading = true)
        Log.d(TAG, "search Query for headlines: $searchQuery")
        viewModelScope.launch {
            val result = newsRepository.getArticle(searchQuery)
            if(result is Resource.Success) {
                val list = result.data ?: emptyList()
                articles = list.toMutableList()
                _newsState.value = DashboardViewState.Success(data = list)
            } else {
                _newsState.value = DashboardViewState.Error(errorMessage = result.message ?: "An Error Occurred")
            }
        }
    }
}

sealed class DashboardViewState<T> {

    data class Loading<T>(val loading: Boolean = false, val isRefresh: Boolean = false) : DashboardViewState<T>()

    data class Success<T>(
        val data: List<T>,
    ) : DashboardViewState<T>()

    data class Error<T>(
        val errorMessage: String,
        val isRefreshError: Boolean = false,
        val isPaginationError: Boolean = false
    ) : DashboardViewState<T>()
}

