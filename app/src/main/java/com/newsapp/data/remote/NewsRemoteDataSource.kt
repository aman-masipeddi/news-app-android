package com.example.newsapp.data.remote

import android.util.Log
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.util.Resource
import com.newsapp.util.NetworkUtils
import javax.inject.Inject

const val TAG = "NewsRemoteDataSource"
class NewsRemoteDataSource @Inject constructor(
    private val newsApiService: NewsApiService,
) {
    suspend fun getTopHeadlines(): Resource<List<NewsArticle>> {
        Log.d(TAG, "Requesting top headlines from API")
        return try {
            if(!NetworkUtils.isConnected()) {
                Log.d(TAG, "No internet connectivity, cancelling request to API")
                return Resource.Error("No Internet Connectivity")
            }
            val response = newsApiService.getTopHeadlines()
            Resource.Success(response.articles)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

    suspend fun getArticles(query: String): Resource<List<NewsArticle>> {
        Log.d(TAG, "Requesting news articles for query: $query")
        return try {
            if(!NetworkUtils.isConnected()) {
                Log.d(TAG, "No internet connectivity, cancelling request to API")
                return Resource.Error("No Internet Connectivity")
            }
            val response = newsApiService.getArticles(query = query)
            Resource.Success(response.articles)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}