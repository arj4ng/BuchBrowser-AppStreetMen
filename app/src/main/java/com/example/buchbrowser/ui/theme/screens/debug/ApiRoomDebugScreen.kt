package com.example.buchbrowser.ui.theme.screens.debug

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.ui.theme.BuchBrowserTheme
import com.example.buchbrowser.viewmodels.ApiRoomDebugUiState
import com.example.buchbrowser.viewmodels.ApiRoomDebugViewModel

// app/src/main/java/com/example/buchbrowser/ui/theme/screens/debug/ApiRoomDebugScreen.kt
// ApiRoomDebugScreen
//
// Created by Arjang Khademi on 03.08.2026
/*
  ╔════════════════════════════════════════════════════════╗
  ║  █████╗ ██████╗      ██╗ ██╗  ██╗ ███╗   ██╗  ██████╗  ║
  ║ ██╔══██╗██╔══██╗     ██║ ██║  ██║ ████╗  ██║ ██╔════╝  ║
  ║ ███████║██████╔╝     ██║ ███████║ ██╔██╗ ██║ ██║  ███╗ ║
  ║ ██╔══██║██╔══██╗██   ██║ ╚════██║ ██║╚██╗██║ ██║   ██║ ║
  ║ ██║  ██║██║  ██║╚█████╔╝      ██║ ██║ ╚████║ ╚██████╔╝ ║
  ║ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚════╝       ╚═╝ ╚═╝  ╚═══╝  ╚═════╝  ║
  ╚═════════════════════════════════════════ [ DEV TAG ] ══╝
*/

// TODO: ╔══ [ BuchBrowser ] ══╗
// TODO: ║ApiRoomDebugScreen.kt║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | DATENTEST
// TODO: ┗━ Datenstatus im Screen prüfen

@Composable
fun ApiRoomDebugScreen(
    viewModel: ApiRoomDebugViewModel,
    innerPadding: PaddingValues,
) {
    // hier geht nur der State vom ViewModel weiter in die Ansicht
    ApiRoomDebugContent(
        uiState = viewModel.uiState,
        innerPadding = innerPadding,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onSearchClick = viewModel::searchBooks,
        onLoadSeenClick = viewModel::loadSeenBooks,
        onMarkSeenClick = viewModel::markBookAsSeen,
    )
}

