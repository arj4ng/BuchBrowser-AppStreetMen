package com.example.buchbrowser.ui.theme.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction


/**
 * Wiederverwendbare Searchbar für die Buchsuche.
 *
 * @param query        Aktueller Text im Eingabefeld.
 * @param onQueryChange Wird bei jedem Tastendruck aufgerufen (neuer Text).
 * @param onSearch     Wird ausgelöst, wenn die Suche gestartet wird (Such-Taste der Tastatur).
 * @param modifier     Optionaler Modifier vom aufrufenden Screen.
 * @param placeholder  Hinweistext, solange das Feld leer ist.
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Titel oder Autor…",
) {
    // zum Schließen der Tastatur nach dem Absenden
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        singleLine = true,
        // Lupe links als optisches Signal
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        // X rechts nur zeigen, wenn Text drin steht
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    // leert das Feld über den ganz normalen Change-Callback
                    onClick = { onQueryChange("") },
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Eingabe löschen")
                }
            }
        },
        // Tastatur zeigt eine "Suchen"-Taste statt Enter
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()                // Suche starten
                focusManager.clearFocus() // Tastatur schließen
            },
        ),
    )
}