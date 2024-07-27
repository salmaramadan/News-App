package com.salma.authentication.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.salma.authentication.R
import com.salma.authentication.data.dataclasses.Article

class DetailsActivity : AppCompatActivity() {

    private lateinit var article: Article

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Retrieve article from Intent
        article = intent.getSerializableExtra("article") as Article

        val title: TextView = findViewById(R.id.titleTextView)
        val author: TextView = findViewById(R.id.authorTextView)
        val description: TextView = findViewById(R.id.descriptionTextView)
        val content: TextView = findViewById(R.id.contentTextView)
        val image: ImageView = findViewById(R.id.imageView)
        val favoriteIcon: ImageView = findViewById(R.id.favoriteImageView)

        title.text = article.title
        author.text = article.author
        description.text = article.description
        content.text = article.content

        Glide.with(this)
            .load(article.urlToImage)
            .placeholder(R.drawable.newsapp)
            .error(R.drawable.ic_settings)
            .into(image)

        favoriteIcon.setImageResource(
            if (article.isFavorite) R.drawable.added_favorite else R.drawable.favorite_icon
        )

        favoriteIcon.setOnClickListener {
            article.isFavorite = !article.isFavorite
            // Update the favorite status in database if needed
        }
    }
}
