package com.tincho5588.reddittopsreader.domain.usecase

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<T>(val status: Status, val data: T?, val message: String?, val httpResponseCode: Int) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, 200)
        }

        fun <T> error(msg: String, httpResponseCode: Int, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg, httpResponseCode)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null, 100)
        }
    }
}