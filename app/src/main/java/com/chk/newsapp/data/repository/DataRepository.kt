package com.chk.newsapp.data.repository

import android.arch.lifecycle.LiveData
import com.chk.newsapp.data.api.model.Headlines
import com.chk.newsapp.data.api.utils.ApiResponse
import com.chk.newsapp.data.api.utils.Resource
import com.chk.newsapp.data.datamodel.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Created by chira on 14-08-2017.
 */
interface DataRepository {

    fun getHeadlinesForMultipleSources(sourceEntities: List<SourceEntity>, newsApiKey: String): Observable<HeadlinesEntity>

    fun getAllHeadlinesFromStorage(): LiveData<Resource<List<HeadlinesEntity>>>

    fun getSources(category: String, language: String, country: String, apiKey: String): LiveData<Resource<List<SourceEntity>>>

    fun getSubscribedSources(): LiveData<Resource<List<SourceEntity>>>

    fun getSourcesByCategory(category: Category, apiKey: String): LiveData<Resource<List<SourceEntity>>>

    fun getArticleDetail(url: String): LiveData<Resource<ArticleDetailEntity>>

    fun setSourceSubscribed(source: SourceEntity): Observable<SourceEntity>

    fun deleteHeadlines(): Observable<Integer>
}