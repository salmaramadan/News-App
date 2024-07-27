package com.salma.authentication.data.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "articles")
data class Article(
    val author: String?,
    @PrimaryKey val title: String,
    val description: String?,
    val urlToImage: String?,
    val content: String?,
    var isFavorite: Boolean = false
) : Serializable
