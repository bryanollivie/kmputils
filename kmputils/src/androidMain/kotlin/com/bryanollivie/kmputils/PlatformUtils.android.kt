package com.bryanollivie.kmputils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.inputmethod.InputMethodManager

object KMPUtilsContext {
    private var appContext: java.lang.ref.WeakReference<android.app.Application>? = null
    fun init(app: android.app.Application) { appContext = java.lang.ref.WeakReference(app) }
    fun get() = appContext?.get()
}

actual fun hideKeyboard() {
    val context = KMPUtilsContext.get() ?: return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

actual fun isNetworkAvailable(): Boolean {
    return try {
        val context = KMPUtilsContext.get() ?: return false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    } catch (e: Exception) {
        false
    }
}

actual fun copyToClipboard(text: String) {
    val context = KMPUtilsContext.get() ?: return
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText("text", text))
}

actual fun isDarkMode(): Boolean {
    val context = KMPUtilsContext.get() ?: return false
    val nightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return nightMode == Configuration.UI_MODE_NIGHT_YES
}

actual fun getDeviceInfo(): DeviceInfo = DeviceInfo(
    model = "${Build.MANUFACTURER} ${Build.MODEL}",
    osName = "Android",
    osVersion = "${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
)

actual fun hapticFeedback() {
    val context = KMPUtilsContext.get() ?: return
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    } catch (_: Exception) {}
}
