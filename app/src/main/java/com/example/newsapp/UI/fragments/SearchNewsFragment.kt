package com.example.newsapp.UI.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.UI.NewsActivity
import com.example.newsapp.UI.NewsViewModel
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.util.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.example.newsapp.util.Resource
import kotlinx.coroutines.*

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    lateinit var newsAdapter : NewsAdapter
    lateinit var viewModel: NewsViewModel
    lateinit var rvSearchNews : RecyclerView
    lateinit var paginationProgressBar: ProgressBar
    lateinit var etSearch : EditText
    var TAG = "SearchNewsFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        paginationProgressBar = view.findViewById(R.id.paginationProgressBarr)
        rvSearchNews = view.findViewById(R.id.rvSearchNewss)
        etSearch = view.findViewById(R.id.etSearch)
        setupRecyclerView()

        newsAdapter.OnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article" , it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment , bundle
            )
        }

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success -> {
                   hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resource.Error->{
                    response.message?.let {
                        Log.e(TAG, "an error :  ${response.message}")

                    }
                }
                is Resource.Loading->{
                    ShowProgressBar()
                }
            }
        })
        // im put delay to send one request in search process
            var job : Job? = null
            etSearch.addTextChangedListener { Editable->
               job?.cancel()
                job = MainScope().launch {
                    //wait 500 ms until send request
                    delay(SEARCH_NEWS_TIME_DELAY)
                }
                Editable?.let {
                    if(Editable.toString().isNotEmpty()) {
                        viewModel.getSearchNews(Editable.toString())
                    }
                    }
                }

            }


    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE

    }

    private fun ShowProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE

    }


}