package com.bryanollivie.kmputils

enum class CurrencyCode(val symbol: String, val decimalSeparator: Char, val thousandSeparator: Char) {
    BRL("R$", ',', '.'),
    USD("$", '.', ','),
    EUR("€", ',', '.')
}

object CurrencyUtils {

    private val rates = mapOf(
        (CurrencyCode.USD to CurrencyCode.BRL) to 5.20,
        (CurrencyCode.BRL to CurrencyCode.USD) to 0.19,
        (CurrencyCode.USD to CurrencyCode.EUR) to 0.92,
        (CurrencyCode.EUR to CurrencyCode.USD) to 1.09,
        (CurrencyCode.BRL to CurrencyCode.EUR) to 0.18,
        (CurrencyCode.EUR to CurrencyCode.BRL) to 5.65
    )

    fun format(value: Double, currency: CurrencyCode): String {
        val intPart = value.toLong()
        val decPart = ((value - intPart) * 100).toLong().let {
            if (it < 0) -it else it
        }
        val formattedInt = formatWithThousands(intPart, currency.thousandSeparator)
        val formattedDec = decPart.toString().padStart(2, '0')
        return "${currency.symbol} $formattedInt${currency.decimalSeparator}$formattedDec"
    }

    fun convert(value: Double, from: CurrencyCode, to: CurrencyCode): Double {
        if (from == to) return value
        val rate = rates[from to to] ?: 1.0
        return value * rate
    }

    private fun formatWithThousands(value: Long, separator: Char): String {
        val str = value.toString()
        val result = StringBuilder()
        var count = 0
        for (i in str.lastIndex downTo 0) {
            if (count > 0 && count % 3 == 0 && str[i] != '-') {
                result.insert(0, separator)
            }
            result.insert(0, str[i])
            count++
        }
        return result.toString()
    }
}
