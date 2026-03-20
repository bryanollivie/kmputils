package com.bryanollivie.kmputils

import kotlin.math.pow
import kotlinx.datetime.*

enum class CardBrand(val displayName: String, val lengths: List<Int>, val cvvLength: Int) {
    VISA("Visa", listOf(13, 16, 19), 3),
    MASTERCARD("Mastercard", listOf(16), 3),
    AMEX("American Express", listOf(15), 4),
    ELO("Elo", listOf(16), 3),
    HIPERCARD("Hipercard", listOf(16), 3),
    DINERS("Diners Club", listOf(14, 16), 3),
    DISCOVER("Discover", listOf(16, 19), 3),
    JCB("JCB", listOf(16, 19), 3),
    UNKNOWN("Unknown", listOf(16), 3)
}

object CreditCardUtils {

    fun detectBrand(number: String): CardBrand {
        val digits = number.filter { it.isDigit() }
        if (digits.isEmpty()) return CardBrand.UNKNOWN

        return when {
            // Elo (before Visa/Mastercard as some ranges overlap)
            digits.startsWith("636368") || digits.startsWith("438935") ||
            digits.startsWith("504175") || digits.startsWith("451416") ||
            digits.startsWith("636297") || digits.startsWith("506699") ||
            digits.startsWith("509") || digits.startsWith("650") ||
            digits.startsWith("6516") || digits.startsWith("6550") -> CardBrand.ELO

            // Hipercard
            digits.startsWith("606282") || digits.startsWith("3841") -> CardBrand.HIPERCARD

            // Amex
            digits.startsWith("34") || digits.startsWith("37") -> CardBrand.AMEX

            // Diners
            digits.startsWith("300") || digits.startsWith("301") ||
            digits.startsWith("302") || digits.startsWith("303") ||
            digits.startsWith("304") || digits.startsWith("305") ||
            digits.startsWith("36") || digits.startsWith("38") -> CardBrand.DINERS

            // Discover
            digits.startsWith("6011") || digits.startsWith("622") ||
            digits.startsWith("644") || digits.startsWith("645") ||
            digits.startsWith("646") || digits.startsWith("647") ||
            digits.startsWith("648") || digits.startsWith("649") ||
            digits.startsWith("65") -> CardBrand.DISCOVER

            // JCB
            digits.startsWith("35") -> CardBrand.JCB

            // Mastercard
            digits.startsWith("51") || digits.startsWith("52") ||
            digits.startsWith("53") || digits.startsWith("54") ||
            digits.startsWith("55") ||
            (digits.length >= 4 && digits.substring(0, 4).toIntOrNull()?.let { it in 2221..2720 } == true) -> CardBrand.MASTERCARD

            // Visa
            digits.startsWith("4") -> CardBrand.VISA

            else -> CardBrand.UNKNOWN
        }
    }

    fun isValidNumber(number: String): Boolean {
        val digits = number.filter { it.isDigit() }
        if (digits.length < 13 || digits.length > 19) return false
        return luhnCheck(digits)
    }

    fun isValidCvv(cvv: String, brand: CardBrand = CardBrand.UNKNOWN): Boolean {
        val digits = cvv.filter { it.isDigit() }
        return digits.length == brand.cvvLength
    }

    fun isValidExpiry(month: Int, year: Int): Boolean {
        if (month < 1 || month > 12) return false
        val instant = kotlinx.datetime.Clock.System.now()
        val tz = kotlinx.datetime.TimeZone.currentSystemDefault()
        val now = instant.toLocalDateTime(tz)
        val currentYear = now.year % 100
        val currentMonth = now.monthNumber

        val expYear = if (year > 100) year % 100 else year
        return when {
            expYear > currentYear -> true
            expYear == currentYear -> month >= currentMonth
            else -> false
        }
    }

    fun maskNumber(number: String): String {
        val digits = number.filter { it.isDigit() }
        if (digits.length < 8) return digits
        val first = digits.take(4)
        val last = digits.takeLast(4)
        val middle = "*".repeat(digits.length - 8)
        return MaskUtils.maskCreditCard("$first$middle$last")
    }

    fun formatForDisplay(number: String): String =
        MaskUtils.maskCreditCard(number)

    fun getInstallmentOptions(
        totalAmount: Double,
        maxInstallments: Int = 12,
        minInstallmentValue: Double = 5.0,
        interestRate: Double = 0.0
    ): List<InstallmentOption> {
        val options = mutableListOf<InstallmentOption>()
        for (i in 1..maxInstallments) {
            val totalWithInterest = if (interestRate > 0 && i > 1) {
                totalAmount * (1 + interestRate).pow(i.toDouble())
            } else {
                totalAmount
            }
            val installmentValue = totalWithInterest / i
            if (installmentValue < minInstallmentValue) break
            options.add(
                InstallmentOption(
                    installments = i,
                    installmentValue = installmentValue,
                    totalValue = totalWithInterest,
                    hasInterest = interestRate > 0 && i > 1
                )
            )
        }
        return options
    }

    private fun luhnCheck(digits: String): Boolean {
        var sum = 0
        var alternate = false
        for (i in digits.lastIndex downTo 0) {
            var n = digits[i] - '0'
            if (alternate) {
                n *= 2
                if (n > 9) n -= 9
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    }
}

data class InstallmentOption(
    val installments: Int,
    val installmentValue: Double,
    val totalValue: Double,
    val hasInterest: Boolean
)
