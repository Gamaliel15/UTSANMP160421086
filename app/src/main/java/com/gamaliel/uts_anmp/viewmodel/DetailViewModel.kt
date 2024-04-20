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

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    val loadingLD = MutableLiveData<Boolean>()
    val newsLoadErrorLD = MutableLiveData<Boolean>()
    val newsLD = MutableLiveData<News>()

    private val TAG = "DetailViewModel"
    private var queue: RequestQueue? = Volley.newRequestQueue(application)

    fun fetch(newsId: String) {
        loadingLD.value = true

        queue?.let { queue ->
            //val url = "http://10.0.2.2/anmp/news_$newsId.json"

            val url = "http://192.168.100.25/anmp/news_$newsId.json"

            val stringRequest = StringRequest(
                Request.Method.GET, url,
                { response ->
                    val newsType = object : TypeToken<News>() {}.type
                    val result = Gson().fromJson<News>(response, newsType)

                    newsLD.value = result
                    loadingLD.value = false

                    Log.d(TAG, "Success Load ID = $newsId")
                },
                { error ->
                    Log.d(TAG, "Error: $error")
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
