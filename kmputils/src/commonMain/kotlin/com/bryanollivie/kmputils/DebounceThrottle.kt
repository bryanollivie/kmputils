package com.bryanollivie.kmputils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

object DebounceThrottle {

    fun <T> Flow<T>.debounce(timeMillis: Long): Flow<T> =
        this.debounce(timeMillis)

    fun <T> debounceLatest(
        delayMs: Long = 300L,
        scope: CoroutineScope,
        action: suspend (T) -> Unit
    ): (T) -> Unit {
        var job: Job? = null
        return { value: T ->
            job?.cancel()
            job = scope.launch {
                delay(delayMs)
                action(value)
            }
        }
    }

    fun throttleFirst(
        intervalMs: Long = 1000L,
        action: () -> Unit
    ): () -> Unit {
        var lastExecution = 0L
        return {
            val now = currentTimeMillis()
            if (now - lastExecution >= intervalMs) {
                lastExecution = now
                action()
            }
        }
    }

    fun throttleLatest(
        intervalMs: Long = 1000L,
        scope: CoroutineScope,
        action: suspend () -> Unit
    ): () -> Unit {
        var job: Job? = null
        var lastExecution = 0L
        return {
            val now = currentTimeMillis()
            job?.cancel()
            val remaining = intervalMs - (now - lastExecution)
            job = scope.launch {
                if (remaining > 0) delay(remaining)
                lastExecution = currentTimeMillis()
                action()
            }
        }
    }

    private fun currentTimeMillis(): Long =
        kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
}
