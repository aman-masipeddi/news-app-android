package com.example.newsapp.data.remote

import com.example.newsapp.data.model.NewsArticle
import retrofit2.http.GET
import retrofit2.http.Query

const val NEWS_API_KEY = "bd2e217d0b9146369d08e9cd8a17179d"


interface NewsApiService {
    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String = NEWS_API_KEY
    ): NewsResponse

    @GET("everything")
    suspend fun getArticles(
        @Query("apiKey") apiKey: String = NEWS_API_KEY,
        @Query("q") query: String = ""
    ): NewsResponse
}

data class NewsResponse(
    val articles: List<NewsArticle>
)