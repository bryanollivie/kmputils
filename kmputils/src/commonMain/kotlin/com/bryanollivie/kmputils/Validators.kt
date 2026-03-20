package com.bryanollivie.kmputils

object Validators {

    fun isValidEmail(email: String): Boolean {
        val pattern = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")
        return pattern.matches(email.trim())
    }

    fun isValidPhone(phone: String): Boolean {
        val digits = phone.filter { it.isDigit() }
        return digits.length in 10..11
    }

    fun isValidCpf(cpf: String): Boolean {
        val digits = cpf.filter { it.isDigit() }
        if (digits.length != 11) return false
        if (digits.all { it == digits[0] }) return false

        // First check digit
        var sum = 0
        for (i in 0..8) {
            sum += (digits[i] - '0') * (10 - i)
        }
        var remainder = sum % 11
        val firstDigit = if (remainder < 2) 0 else 11 - remainder

        if ((digits[9] - '0') != firstDigit) return false

        // Second check digit
        sum = 0
        for (i in 0..9) {
            sum += (digits[i] - '0') * (11 - i)
        }
        remainder = sum % 11
        val secondDigit = if (remainder < 2) 0 else 11 - remainder

        return (digits[10] - '0') == secondDigit
    }

    fun isValidCnpj(cnpj: String): Boolean {
        val digits = cnpj.filter { it.isDigit() }
        if (digits.length != 14) return false
        if (digits.all { it == digits[0] }) return false

        val weights1 = intArrayOf(5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)
        val weights2 = intArrayOf(6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2)

        var sum = 0
        for (i in 0..11) {
            sum += (digits[i] - '0') * weights1[i]
        }
        var remainder = sum % 11
        val firstDigit = if (remainder < 2) 0 else 11 - remainder
        if ((digits[12] - '0') != firstDigit) return false

        sum = 0
        for (i in 0..12) {
            sum += (digits[i] - '0') * weights2[i]
        }
        remainder = sum % 11
        val secondDigit = if (remainder < 2) 0 else 11 - remainder

        return (digits[13] - '0') == secondDigit
    }
}
