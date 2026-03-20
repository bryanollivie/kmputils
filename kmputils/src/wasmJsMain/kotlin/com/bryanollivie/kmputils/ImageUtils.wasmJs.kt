package com.bryanollivie.kmputils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

actual fun ImageBitmap.resize(width: Int, height: Int): ImageBitmap = this
actual fun ImageBitmap.toByteArray(format: ImageFormat, quality: Int): ByteArray = ByteArray(0)
actual fun byteArrayToImageBitmap(bytes: ByteArray): ImageBitmap = ImageBitmap(1, 1)
actual fun ImageBitmap.rotate(degrees: Float): ImageBitmap = this
actual fun ImageBitmap.flipHorizontal(): ImageBitmap = this
actual fun ImageBitmap.flipVertical(): ImageBitmap = this
actual fun ImageBitmap.crop(x: Int, y: Int, width: Int, height: Int): ImageBitmap = this
actual fun ImageBitmap.toGrayscale(): ImageBitmap = this
actual fun ImageBitmap.applyTint(color: Color, alpha: Float): ImageBitmap = this
