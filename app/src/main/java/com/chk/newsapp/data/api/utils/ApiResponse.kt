package com.chk.newsapp.data.api.utils

import retrofit2.Response
import java.io.IOException

/**
 * Created by chira on 15-08-2017.
 */
data class ApiResponse<RequestType>(val response: Response<RequestType>?, val throwable: Throwable?) {
    var errorMessage: String? = ""
    var body: RequestType? = null
    var code: Int? = 0

    init {
        if (response != null) {

            code = response.code()

            if (response.isSuccessful) {
                body = response.body()
            } else {
                var message: String? = null
                if (response.errorBody() != null) {
                    try {
                        message = response.errorBody().toString()
                    } catch (ignored: IOException) {
                    }
                }
                if (message == null || message.trim({ it <= ' ' }).isEmpty()) {
                    message = response.message()
                }
                errorMessage = message;
            }
        } else if (throwable != null) {
            errorMessage = throwable.message
        }
    }

    fun isSuccessful(): Boolean {
        return (code == 200)
    }

}

