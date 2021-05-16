package com.harshil.trendingnews.ui.newslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.harshil.trendingnews.data.Article
import com.harshil.trendingnews.databinding.ListNewsItemBinding

class NewsListAdapter(
    private val newsListInteractor: NewsListInteractor
) : PagingDataAdapter<Article, NewsListAdapter.NewsListViewHolder>(DIFF_UTIL) {
    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        return NewsListViewHolder(
            ListNewsItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsListViewHolder, position: Int) {
        getItem(position)?.let { article ->
            holder.bindData(article)
        }
    }

    inner class NewsListViewHolder(private val binding: ListNewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(article: Article) {
            binding.run {
                tvNewsSource.text = article.source?.name.orEmpty()
                tvNewsDescription.text = article.description.orEmpty()
                tvNewsHeadline.text = article.title
                tvPublishTime.text = article.publishedAt.orEmpty()
                ivNewsImage.load(article.urlToImage)
                root.setOnClickListener {
                    newsListInteractor.goToDetailScreen(article)
                }
            }
        }
    }
}

interface NewsListInteractor {
    fun goToDetailScreen(article: Article)
}
