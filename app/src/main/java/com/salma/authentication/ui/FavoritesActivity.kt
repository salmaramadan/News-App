package com.salma.authentication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.salma.authentication.R
import com.salma.authentication.adapter.NewsAdapter
import com.salma.authentication.data.database.AppDatabase
import com.salma.authentication.data.dataclasses.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoritesAdapter: NewsAdapter
    private lateinit var database: AppDatabase
    private var favoriteArticles: List<Article> = listOf()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        bottomNavigationView = findViewById(R.id.bottom_navigation_fav)
        bottomNavigationView.selectedItemId = R.id.nav_favorites

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

        database = AppDatabase.getDatabase(this)
        favoritesRecyclerView = findViewById(R.id.favorites_recycler_view)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchFavoriteArticles()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun fetchFavoriteArticles() {
        lifecycleScope.launch {
            try {
                favoriteArticles = withContext(Dispatchers.IO) {
                    database.articleDao().getAllFavorites()
                }
                favoritesAdapter = NewsAdapter(
                    articles = favoriteArticles,
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
                                    favoritesAdapter.notifyDataSetChanged()
                                    Toast.makeText(this@FavoritesActivity, "Favorite status updated", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "Error updating favorite status: ${e.message}", e)
                                runOnUiThread {
                                    Toast.makeText(this@FavoritesActivity, "Error updating favorite status: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    onItemClickListener = { article ->
                        val intent = Intent(this@FavoritesActivity, DetailsActivity::class.java).apply {
                            putExtra("article", article)
                        }
                        startActivity(intent)
                    }
                )
                favoritesRecyclerView.adapter = favoritesAdapter
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching favorite articles: ${e.message}")
                Toast.makeText(this@FavoritesActivity, "Error fetching favorite articles", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish() // Optionally finish FavoritesActivity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "FavoritesActivity"
    }
}
