@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.bryanollivie.kmputils

import platform.Foundation.NSSelectorFromString
import platform.UIKit.*

actual fun hideKeyboard() {
    UIApplication.sharedApplication.sendAction(
        NSSelectorFromString("resignFirstResponder"),
        to = null,
        from = null,
        forEvent = null
    )
}

actual fun isNetworkAvailable(): Boolean = true

actual fun copyToClipboard(text: String) {
    UIPasteboard.generalPasteboard.string = text
}

actual fun isDarkMode(): Boolean {
    return UIScreen.mainScreen.traitCollection.userInterfaceStyle == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}

actual fun getDeviceInfo(): DeviceInfo = DeviceInfo(
    model = UIDevice.currentDevice.model,
    osName = UIDevice.currentDevice.systemName,
    osVersion = UIDevice.currentDevice.systemVersion
)

actual fun hapticFeedback() {
    val generator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
    generator.prepare()
    generator.impactOccurred()
}
