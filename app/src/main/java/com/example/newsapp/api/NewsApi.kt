package com.example.newsapp.api

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    // this url from api tutorial
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode : String = "eg",
        @Query("page")
        pageNumber : Int = 1 ,
        @Query("apiKey")
        apiKey : String = API_KEY

    ):Response<NewsResponse>

    //choose everything to search in all news that explained in api tutorial
    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery : String  ,
        @Query("page")
        pageNumber : Int = 1 ,
        @Query("apiKey")
        apiKey : String = API_KEY

    ):Response<NewsResponse>
}