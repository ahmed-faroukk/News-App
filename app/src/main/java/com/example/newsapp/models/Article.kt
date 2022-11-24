package com.example.newsapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "articles"
)

data class Article(
    // i make id for loved articles only because these will save in Room Db
    @PrimaryKey(
        autoGenerate = true
    )
    var id : Int ?=null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String,
    val urlToImage: String
    //make it : serializable to which tell kotlin that we want to be able to pass this class between several fragments with the navigation components
) : Serializable