package com.harshil.trendingnews.ui.newsdetail

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.harshil.trendingnews.R
import com.harshil.trendingnews.databinding.FragmentNewsDetailBinding
import com.harshil.trendingnews.utils.appendClickableString
import com.harshil.trendingnews.utils.launchCustomTab


class NewsDetailFragment : Fragment() {
    //region global variables
    private var _binding: FragmentNewsDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val args by navArgs<NewsDetailFragmentArgs>()
    //endregion

    //region lifecycle methods
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //endregion

    //region ui handling
    private fun setupUI() {
        args.article?.let { article ->
            binding.ivNewsImage.load(article.urlToImage)
            binding.tvNewsHeadline.text = article.title
            binding.tvNewsSource.text = article.source?.name.orEmpty()
            binding.tvNewsContent.text = article.content.orEmpty()

            binding.tvNewsContent.text = appendClickableString(
                actualString = article.content.orEmpty(),
                appendString = getString(R.string.read_more)
            ) {
                val url = article.url.orEmpty()
                if (url.isNotBlank()) {
                    context?.launchCustomTab(url = url)
                }
            }
            binding.tvNewsContent.movementMethod = LinkMovementMethod.getInstance()
        }
    }
    //endregion
}