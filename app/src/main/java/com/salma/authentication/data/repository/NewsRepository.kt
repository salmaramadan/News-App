package com.salma.authentication.data.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.salma.authentication.data.dataclasses.Article
import com.salma.authentication.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository {
    fun fetchNews(callback: (List<Article>?) -> Unit) {
        val newsService = RetrofitInstance.api
        val call = newsService.getNews(
            query = "apple",
            from = "2024-07-11",
            to = "2024-07-11",
            sortBy = "popularity",
            apiKey = "1132cf54cc6c4154be157a1d5a15adc0"
        )

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val articles = body?.getAsJsonArray("articles")
                    val articleList = articles?.let {
                        Gson().fromJson(it, Array<Article>::class.java).toList()
                    }
                    callback(articleList)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(null)
            }
        })
    }
}
