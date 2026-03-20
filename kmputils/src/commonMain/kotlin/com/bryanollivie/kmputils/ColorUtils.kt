package com.bryanollivie.kmputils

import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.roundToInt

object ColorUtils {

    fun hexToColor(hex: String): Color {
        val clean = hex.removePrefix("#")
        val value = clean.toLong(16)
        return when (clean.length) {
            6 -> Color(
                red = ((value shr 16) and 0xFF).toInt(),
                green = ((value shr 8) and 0xFF).toInt(),
                blue = (value and 0xFF).toInt()
            )
            8 -> Color(
                alpha = ((value shr 24) and 0xFF).toInt(),
                red = ((value shr 16) and 0xFF).toInt(),
                green = ((value shr 8) and 0xFF).toInt(),
                blue = (value and 0xFF).toInt()
            )
            else -> Color.Black
        }
    }

    fun colorToHex(color: Color): String {
        val r = (color.red * 255).roundToInt()
        val g = (color.green * 255).roundToInt()
        val b = (color.blue * 255).roundToInt()
        return "#${r.toString(16).padStart(2, '0')}${g.toString(16).padStart(2, '0')}${b.toString(16).padStart(2, '0')}".uppercase()
    }

    fun darken(color: Color, factor: Float = 0.2f): Color {
        val f = (1f - factor).coerceIn(0f, 1f)
        return Color(
            red = color.red * f,
            green = color.green * f,
            blue = color.blue * f,
            alpha = color.alpha
        )
    }

    fun lighten(color: Color, factor: Float = 0.2f): Color {
        val f = factor.coerceIn(0f, 1f)
        return Color(
            red = color.red + (1f - color.red) * f,
            green = color.green + (1f - color.green) * f,
            blue = color.blue + (1f - color.blue) * f,
            alpha = color.alpha
        )
    }

    fun contrastRatio(color1: Color, color2: Color): Double {
        val l1 = luminance(color1)
        val l2 = luminance(color2)
        val lighter = maxOf(l1, l2)
        val darker = minOf(l1, l2)
        return (lighter + 0.05) / (darker + 0.05)
    }

    fun isAccessible(foreground: Color, background: Color, level: String = "AA"): Boolean {
        val ratio = contrastRatio(foreground, background)
        return when (level) {
            "AA" -> ratio >= 4.5
            "AAA" -> ratio >= 7.0
            else -> ratio >= 4.5
        }
    }

    fun toRgbString(color: Color): String {
        val r = (color.red * 255).roundToInt()
        val g = (color.green * 255).roundToInt()
        val b = (color.blue * 255).roundToInt()
        return "rgb($r, $g, $b)"
    }

    private fun luminance(color: Color): Double {
        fun adjust(c: Float): Double {
            val v = c.toDouble()
            return if (v <= 0.03928) v / 12.92 else ((v + 0.055) / 1.055).pow(2.4)
        }
        return 0.2126 * adjust(color.red) + 0.7152 * adjust(color.green) + 0.0722 * adjust(color.blue)
    }
}
