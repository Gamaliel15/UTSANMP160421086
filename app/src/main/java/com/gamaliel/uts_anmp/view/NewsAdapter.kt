package com.gamaliel.uts_anmp.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.gamaliel.uts_anmp.databinding.NewsListItemBinding
import com.gamaliel.uts_anmp.model.News
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class NewsAdapter(val newsList:ArrayList<News>)
    : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(var binding: NewsListItemBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        var binding = NewsListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.binding.txtTitle.text = newsList[position].judul
        holder.binding.txtName.text = newsList[position].penulis
        holder.binding.txtIsi.text = newsList[position].isi
        holder.binding.btnRead.setOnClickListener{
            val action = HomeFragmentDirections.actiondetailfragment(newsList[position].id_news)
            Navigation.findNavController(it).navigate(action)
        }

        val picasso = Picasso.Builder(holder.itemView.context)
        picasso.listener { picasso, uri, exception ->
            exception.printStackTrace()
        }
        picasso.build().load(newsList[position].url_photo).into(holder.binding.imgNews, object:
            Callback {
            override fun onSuccess() {
                holder.binding.imgNews.visibility = View.VISIBLE

            }

            override fun onError(e: Exception?) {
                Log.e("picasso_error", e.toString())
            }

        })
    }

    fun updateNewsList(newNewsList:ArrayList<News>){
        newsList.clear()
        newsList.addAll(newNewsList)
        notifyDataSetChanged()
    }
    }