package com.example.newsapp.UI

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapp.network.ConnectivityObserver
import com.example.newsapp.network.NetworkConnectivityObserver
import com.example.newsapp.R
import com.example.newsapp.UI.fragments.ArticleFragment
import com.example.newsapp.UI.fragments.BreakingNewsFragment
import com.example.newsapp.UI.fragments.SavedNewsFragment
import com.example.newsapp.UI.fragments.SearchNewsFragment
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.db.ArticleDatabase
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_news.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class NewsActivity : AppCompatActivity() {
    lateinit var buttomNavigationView: BottomNavigationView
    lateinit var binding: ActivityNewsBinding
    private var articleFragment = ArticleFragment()
    private var beakingNewsFragment = BreakingNewsFragment()
    private var savedFragment = SavedNewsFragment()
    private var searchNewsFragment = SearchNewsFragment()
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   Log.d("testAc" , "true1")
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buttomNavigationView = findViewById(R.id.bottomNavigation)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        buttomNavigationView.setupWithNavController(navController)

        //set Repository with ViewModelProviderFactory
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val newsViewModelProviderFactory = NewsViewModelProviderFactory(application,newsRepository)
        //set viewModel with ViewModelProvider
        viewModel = ViewModelProvider(
            this, newsViewModelProviderFactory
        ).get(NewsViewModel::class.java)


    }





}