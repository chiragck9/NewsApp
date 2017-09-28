package com.chk.newsapp.data.api

import android.arch.lifecycle.LiveData
import com.chk.newsapp.data.api.model.Headlines
import com.chk.newsapp.data.api.model.Sources
import com.chk.newsapp.data.api.utils.ApiResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by chira on 14-08-2017.
 */
interface NewsApi {

    @GET("top-headlines?")
    fun getHeadlines(@Query("sources") source: String, @Query("category") category: String, @Query("apiKey") apiKey: String): LiveData<ApiResponse<Headlines>>

    @GET("top-headlines")
    fun getHeadlinesObservable(@Query("sources") source: String, @Query("apiKey") apiKey: String): Observable<Headlines>

    @GET("sources?")
    fun getSources(@Query("category") category: String, @Query("language") language: String, @Query("country") country: String, @Query("apiKey") apiKey: String): LiveData<ApiResponse<Sources>>

}