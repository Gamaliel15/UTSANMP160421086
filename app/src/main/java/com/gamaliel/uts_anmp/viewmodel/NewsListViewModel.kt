package com.gamaliel.uts_anmp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gamaliel.uts_anmp.model.News
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NewsListViewModel(application: Application): AndroidViewModel(application) {
    val newsLD = MutableLiveData<ArrayList<News>>()
    val loadingLD = MutableLiveData<Boolean>()
    val newsLoadErrorLD = MutableLiveData<Boolean>()

    val TAG = "volleyTag"
    private var queue: RequestQueue? = null


    init {
        queue = Volley.newRequestQueue(application)
    }

    fun refresh() {
        loadingLD.value = true
        newsLoadErrorLD.value = false

        queue?.let { queue ->
            val url = "http://10.0.2.2/anmp/news.json"

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val newsType = object : TypeToken<List<News>>() {}.type
                    val result = Gson().fromJson<List<News>>(response, newsType)
                    newsLD.value = result as ArrayList<News>?
                    loadingLD.value = false
                    Log.d(TAG, "Success Load List News")
                },
                { error ->
                    Log.d(TAG, "Error: $error") // Log any error that occurred
                    newsLoadErrorLD.value = true
                    loadingLD.value = false
                })

            stringRequest.tag = TAG
            queue.add(stringRequest)
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue?.cancelAll(TAG)
    }
}