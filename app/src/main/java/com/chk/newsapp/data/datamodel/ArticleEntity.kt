package com.chk.newsapp.data.datamodel

import android.arch.persistence.room.Entity

/**
 * Created by chira on 14-08-2017.
 */
data class ArticleEntity(

        var sourceId: String = "",
        var sourceName: String = "",
        var author: String? = "",
        var title: String = "",
        var description: String = "",
        var url: String = "",
        var urlToImage: String? = "",
        var publishedAt: String? = ""
)