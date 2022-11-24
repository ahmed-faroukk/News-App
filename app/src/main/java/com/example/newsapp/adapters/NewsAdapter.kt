package com.example.newsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.UI.fragments.ArticleFragment
import com.example.newsapp.models.Article

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
     var articleFragment = ArticleFragment()
    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var ImageView : ImageView = itemView.findViewById(R.id.ivArticleImage)
        var  tvSource: TextView = itemView.findViewById(R.id.tvSource)
        var  tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
       // var  tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
      //  var  tvPublishedAt: TextView = itemView.findViewById(R.id.tvPublishedAt)
    }

    // start DiffUtil
    private var differCallback = object : DiffUtil.ItemCallback<Article>() {

        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    } //AsyncListDiffer is a tool that take our two list and compares them and calculates the difference and it will run in background
    val differ = AsyncListDiffer(this, differCallback)
   //end of differ


   // recyclerView Functions
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {

       return ArticleViewHolder(

    LayoutInflater.from(
        parent.context).inflate(R.layout.item_article_preview,
        parent,
        false )


)
    }
    // bind my api data that in differ list with my view
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {

        val article = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.ImageView)
            holder.tvSource.text = article.source?.name
            holder.tvTitle.text = article?.title

           // holder.tvDescription.text = article.description
          //  holder.tvPublishedAt.text = article.publishedAt

          setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    //to manage item click
    private var onItemClickListener:((Article)->Unit)? = null

    fun OnItemClickListener(listener: (Article)->Unit){
        onItemClickListener = listener
    }

    private fun replaceFragment(fragment: Fragment) {
        val view : View

    }

}