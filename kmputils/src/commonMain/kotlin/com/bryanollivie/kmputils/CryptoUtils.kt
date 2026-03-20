package com.bryanollivie.kmputils

import kotlin.random.Random

object CryptoUtils {

    fun encrypt(text: String, key: String): String {
        val keyBytes = expandKey(key, text.length)
        val encrypted = text.mapIndexed { index, char ->
            (char.code xor keyBytes[index].code).toByte()
        }.toByteArray()
        return bytesToHex(encrypted)
    }

    fun decrypt(hexText: String, key: String): String {
        val bytes = hexToBytes(hexText)
        val keyBytes = expandKey(key, bytes.size)
        return bytes.mapIndexed { index, byte ->
            (byte.toInt() xor keyBytes[index].code).toChar()
        }.joinToString("")
    }

    fun hash(text: String): String {
        var h = 0x811c9dc5.toInt()
        for (char in text) {
            h = h xor char.code
            h = (h * 0x01000193)
        }
        return h.toUInt().toString(16).padStart(8, '0')
    }

    fun generateToken(length: Int = 32): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length).map { chars[Random.nextInt(chars.length)] }.joinToString("")
    }

    fun base64Encode(text: String): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        val bytes = text.encodeToByteArray()
        val result = StringBuilder()
        var i = 0
        while (i < bytes.size) {
            val b0 = bytes[i].toInt() and 0xFF
            val b1 = if (i + 1 < bytes.size) bytes[i + 1].toInt() and 0xFF else 0
            val b2 = if (i + 2 < bytes.size) bytes[i + 2].toInt() and 0xFF else 0
            result.append(chars[(b0 shr 2) and 0x3F])
            result.append(chars[((b0 shl 4) or (b1 shr 4)) and 0x3F])
            result.append(if (i + 1 < bytes.size) chars[((b1 shl 2) or (b2 shr 6)) and 0x3F] else '=')
            result.append(if (i + 2 < bytes.size) chars[b2 and 0x3F] else '=')
            i += 3
        }
        return result.toString()
    }

    fun base64Decode(encoded: String): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        val stripped = encoded.trimEnd('=')
        val bytes = mutableListOf<Byte>()
        var i = 0
        while (i < stripped.length) {
            val b0 = chars.indexOf(stripped[i])
            val b1 = if (i + 1 < stripped.length) chars.indexOf(stripped[i + 1]) else 0
            val b2 = if (i + 2 < stripped.length) chars.indexOf(stripped[i + 2]) else 0
            val b3 = if (i + 3 < stripped.length) chars.indexOf(stripped[i + 3]) else 0
            bytes.add(((b0 shl 2) or (b1 shr 4)).toByte())
            if (i + 2 < stripped.length) bytes.add((((b1 shl 4) or (b2 shr 2)) and 0xFF).toByte())
            if (i + 3 < stripped.length) bytes.add((((b2 shl 6) or b3) and 0xFF).toByte())
            i += 4
        }
        return bytes.toByteArray().decodeToString()
    }

    private fun expandKey(key: String, length: Int): String {
        val sb = StringBuilder()
        while (sb.length < length) sb.append(key)
        return sb.substring(0, length)
    }

    private fun bytesToHex(bytes: ByteArray): String =
        bytes.joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }

    private fun hexToBytes(hex: String): ByteArray =
        ByteArray(hex.length / 2) { hex.substring(it * 2, it * 2 + 2).toInt(16).toByte() }
}
