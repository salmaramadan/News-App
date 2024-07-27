package com.salma.authentication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salma.authentication.R
import com.salma.authentication.data.dataclasses.Article

class NewsAdapter(
    private val articles: List<Article>,
    private val onFavoriteClickListener: (Article) -> Unit,
    private val onItemClickListener: (Article) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount(): Int = articles.size

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.article_title)
        private val description: TextView = itemView.findViewById(R.id.article_description)
        private val author: TextView = itemView.findViewById(R.id.article_author)
        private val thumbnail: ImageView = itemView.findViewById(R.id.article_thumbnail)
        private val favorite: ImageView = itemView.findViewById(R.id.article_favorite)

        fun bind(article: Article) {
            title.text = article.title
            description.text = article.description
            author.text = article.author

            Glide.with(itemView.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.newsapp)
                .error(R.drawable.ic_settings)
                .into(thumbnail)

            favorite.setImageResource(
                if (article.isFavorite) R.drawable.added_favorite else R.drawable.favorite_icon
            )

            favorite.setOnClickListener {
                onFavoriteClickListener(article)
            }

            itemView.setOnClickListener {
                onItemClickListener(article)
            }
        }
    }
}
