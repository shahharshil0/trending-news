package com.harshil.trendingnews.ui.newslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.harshil.trendingnews.databinding.ItemLoadStateFooterBinding

class NewsListLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<NewsListLoadStateAdapter.NewsListLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: NewsListLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NewsListLoadStateViewHolder {
        return NewsListLoadStateViewHolder(
            ItemLoadStateFooterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class NewsListLoadStateViewHolder(
        private val binding: ItemLoadStateFooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = loadState.error.localizedMessage
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState !is LoadState.Loading
            binding.errorMsg.isVisible = loadState !is LoadState.Loading
        }
    }
}