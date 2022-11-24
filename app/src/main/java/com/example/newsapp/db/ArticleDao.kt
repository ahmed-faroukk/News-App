package com.example.newsapp.db

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.models.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //name it upsert because if found the article will update it else will insert
    @NonNull
    suspend fun Upsert(article: Article): Long
    //will return live data object and this didn't find in suspend fun
    // live data work on change recycler view if db  changes and if rotate also
    @Query("SELECT * FROM articles")
    @NonNull

    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    @NonNull
    suspend fun deleteArticle (article: Article)

}