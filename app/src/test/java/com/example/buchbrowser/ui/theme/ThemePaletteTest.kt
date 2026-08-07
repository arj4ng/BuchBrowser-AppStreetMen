package com.example.buchbrowser.ui.theme

import androidx.compose.ui.graphics.toArgb
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ThemePaletteTest {

    @Test
    fun `system mode follows light palette when dark theme false`() {
        val scheme = resolveAppColorScheme(
            appThemeMode = AppThemeMode.SYSTEM,
            isDarkTheme = false,
        )

        assertEquals(0xFFF3F5F8.toInt(), scheme.background.toArgb())
    }

    @Test
    fun `system mode follows dark palette when dark theme true`() {
        val scheme = resolveAppColorScheme(
            appThemeMode = AppThemeMode.SYSTEM,
            isDarkTheme = true,
        )

        assertEquals(0xFF101418.toInt(), scheme.background.toArgb())
    }

    @Test
    fun `curated themes resolve distinct palettes`() {
        val paper = resolveAppColorScheme(
            appThemeMode = AppThemeMode.PAPER,
            isDarkTheme = false,
        )
        val midnight = resolveAppColorScheme(
            appThemeMode = AppThemeMode.MIDNIGHT,
            isDarkTheme = false,
        )
        val sunset = resolveAppColorScheme(
            appThemeMode = AppThemeMode.SUNSET,
            isDarkTheme = false,
        )

        assertNotEquals(paper.primary, midnight.primary)
        assertNotEquals(paper.surfaceContainer, sunset.surfaceContainer)
        assertNotEquals(midnight.primary, sunset.primary)
        assertNotEquals(midnight.surface, sunset.surface)
    }
}
