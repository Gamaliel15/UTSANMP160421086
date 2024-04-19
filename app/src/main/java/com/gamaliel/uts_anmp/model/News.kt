package com.gamaliel.uts_anmp.model

import com.google.gson.annotations.SerializedName

data class News(val id_news:String,
                val judul:String,
                val penulis:String,
                val isi:String,
                val url_photo:String,
                val paragraf:List<String>,)
