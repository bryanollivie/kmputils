package com.bryanollivie.kmputils

import kotlinx.datetime.Clock

class RateLimiter(
    private val maxCalls: Int,
    private val windowMs: Long
) {
    private val timestamps = mutableListOf<Long>()

    fun tryAcquire(): Boolean {
        val now = Clock.System.now().toEpochMilliseconds()
        timestamps.removeAll { now - it > windowMs }
        return if (timestamps.size < maxCalls) {
            timestamps.add(now)
            true
        } else {
            false
        }
    }

    fun remainingCalls(): Int {
        val now = Clock.System.now().toEpochMilliseconds()
        timestamps.removeAll { now - it > windowMs }
        return (maxCalls - timestamps.size).coerceAtLeast(0)
    }

    fun timeUntilNextSlot(): Long {
        if (timestamps.size < maxCalls) return 0
        val now = Clock.System.now().toEpochMilliseconds()
        val oldest = timestamps.minOrNull() ?: return 0
        return (oldest + windowMs - now).coerceAtLeast(0)
    }

    fun reset() = timestamps.clear()
}
