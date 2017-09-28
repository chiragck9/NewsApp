package com.chk.newsapp.data.api.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by chira on 14-08-2017.
 */
data class Source(

        var id: String = "",
        var name: String = "",
        var description: String = "",
        var url: String = "",
        var category: String = "",
        var language: String = "",
        var country: String = ""
)