package com.example.buchbrowser.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.buchbrowser.music.MusicTheme

class MusicViewModel : ViewModel() {

    var selectedTheme by mutableStateOf(MusicTheme.NONE)
        private set

    fun selectTheme(theme: MusicTheme) {
        selectedTheme = theme
    }
}