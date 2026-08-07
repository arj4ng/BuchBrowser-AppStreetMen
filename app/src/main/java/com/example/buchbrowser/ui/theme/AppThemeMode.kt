package com.example.buchbrowser.ui.theme

enum class AppThemeMode(
    val storageValue: String,
    val displayName: String,
) {
    SYSTEM("system", "System"),
    PAPER("paper", "Paper"),
    MIDNIGHT("midnight", "Midnight"),
    SUNSET("sunset", "Sunset");

    companion object {
        fun fromStorageValue(value: String?): AppThemeMode {
            return entries.firstOrNull { it.storageValue == value } ?: SYSTEM
        }
    }
}
