package com.bryanollivie.kmputils

import kotlinx.coroutines.flow.*

object EventBus {
    @PublishedApi
    internal val _events = MutableSharedFlow<Any>(extraBufferCapacity = 64)

    suspend fun emit(event: Any) {
        _events.emit(event)
    }

    fun tryEmit(event: Any): Boolean =
        _events.tryEmit(event)

    inline fun <reified T> subscribe(): Flow<T> =
        _events.filterIsInstance<T>()

    fun subscribeAll(): Flow<Any> = _events.asSharedFlow()
}

class TypedEventBus<T> {
    private val _events = MutableSharedFlow<T>(extraBufferCapacity = 64)

    suspend fun emit(event: T) {
        _events.emit(event)
    }

    fun tryEmit(event: T): Boolean =
        _events.tryEmit(event)

    fun subscribe(): Flow<T> = _events.asSharedFlow()
}
