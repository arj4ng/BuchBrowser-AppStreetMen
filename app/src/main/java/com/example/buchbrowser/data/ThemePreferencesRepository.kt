package com.example.buchbrowser.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.buchbrowser.ui.theme.AppThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themePreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "theme_preferences",
)

class ThemePreferencesRepository(
    private val dataStore: DataStore<Preferences>,
) {
    val appThemeMode: Flow<AppThemeMode> = dataStore.data.map { preferences ->
        AppThemeMode.fromStorageValue(preferences[AppThemePreferenceKeys.APP_THEME_MODE])
    }

    suspend fun setAppThemeMode(mode: AppThemeMode) {
        dataStore.edit { preferences ->
            preferences[AppThemePreferenceKeys.APP_THEME_MODE] = mode.storageValue
        }
    }

    companion object {
        fun fromContext(context: Context): ThemePreferencesRepository {
            return ThemePreferencesRepository(context.themePreferencesDataStore)
        }
    }
}

private object AppThemePreferenceKeys {
    val APP_THEME_MODE = stringPreferencesKey("app_theme_mode")
}