@Composable
fun ApiRoomDebugContent(
    uiState: ApiRoomDebugUiState,
    innerPadding: PaddingValues,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onLoadSeenClick: () -> Unit,
    onMarkSeenClick: (Book) -> Unit,
) {
    // der Screen zeigt API und Room Daten bewusst gleichzeitig
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(16.dp),
    ) {
        Text(
            text = "Daten Testansicht",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = uiState.statusText,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Suchbegriff")
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                cursorColor = MaterialTheme.colorScheme.primary,
            ),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = onSearchClick,
            ) {
                Text("API testen")
            }

            Button(
                onClick = onLoadSeenClick,
            ) {
                Text("Room laden")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
        }

        // LazyColumn macht die Liste scrollbar, sobald mehr Treffer da sind
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                DebugSectionTitle(
                    titleText = "API Suchergebnisse (${uiState.searchResults.size})",
                )
            }

            items(uiState.searchResults, key = { book -> book.id }) { book ->
                DebugBookCard(
                    book = book,
                    buttonText = if (book.isSeen) {
                        "Schon gesehen"
                    } else {
                        "Als gesehen speichern"
                    },
                    onActionClick = {
                        if (!book.isSeen) {
                            onMarkSeenClick(book)
                        }
                    },
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                DebugSectionTitle(
                    titleText = "Room Gesehene Bücher (${uiState.seenBooks.size})",
                )
            }

            items(uiState.seenBooks, key = { book -> "seen_${book.id}" }) { book ->
                DebugBookCard(
                    book = book,
                    buttonText = "Gespeichert",
                    onActionClick = {},
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ApiRoomDebugScreenPreview() {
    var previewState by remember {
        mutableStateOf(
            ApiRoomDebugUiState(
                searchQuery = "harry potter",
                searchResults = listOf(
                    Book(
                        id = "/works/OL82586W",
                        title = "Harry Potter und der Stein der Weisen",
                        authorNames = listOf("J. K. Rowling"),
                        coverUrl = "https://covers.openlibrary.org/b/id/10521270-M.jpg",
                        firstPublishYear = 1997,
                        languageCodes = listOf("ger"),
                        isSeen = false,
                    ),
                    Book(
                        id = "/works/OL45804W",
                        title = "Der Herr der Ringe",
                        authorNames = listOf("J. R. R. Tolkien"),
                        coverUrl = "https://covers.openlibrary.org/b/id/14625727-M.jpg",
                        firstPublishYear = 1954,
                        languageCodes = listOf("ger"),
                        isSeen = true,
                    ),
                ),
                seenBooks = listOf(
                    Book(
                        id = "/works/OL45804W",
                        title = "Der Herr der Ringe",
                        authorNames = listOf("J. R. R. Tolkien"),
                        coverUrl = "https://covers.openlibrary.org/b/id/14625727-M.jpg",
                        firstPublishYear = 1954,
                        languageCodes = listOf("ger"),
                        isSeen = true,
                    ),
                ),
                statusText = "2 Bücher geladen. 1 Buch in Room.",
            ),
        )
    }

    BuchBrowserTheme {
        // preview läuft mit lokalem Fake State, damit dort trotzdem was reagiert
        ApiRoomDebugContent(
            uiState = previewState,
            innerPadding = PaddingValues(0.dp),
            onSearchQueryChange = { newQuery ->
                previewState = previewState.copy(
                    searchQuery = newQuery,
                    statusText = "Preview Suchtext geändert.",
                )
            },
            onSearchClick = {
                previewState = previewState.copy(
                    statusText = "Preview API Test ausgelöst.",
                )
            },
            onLoadSeenClick = {
                previewState = previewState.copy(
                    statusText = "Preview Room Test ausgelöst.",
                )
            },
            onMarkSeenClick = { selectedBook ->
                val updatedSearchResults = previewState.searchResults.map { book ->
                    if (book.id == selectedBook.id) {
                        book.copy(isSeen = true)
                    } else {
                        book
                    }
                }
                val updatedSeenBooks = buildList {
                    addAll(previewState.seenBooks)

                    if (previewState.seenBooks.none { book ->
                            book.id == selectedBook.id
                        }
                    ) {
                        add(selectedBook.copy(isSeen = true))
                    }
                }

                previewState = previewState.copy(
                    searchResults = updatedSearchResults,
                    seenBooks = updatedSeenBooks,
                    statusText = "Preview Buch als gesehen markiert.",
                )
            },
        )
    }
}

@Composable
private fun DebugSectionTitle(titleText: String) {
    Text(
        text = titleText,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun DebugBookCard(
    book: Book,
    buttonText: String,
    onActionClick: () -> Unit,
) {
    // eine Karte zeigt Cover, Grunddaten und den Gesehen Status zusammen
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (book.coverUrl != null) {
                Box(
                    modifier = Modifier
                        .size(width = 92.dp, height = 132.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    // wenn das echte Cover lädt, liegt es direkt über dem grauen Platzhalter
                    AsyncImage(
                        model = book.coverUrl,
                        contentDescription = "Buchcover von ${book.title}",
                        modifier = Modifier.matchParentSize(),
                    )

                    Text(
                        text = "Cover",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(width = 92.dp, height = 132.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center,
                ) {
                    // ohne URL bleibt einfach der Platzhalter stehen
                    Text(
                        text = "Kein\nCover",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Autor: ${book.authorNames.joinToString().ifBlank { "Unbekannt" }}",
                    style = MaterialTheme.typography.bodyMedium,
                )

                Text(
                    text = "Jahr: ${book.firstPublishYear ?: "Unbekannt"}",
                    style = MaterialTheme.typography.bodySmall,
                )

                Text(
                    text = "ID: ${book.id}",
                    style = MaterialTheme.typography.bodySmall,
                )

                Text(
                    text = "Gesehen: ${if (book.isSeen) "Ja" else "Nein"}",
                    style = MaterialTheme.typography.bodySmall,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onActionClick,
                    enabled = !book.isSeen,
                ) {
                    Text(buttonText)
                }
            }
        }
    }
}
