package com.bryanollivie.kmputils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import kotlin.math.roundToInt

expect fun ImageBitmap.resize(width: Int, height: Int): ImageBitmap

expect fun ImageBitmap.toByteArray(format: ImageFormat = ImageFormat.PNG, quality: Int = 100): ByteArray

expect fun byteArrayToImageBitmap(bytes: ByteArray): ImageBitmap

expect fun ImageBitmap.rotate(degrees: Float): ImageBitmap

expect fun ImageBitmap.flipHorizontal(): ImageBitmap

expect fun ImageBitmap.flipVertical(): ImageBitmap

expect fun ImageBitmap.crop(x: Int, y: Int, width: Int, height: Int): ImageBitmap

expect fun ImageBitmap.toGrayscale(): ImageBitmap

expect fun ImageBitmap.applyTint(color: Color, alpha: Float = 0.5f): ImageBitmap

enum class ImageFormat {
    PNG, JPEG
}

object ImageUtils {

    fun aspectRatio(width: Int, height: Int): Float =
        if (height > 0) width.toFloat() / height.toFloat() else 0f

    fun fitSize(
        srcWidth: Int,
        srcHeight: Int,
        maxWidth: Int,
        maxHeight: Int
    ): Pair<Int, Int> {
        if (srcWidth <= maxWidth && srcHeight <= maxHeight) return srcWidth to srcHeight
        val ratio = aspectRatio(srcWidth, srcHeight)
        return if (ratio > aspectRatio(maxWidth, maxHeight)) {
            maxWidth to (maxWidth / ratio).roundToInt()
        } else {
            (maxHeight * ratio).roundToInt() to maxHeight
        }
    }

    fun fillSize(
        srcWidth: Int,
        srcHeight: Int,
        targetWidth: Int,
        targetHeight: Int
    ): Pair<Int, Int> {
        val ratio = aspectRatio(srcWidth, srcHeight)
        return if (ratio > aspectRatio(targetWidth, targetHeight)) {
            (targetHeight * ratio).roundToInt() to targetHeight
        } else {
            targetWidth to (targetWidth / ratio).roundToInt()
        }
    }

    fun estimateFileSize(width: Int, height: Int, format: ImageFormat): Long {
        val pixels = width.toLong() * height.toLong()
        return when (format) {
            ImageFormat.PNG -> pixels * 4
            ImageFormat.JPEG -> pixels / 4
        }
    }

    fun formatFileSize(bytes: Long): String = when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${"%.1f".format(bytes / 1024.0)} KB"
        else -> "${"%.1f".format(bytes / (1024.0 * 1024.0))} MB"
    }
}
