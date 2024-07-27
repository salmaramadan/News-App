package com.salma.authentication.utiles

import com.salma.authentication.data.dataclasses.Article


interface OnFavoriteClickListener {
    fun onFavoriteClick(article: Article)
}
