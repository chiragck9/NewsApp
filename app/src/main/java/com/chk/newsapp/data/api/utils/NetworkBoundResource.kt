package com.chk.newsapp.data.api.utils

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import com.chk.newsapp.data.executors.AppExecutor
import com.chk.newsapp.data.executors.MainThreadExecutor

/**
 * Created by chira on 15-08-2017.
 */


abstract class NetworkBoundResource<ResultType, RequestType> @MainThread
internal constructor(appExecutor: AppExecutor, private val mainThreadExecutor: MainThreadExecutor) {

    private val result = MediatorLiveData<Resource<ResultType>>()
    private val appExecutor: AppExecutor

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    // Called with the data in the database to decide whether it should be
    // fetched from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to get the cached data from the database
    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    // Called to create the API call.
    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected fun onFetchFailed() {
    }

    // returns a LiveData that represents the resource
    val asLiveData: LiveData<Resource<ResultType>>
        get() = result

    init {
        this.appExecutor = appExecutor

        result.value = Resource.loading<ResultType>(null)
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource
                ) { newData -> result.setValue(Resource.success<ResultType>(newData!!)) }
            }
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()

        // we re-attach dbSource as a new source,
        // it will dispatch its latest value quickly
        result.addSource(dbSource
        ) { newData -> result.setValue(Resource.loading(newData)) }
        result.addSource(apiResponse) { response ->

            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            if (response != null) {
                if (response.isSuccessful()) {
                    appExecutor.execute {
                        saveCallResult(response.body!!)

                        mainThreadExecutor.execute {
                            result.addSource(loadFromDb()
                            ) { newData -> result.setValue(Resource.success<ResultType>(newData!!)) }
                        }
                    }
                } else {
                    onFetchFailed()
                    result.addSource(dbSource
                    ) { newData -> result.setValue(Resource.error(response.errorMessage!!, newData)) }
                }
            }
        }
    }
}
