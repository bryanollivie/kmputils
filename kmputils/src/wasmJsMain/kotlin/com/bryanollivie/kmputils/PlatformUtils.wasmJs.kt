package com.bryanollivie.kmputils

import kotlinx.browser.document
import kotlinx.browser.window

actual fun hideKeyboard() {
    (document.activeElement as? org.w3c.dom.HTMLElement)?.blur()
}

actual fun isNetworkAvailable(): Boolean =
    window.navigator.onLine

actual fun copyToClipboard(text: String) {
    window.navigator.clipboard.writeText(text)
}

actual fun isDarkMode(): Boolean {
    return window.matchMedia("(prefers-color-scheme: dark)").matches
}

actual fun getDeviceInfo(): DeviceInfo = DeviceInfo(
    model = "Browser",
    osName = "Web",
    osVersion = window.navigator.userAgent.take(50)
)

actual fun hapticFeedback() {
    // Web does not support haptic feedback
}
