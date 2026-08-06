package com.example.buchbrowser.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class AppShapes(
    val small: RoundedCornerShape,
    val medium: RoundedCornerShape,
    val large: RoundedCornerShape,
    val extraLarge: RoundedCornerShape,
)

@Immutable
data class AppSpacing(
    val xSmall: androidx.compose.ui.unit.Dp,
    val small: androidx.compose.ui.unit.Dp,
    val medium: androidx.compose.ui.unit.Dp,
    val large: androidx.compose.ui.unit.Dp,
    val xLarge: androidx.compose.ui.unit.Dp,
    val xxLarge: androidx.compose.ui.unit.Dp,
)

private val LocalAppShapes = staticCompositionLocalOf {
    AppShapes(
        small = RoundedCornerShape(12.dp),
        medium = RoundedCornerShape(20.dp),
        large = RoundedCornerShape(28.dp),
        extraLarge = RoundedCornerShape(36.dp),
    )
}

private val LocalAppSpacing = staticCompositionLocalOf {
    AppSpacing(
        xSmall = 4.dp,
        small = 8.dp,
        medium = 16.dp,
        large = 24.dp,
        xLarge = 32.dp,
        xxLarge = 40.dp,
    )
}

object AppTheme {
    val shapes: AppShapes
        @androidx.compose.runtime.Composable get() = LocalAppShapes.current

    val spacing: AppSpacing
        @androidx.compose.runtime.Composable get() = LocalAppSpacing.current
}

internal object ThemeLocals {
    val appShapes = LocalAppShapes
    val appSpacing = LocalAppSpacing
}
