package com.tincho5588.reddittopsreader.domain.usecase

/**
 * Abstraction of an Synchronous Use Case. When executed, the Use Case will return a Resource
 * object representing the output of the use case.
 * The status and data within the Resource will immediately in place for the caller to use.
 */
abstract class SyncUseCase<Q : SyncUseCase.RequestValues, P> {

    var requestValues: Q? = null

    protected abstract fun executeUseCase(requestValues: Q?): Resource<P>

    fun run(): Resource<P> {
        return executeUseCase(requestValues)
    }

    /**
     * Data passed to a request.
     */
    interface RequestValues
}