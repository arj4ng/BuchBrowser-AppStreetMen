package com.example.buchbrowser.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

@Composable
fun BuchBrowserTheme(
    appThemeMode: AppThemeMode = AppThemeMode.SYSTEM,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = resolveAppColorScheme(
        appThemeMode = appThemeMode,
        isDarkTheme = darkTheme,
    )

    CompositionLocalProvider(
        ThemeLocals.appShapes provides DefaultAppShapes,
        ThemeLocals.appSpacing provides DefaultAppSpacing,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content,
        )
    }
}

private val DefaultAppShapes = AppShapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp),
)

private val DefaultAppSpacing = AppSpacing(
    xSmall = 4.dp,
    small = 8.dp,
    medium = 16.dp,
    large = 24.dp,
    xLarge = 32.dp,
    xxLarge = 40.dp,
)
