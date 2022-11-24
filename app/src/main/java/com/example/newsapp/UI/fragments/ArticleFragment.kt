package com.example.newsapp.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.example.newsapp.R
import com.example.newsapp.UI.NewsActivity
import com.example.newsapp.UI.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    lateinit var webView : WebView
    lateinit var fab : FloatingActionButton
     val args: ArticleFragmentArgs by navArgs()
     var url: String = "https://www.dictionary.com/browse/article"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        webView= view.findViewById(R.id.webView)
        fab = view.findViewById(R.id.fab)
        val article = args.article
        webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }
        fab.setOnClickListener(View.OnClickListener {
            viewModel.saveArticle(article)
            fab.setImageResource(R.drawable.ic_baseline_favoritee_24)
            Snackbar.make(view, "Article saved successfully" , Snackbar.LENGTH_SHORT).show()
        })



    }


}