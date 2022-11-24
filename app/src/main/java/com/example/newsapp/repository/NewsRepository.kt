package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.models.Article

// have remote data and local data
class NewsRepository(

    //instance of database
    val db : ArticleDatabase
) {
    //remote data
    // get fun that directly queries our api
    suspend fun getBreakingNews(countryCode : String , pageNumber : Int)
      =  RetrofitInstance().api.getBreakingNews(countryCode , pageNumber)

    suspend fun getSearchNews(searchQuery : String , pageNumber : Int)
      = RetrofitInstance().api.searchForNews(searchQuery , pageNumber)

    //local data
        suspend fun upsert(article : Article) = db.getArticleDao().Upsert(article)

        fun getSavedNews()= db.getArticleDao().getAllArticles()

        suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)



}