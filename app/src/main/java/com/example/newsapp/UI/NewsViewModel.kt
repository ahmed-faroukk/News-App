package com.example.newsapp.UI

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.network.ConnectivityObserver
import com.example.newsapp.network.NetworkConnectivityObserver
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response
// handle response of our request and put live data object that handle changes in ui
class NewsViewModel(
    app : Application,
    val newsRepository: NewsRepository
) : AndroidViewModel(app) {
    val context = getApplication<Application>().applicationContext
    // to make network request
init {
    getBreakingNews("eg")
}
    //  create Live data Object that notify all of our fragments about changes
    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    //page variable is here because if i put it in the fragment it will losing value when rotate or any thing losing data
    var breakingNewsPage = 1
    //to handle rotate device in pagination
    var breakingNewsResponse : NewsResponse ?= null
    lateinit var connectivityObserver: ConnectivityObserver

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse : NewsResponse? = null


    fun getBreakingNews(countryCode : String) = viewModelScope.launch {

      safeBreakingNewsCall(countryCode)

    }

    fun getSearchNews(searchQuery : String ) = viewModelScope.launch {
       safeSearchNewsCall( searchQuery)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse> {
        if(response.isSuccessful){
            //check that body of response not null
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                //case 1 : first page then the breaking news = null
                if(breakingNewsResponse==null){
                    breakingNewsResponse = resultResponse
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticles?.addAll(newArticle)
                }
                // if breaking news is null "first page" will return result response
                return Resource.Success(breakingNewsResponse?:resultResponse )
            }
        }
        return Resource.Error(response.message() , data = null)
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message() , data = null)

    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }
    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
    fun getSavedNews() = newsRepository.getSavedNews()

    fun safeBreakingNewsCall(countryCode: String ) {
        connectivityObserver = NetworkConnectivityObserver(context)
        connectivityObserver.observe().onEach {
            try{
                if (it.toString() == "Lost" || it.toString() == "Losing" || it.toString() == "Unavailable") {
                    breakingNews.postValue(Resource.Error("No Internet Connection" , null))
                }else{
                    val response = newsRepository.getBreakingNews(countryCode , breakingNewsPage)
                    breakingNews.postValue(handleBreakingNewsResponse(response))
                }

            }catch (t : Throwable){
                    when(t){
                        is IOException ->breakingNews.postValue(Resource.Error("Network failure" ,null))
                        else -> breakingNews.postValue(Resource.Error("conversion Error" , null))
                    }
            }

        }.launchIn(MainScope())
    }

    fun safeSearchNewsCall(SearchQuery: String ) {
        connectivityObserver = NetworkConnectivityObserver(context)
        connectivityObserver.observe().onEach {
            try{
                if (it.toString() == "Lost" || it.toString() == "Losing" || it.toString() == "Unavailable") {
                    searchNews.postValue(Resource.Error("No Internet Connection" , null))
                }else{
                    val response = newsRepository.getSearchNews(SearchQuery , searchNewsPage)
                    searchNews.postValue(handleSearchNewsResponse(response))
                }

            }catch (t : Throwable){
                when(t){
                    is IOException ->searchNews.postValue(Resource.Error("Network failure" ,null))
                    else -> searchNews.postValue(Resource.Error("conversion Error" , null))
                }
            }

        }.launchIn(MainScope())
    }

/*
    fun alert() {
        val builder = AlertDialog.Builder(context, AppCompatActivity.TRIM_MEMORY_BACKGROUND)
        builder.setTitle("News App")
        builder.setMessage("there is no Internet")

        builder.setPositiveButton("OK") { dialog, which -> }

        builder.show()

    }*/

//9:43
}