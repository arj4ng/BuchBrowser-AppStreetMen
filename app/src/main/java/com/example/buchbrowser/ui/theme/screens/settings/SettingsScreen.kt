package com.example.buchbrowser.ui.theme.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buchbrowser.music.MusicTheme
import com.example.buchbrowser.ui.theme.AppThemeMode
import com.example.buchbrowser.viewmodels.MusicViewModel
import com.example.buchbrowser.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    musicViewModel: MusicViewModel,
    settingsViewModel: SettingsViewModel,
) {
    var expanded by remember { mutableStateOf(false) }
    var themeExpanded by remember { mutableStateOf(false) }
    val appThemeMode by settingsViewModel.appThemeMode.collectAsState()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            tonalElevation = 4.dp,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Atmosphere",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = "Choose app theme.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                ExposedDropdownMenuBox(
                    expanded = themeExpanded,
                    onExpandedChange = { themeExpanded = !themeExpanded },
                ) {
                    OutlinedTextField(
                        value = appThemeMode.displayName,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true,
                            )
                            .fillMaxWidth(),
                        label = { Text("App theme") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(themeExpanded)
                        },
                    )

                    DropdownMenu(
                        expanded = themeExpanded,
                        onDismissRequest = { themeExpanded = false },
                    ) {
                        AppThemeMode.entries.forEach { mode ->
                            DropdownMenuItem(
                                text = { Text(mode.displayName) },
                                onClick = {
                                    settingsViewModel.setAppThemeMode(mode)
                                    themeExpanded = false
                                },
                            )
                        }
                    }
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Hintergrundmusik",
                    style = MaterialTheme.typography.titleLarge,
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    OutlinedTextField(
                        value = musicViewModel.selectedTheme.displayName,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor(
                                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled = true,
                            )
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        MusicTheme.entries.forEach { theme ->
                            DropdownMenuItem(
                                text = { Text(theme.displayName) },
                                onClick = {
                                    musicViewModel.selectTheme(theme)
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
