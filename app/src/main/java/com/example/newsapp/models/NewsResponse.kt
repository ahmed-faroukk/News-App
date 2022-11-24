package com.example.newsapp.models

import com.example.newsapp.models.Article

data class NewsResponse(
    //make it mutable to use addAll fun in viewModel that make newArticle = oldArticle
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)