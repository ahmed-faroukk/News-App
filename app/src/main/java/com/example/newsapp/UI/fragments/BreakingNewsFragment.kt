package com.example.newsapp.UI.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapp.R
import com.example.newsapp.UI.NewsActivity
import com.example.newsapp.UI.NewsViewModel
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsapp.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var rvBreakingNews: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    //handle pagination
    var isLoading = false
    var isScrolling = false
    var isLastPage = false
    val TAG = "BreakingNewsFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as NewsActivity).viewModel
        swipeRefreshLayout = view.findViewById(R.id.swipe)
        rvBreakingNews = view.findViewById(R.id.rvBreakingNews)
        progressBar = view.findViewById(R.id.paginationProgressBar)
        setupRecyclerView()

        newsAdapter.OnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article" , it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment2_to_articleFragment , bundle
            )
        }

        // i rename resourse as response
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { NewsResponse ->
                        newsAdapter.differ.submitList(NewsResponse.articles)
                        val totalPages = NewsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                    }

                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(activity , "no internet", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    ShowProgressBar()
                }

            }

        })
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
                when (response) {
                    is Resource.Success -> {
                        hideProgressBar()
                        response.data?.let { NewsResponse ->
                            newsAdapter.differ.submitList(NewsResponse.articles)
                            val totalPages = NewsResponse.totalResults / QUERY_PAGE_SIZE + 2
                            isLastPage = viewModel.breakingNewsPage == totalPages
                        }

                    }
                    is Resource.Error -> {
                        response.message?.let {
                            Toast.makeText(activity , "no internet", Toast.LENGTH_SHORT).show()
                        }
                    }
                    is Resource.Loading -> {
                        ShowProgressBar()
                    }

                }

            })
            swipeRefreshLayout.isRefreshing = false

        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            //set adapter
            adapter = newsAdapter
            //set layoutManager
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)

        }

    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        isLoading = false

    }

    private fun ShowProgressBar() {
        progressBar.visibility = View.VISIBLE
        isLoading = true

    }
    // handel pagination
    // 1:  handle scrolling in rv
    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition>=0
            val isTotalMoreThanVisible = totalItemCount>= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isNotAtBeginning && isLastItem &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate){
                viewModel.getBreakingNews("eg")
                isScrolling = false
            }else {
                rvBreakingNews.setPadding(0,0,0,0)
            }

        }
    }

}