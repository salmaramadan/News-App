package com.salma.authentication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.salma.authentication.R
import com.salma.authentication.adapter.NewsAdapter
import com.salma.authentication.data.database.AppDatabase
import com.salma.authentication.data.dataclasses.Article
import com.salma.authentication.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsRepository: NewsRepository
    private lateinit var database: AppDatabase
    private lateinit var bottomNavigationView: BottomNavigationView
    private var articles: List<Article> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        database = AppDatabase.getDatabase(this)
        newsRecyclerView = findViewById(R.id.news_recycler_view)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)

        newsRepository = NewsRepository()

        fetchNews()

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_favorites -> {
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.nav_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun fetchNews() {
        newsRepository.fetchNews { articles ->
            runOnUiThread {
                if (articles != null) {
                    // Filter out articles with null or empty imageUrl
                    val filteredArticles = articles.filter { !it.urlToImage.isNullOrEmpty() }

                    this.articles = filteredArticles
                    newsAdapter = NewsAdapter(
                        articles = this.articles,
                        onFavoriteClickListener = { article ->
                            article.isFavorite = !article.isFavorite
                            lifecycleScope.launch {
                                try {
                                    withContext(Dispatchers.IO) {
                                        if (article.isFavorite) {
                                            database.articleDao().insert(article)
                                        } else {
                                            database.articleDao().update(article)
                                        }
                                    }
                                    runOnUiThread {
                                        newsAdapter.notifyDataSetChanged()
                                        Toast.makeText(this@HomeActivity, "Favorite status updated", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e(TAG, "Error updating favorite status: ${e.message}", e)
                                    runOnUiThread {
                                        Toast.makeText(this@HomeActivity, "Error updating favorite status: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        onItemClickListener = { article ->
                            val intent = Intent(this, DetailsActivity::class.java).apply {
                                putExtra("article", article)
                            }
                            startActivity(intent)
                        }
                    )
                    newsRecyclerView.adapter = newsAdapter
                } else {
                    Toast.makeText(this@HomeActivity, "Error fetching news", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val TAG = "HomeActivity"
    }
}
