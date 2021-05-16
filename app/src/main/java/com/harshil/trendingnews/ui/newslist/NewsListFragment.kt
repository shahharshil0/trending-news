package com.harshil.trendingnews.ui.newslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.harshil.trendingnews.base.BaseFragment
import com.harshil.trendingnews.data.Article
import com.harshil.trendingnews.databinding.FragmentNewsListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class NewsListFragment : BaseFragment(), NewsListInteractor {
    //region global variables
    private var _binding: FragmentNewsListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter = NewsListAdapter(this)

    private val viewModel: NewsListViewModel by lazy { getViewModel() }
    //endregion

    //region lifecycle methods
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupVMObserver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    //region vm observers
    private fun setupVMObserver() {
        setupTopHeadlinesObserver()
    }

    private fun setupTopHeadlinesObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topHeadlines.collectLatest {
                adapter.submitData(it)
            }
        }
    }
    //endregion

    //region ui handling
    private fun setupUI() {
        //setup news list recyclerview
        binding.rvNewsList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@NewsListFragment.adapter.withLoadStateHeaderAndFooter(
                header = NewsListLoadStateAdapter { this@NewsListFragment.adapter.retry() },
                footer = NewsListLoadStateAdapter { this@NewsListFragment.adapter.retry() }
            )
        }

        //add load states to adapter
        adapter.addLoadStateListener { loadState ->
            val refreshState = loadState.mediator?.refresh
            binding.rvNewsList.isVisible = refreshState is LoadState.NotLoading
            binding.progressBar.isVisible = refreshState is LoadState.Loading
            binding.swipeRefresh.isRefreshing =
                refreshState is LoadState.Loading && !binding.swipeRefresh.isVisible
            binding.retryButton.isVisible = refreshState is LoadState.Error
            handleError(loadState)

            //no result state
            binding.tvEmpty.isVisible =
                loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1
        }

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }

        binding.retryButton.setOnClickListener {
            adapter.retry()
        }
    }

    private fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error

        errorState?.let {
            Toast.makeText(requireContext(), "${it.error}", Toast.LENGTH_LONG).show()
        }
    }
    //endregion

    //region interactor methods
    override fun goToDetailScreen(article: Article) {
        val action =
            NewsListFragmentDirections.actionNewsListFragmentToNewsDetailFragment(article = article)
        findNavController().navigate(action)
    }
    //endregion
}