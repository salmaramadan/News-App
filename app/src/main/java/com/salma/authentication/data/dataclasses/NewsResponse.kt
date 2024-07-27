package com.salma.authentication.data.dataclasses

import com.salma.authentication.data.dataclasses.Article

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
