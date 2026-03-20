package com.bryanollivie.kmputils

import kotlinx.datetime.*

object DateUtils {

    fun now(): Long = Clock.System.now().toEpochMilliseconds()

    fun formatDate(millis: Long, pattern: String = "dd/MM/yyyy"): String {
        val instant = Instant.fromEpochMilliseconds(millis)
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        return pattern
            .replace("yyyy", dateTime.year.toString())
            .replace("MM", dateTime.monthNumber.toString().padStart(2, '0'))
            .replace("dd", dateTime.dayOfMonth.toString().padStart(2, '0'))
            .replace("HH", dateTime.hour.toString().padStart(2, '0'))
            .replace("mm", dateTime.minute.toString().padStart(2, '0'))
            .replace("ss", dateTime.second.toString().padStart(2, '0'))
    }

    fun relativeTime(millis: Long, nowMillis: Long = now(), lang: String = "en"): String {
        val diffSeconds = (nowMillis - millis) / 1000
        val diffMinutes = diffSeconds / 60
        val diffHours = diffMinutes / 60
        val diffDays = diffHours / 24

        return when {
            diffSeconds < 60 -> if (lang == "pt") "agora mesmo" else "just now"
            diffMinutes < 60 -> if (lang == "pt") "há $diffMinutes min" else "$diffMinutes min ago"
            diffHours < 24 -> if (lang == "pt") "há $diffHours h" else "$diffHours h ago"
            diffDays < 30 -> if (lang == "pt") "há $diffDays dias" else "$diffDays days ago"
            diffDays < 365 -> {
                val months = diffDays / 30
                if (lang == "pt") "há $months meses" else "$months months ago"
            }
            else -> {
                val years = diffDays / 365
                if (lang == "pt") "há $years anos" else "$years years ago"
            }
        }
    }
}
