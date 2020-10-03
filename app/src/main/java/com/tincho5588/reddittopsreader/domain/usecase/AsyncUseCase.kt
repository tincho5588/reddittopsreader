package com.tincho5588.reddittopsreader.domain.usecase

import androidx.lifecycle.LiveData

/**
 * Abstraction of an Asynchronous Use Case. When executed, the Use Case will return an
 * observable LiveData<> object that contains a Resource object representing the output of the
 * use case.
 * Callers shall observe said object to retrieved the output of the use case
 */
abstract class AsyncUseCase<Q : AsyncUseCase.RequestValues, P> {

    var requestValues: Q? = null

    protected abstract fun executeUseCase(requestValues: Q?): LiveData<Resource<P>>

    fun run(): LiveData<Resource<P>> {
        return executeUseCase(requestValues)
    }

    /**
     * Data passed to a request.
     */
    interface RequestValues
}