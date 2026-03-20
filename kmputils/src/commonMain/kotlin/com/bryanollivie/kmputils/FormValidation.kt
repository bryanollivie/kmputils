package com.bryanollivie.kmputils

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)

fun interface ValidationRule {
    fun validate(value: String): ValidationResult
}

object FormRules {

    fun required(message: String = "Field is required"): ValidationRule =
        ValidationRule { value ->
            if (value.isBlank()) ValidationResult(false, message)
            else ValidationResult(true)
        }

    fun minLength(min: Int, message: String = "Minimum $min characters"): ValidationRule =
        ValidationRule { value ->
            if (value.length < min) ValidationResult(false, message)
            else ValidationResult(true)
        }

    fun maxLength(max: Int, message: String = "Maximum $max characters"): ValidationRule =
        ValidationRule { value ->
            if (value.length > max) ValidationResult(false, message)
            else ValidationResult(true)
        }

    fun email(message: String = "Invalid email"): ValidationRule =
        ValidationRule { value ->
            if (Validators.isValidEmail(value)) ValidationResult(true)
            else ValidationResult(false, message)
        }

    fun cpf(message: String = "Invalid CPF"): ValidationRule =
        ValidationRule { value ->
            if (Validators.isValidCpf(value)) ValidationResult(true)
            else ValidationResult(false, message)
        }

    fun cnpj(message: String = "Invalid CNPJ"): ValidationRule =
        ValidationRule { value ->
            if (Validators.isValidCnpj(value)) ValidationResult(true)
            else ValidationResult(false, message)
        }

    fun matches(regex: Regex, message: String = "Invalid format"): ValidationRule =
        ValidationRule { value ->
            if (regex.matches(value)) ValidationResult(true)
            else ValidationResult(false, message)
        }

    fun custom(message: String = "Invalid", predicate: (String) -> Boolean): ValidationRule =
        ValidationRule { value ->
            if (predicate(value)) ValidationResult(true)
            else ValidationResult(false, message)
        }
}

class FormValidator {
    private val fields = mutableMapOf<String, List<ValidationRule>>()

    fun addField(name: String, vararg rules: ValidationRule): FormValidator {
        fields[name] = rules.toList()
        return this
    }

    fun validateField(name: String, value: String): ValidationResult {
        val rules = fields[name] ?: return ValidationResult(true)
        for (rule in rules) {
            val result = rule.validate(value)
            if (!result.isValid) return result
        }
        return ValidationResult(true)
    }

    fun validateAll(values: Map<String, String>): Map<String, ValidationResult> {
        return fields.keys.associateWith { fieldName ->
            validateField(fieldName, values[fieldName] ?: "")
        }
    }

    fun isFormValid(values: Map<String, String>): Boolean =
        validateAll(values).values.all { it.isValid }
}
