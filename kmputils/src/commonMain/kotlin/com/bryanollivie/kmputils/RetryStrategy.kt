package com.bryanollivie.kmputils

import kotlinx.coroutines.delay
import kotlin.math.pow

object RetryStrategy {

    suspend fun <T> withExponentialBackoff(
        maxRetries: Int = 3,
        initialDelayMs: Long = 1000L,
        maxDelayMs: Long = 30000L,
        factor: Double = 2.0,
        onRetry: ((attempt: Int, exception: Exception) -> Unit)? = null,
        block: suspend (attempt: Int) -> T
    ): T {
        var currentDelay = initialDelayMs
        repeat(maxRetries) { attempt ->
            try {
                return block(attempt + 1)
            } catch (e: Exception) {
                onRetry?.invoke(attempt + 1, e)
                if (attempt == maxRetries - 1) throw e
                delay(currentDelay)
                currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelayMs)
            }
        }
        return block(maxRetries)
    }

    suspend fun <T> withLinearBackoff(
        maxRetries: Int = 3,
        delayMs: Long = 1000L,
        onRetry: ((attempt: Int, exception: Exception) -> Unit)? = null,
        block: suspend (attempt: Int) -> T
    ): T {
        repeat(maxRetries) { attempt ->
            try {
                return block(attempt + 1)
            } catch (e: Exception) {
                onRetry?.invoke(attempt + 1, e)
                if (attempt == maxRetries - 1) throw e
                delay(delayMs * (attempt + 1))
            }
        }
        return block(maxRetries)
    }
}
