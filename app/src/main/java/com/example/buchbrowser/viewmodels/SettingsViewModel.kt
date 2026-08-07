package com.example.buchbrowser.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.buchbrowser.data.ThemePreferencesRepository
import com.example.buchbrowser.ui.theme.AppThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val repository = ThemePreferencesRepository.fromContext(application)

    val appThemeMode: StateFlow<AppThemeMode> = repository.appThemeMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppThemeMode.SYSTEM,
    )

    fun setAppThemeMode(mode: AppThemeMode) {
        viewModelScope.launch {
            repository.setAppThemeMode(mode)
        }
    }
}
