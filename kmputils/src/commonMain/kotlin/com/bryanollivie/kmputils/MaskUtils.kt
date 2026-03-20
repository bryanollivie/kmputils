package com.bryanollivie.kmputils

object MaskUtils {

    fun applyMask(raw: String, pattern: String): String {
        val digits = raw.filter { it.isDigit() }
        val result = StringBuilder()
        var digitIndex = 0
        for (char in pattern) {
            if (digitIndex >= digits.length) break
            if (char == '#') {
                result.append(digits[digitIndex])
                digitIndex++
            } else {
                result.append(char)
            }
        }
        return result.toString()
    }

    fun maskPhone(raw: String): String {
        val digits = raw.filter { it.isDigit() }
        return if (digits.length <= 10) {
            applyMask(digits, "(##) ####-####")
        } else {
            applyMask(digits, "(##) #####-####")
        }
    }

    fun maskCpf(raw: String): String =
        applyMask(raw, "###.###.###-##")

    fun maskCnpj(raw: String): String =
        applyMask(raw, "##.###.###/####-##")

    fun maskCreditCard(raw: String): String =
        applyMask(raw, "#### #### #### ####")

    fun maskDate(raw: String): String =
        applyMask(raw, "##/##/####")

    fun maskCep(raw: String): String =
        applyMask(raw, "#####-###")
}
