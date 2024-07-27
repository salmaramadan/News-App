package com.salma.authentication.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.salma.authentication.data.dataclasses.Article

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Update
    suspend fun update(article: Article)

    @Query("SELECT * FROM articles WHERE isFavorite = 1")
    suspend fun getAllFavorites(): List<Article>
}
