package com.bryanollivie.kmputils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual fun ImageBitmap.resize(width: Int, height: Int): ImageBitmap {
    val android = this.asAndroidBitmap()
    return Bitmap.createScaledBitmap(android, width, height, true).asImageBitmap()
}

actual fun ImageBitmap.toByteArray(format: ImageFormat, quality: Int): ByteArray {
    val android = this.asAndroidBitmap()
    val stream = ByteArrayOutputStream()
    val compressFormat = when (format) {
        ImageFormat.PNG -> Bitmap.CompressFormat.PNG
        ImageFormat.JPEG -> Bitmap.CompressFormat.JPEG
    }
    android.compress(compressFormat, quality, stream)
    return stream.toByteArray()
}

actual fun byteArrayToImageBitmap(bytes: ByteArray): ImageBitmap {
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
}

actual fun ImageBitmap.rotate(degrees: Float): ImageBitmap {
    val android = this.asAndroidBitmap()
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(android, 0, 0, android.width, android.height, matrix, true).asImageBitmap()
}

actual fun ImageBitmap.flipHorizontal(): ImageBitmap {
    val android = this.asAndroidBitmap()
    val matrix = Matrix().apply { preScale(-1f, 1f) }
    return Bitmap.createBitmap(android, 0, 0, android.width, android.height, matrix, true).asImageBitmap()
}

actual fun ImageBitmap.flipVertical(): ImageBitmap {
    val android = this.asAndroidBitmap()
    val matrix = Matrix().apply { preScale(1f, -1f) }
    return Bitmap.createBitmap(android, 0, 0, android.width, android.height, matrix, true).asImageBitmap()
}

actual fun ImageBitmap.crop(x: Int, y: Int, width: Int, height: Int): ImageBitmap {
    val android = this.asAndroidBitmap()
    val safeX = x.coerceIn(0, android.width - 1)
    val safeY = y.coerceIn(0, android.height - 1)
    val safeW = width.coerceAtMost(android.width - safeX)
    val safeH = height.coerceAtMost(android.height - safeY)
    return Bitmap.createBitmap(android, safeX, safeY, safeW, safeH).asImageBitmap()
}

actual fun ImageBitmap.toGrayscale(): ImageBitmap {
    val android = this.asAndroidBitmap()
    val result = Bitmap.createBitmap(android.width, android.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)
    val paint = Paint().apply {
        colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
    }
    canvas.drawBitmap(android, 0f, 0f, paint)
    return result.asImageBitmap()
}

actual fun ImageBitmap.applyTint(color: Color, alpha: Float): ImageBitmap {
    val bmp = this.asAndroidBitmap().copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(bmp)
    val argb = android.graphics.Color.argb(
        (alpha * 255).toInt(),
        (color.red * 255).toInt(),
        (color.green * 255).toInt(),
        (color.blue * 255).toInt()
    )
    val paint = Paint().apply {
        this.color = argb
    }
    canvas.drawRect(0f, 0f, bmp.width.toFloat(), bmp.height.toFloat(), paint)
    return bmp.asImageBitmap()
}
