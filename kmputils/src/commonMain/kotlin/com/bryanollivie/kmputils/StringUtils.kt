package com.bryanollivie.kmputils

object StringUtils {

    private val accentMap = mapOf(
        'á' to 'a', 'à' to 'a', 'â' to 'a', 'ã' to 'a', 'ä' to 'a',
        'é' to 'e', 'è' to 'e', 'ê' to 'e', 'ë' to 'e',
        'í' to 'i', 'ì' to 'i', 'î' to 'i', 'ï' to 'i',
        'ó' to 'o', 'ò' to 'o', 'ô' to 'o', 'õ' to 'o', 'ö' to 'o',
        'ú' to 'u', 'ù' to 'u', 'û' to 'u', 'ü' to 'u',
        'ç' to 'c', 'ñ' to 'n',
        'Á' to 'A', 'À' to 'A', 'Â' to 'A', 'Ã' to 'A', 'Ä' to 'A',
        'É' to 'E', 'È' to 'E', 'Ê' to 'E', 'Ë' to 'E',
        'Í' to 'I', 'Ì' to 'I', 'Î' to 'I', 'Ï' to 'I',
        'Ó' to 'O', 'Ò' to 'O', 'Ô' to 'O', 'Õ' to 'O', 'Ö' to 'O',
        'Ú' to 'U', 'Ù' to 'U', 'Û' to 'U', 'Ü' to 'U',
        'Ç' to 'C', 'Ñ' to 'N'
    )

    fun capitalize(text: String): String =
        text.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.uppercase() else it.toString() }
        }

    fun truncate(text: String, maxLen: Int, suffix: String = "..."): String =
        if (text.length <= maxLen) text
        else text.take(maxLen - suffix.length) + suffix

    fun removeAccents(text: String): String =
        text.map { accentMap[it] ?: it }.joinToString("")

    fun toSlug(text: String): String =
        removeAccents(text.lowercase())
            .replace(Regex("[^a-z0-9\\s-]"), "")
            .replace(Regex("\\s+"), "-")
            .replace(Regex("-+"), "-")
            .trim('-')
}
