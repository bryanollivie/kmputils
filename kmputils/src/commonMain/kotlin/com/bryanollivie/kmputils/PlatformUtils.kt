package com.bryanollivie.kmputils

expect fun hideKeyboard()

expect fun isNetworkAvailable(): Boolean

expect fun copyToClipboard(text: String)

expect fun isDarkMode(): Boolean

expect fun getDeviceInfo(): DeviceInfo

expect fun hapticFeedback()

data class DeviceInfo(
    val model: String,
    val osName: String,
    val osVersion: String
)
