package com.example.newsapp.data.repository

import com.example.newsapp.data.remote.NewsRemoteDataSource
import com.example.newsapp.data.model.NewsArticle
import com.example.newsapp.util.Resource
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val remoteDataSource: NewsRemoteDataSource
) {
    suspend fun getTopHeadlines(): Resource<List<NewsArticle>> {
        return remoteDataSource.getTopHeadlines()

    }

    suspend fun getArticle(query: String) : Resource<List<NewsArticle>> {
        return remoteDataSource.getArticles(query = query)
    }
}