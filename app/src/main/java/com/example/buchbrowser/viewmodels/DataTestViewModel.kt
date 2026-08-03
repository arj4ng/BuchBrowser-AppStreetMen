package com.example.buchbrowser.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.repository.BookRepository
import kotlinx.coroutines.launch

// app/src/main/java/com/example/buchbrowser/viewmodels/DataTestViewModel.kt
// DataTestViewModel
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
// TODO: ║DataTestViewModel.kt ║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | DATENTEST
// TODO: ┗━ Datenzugriff sichtbar machen

class DataTestViewModel(
    private val repository: BookRepository,
) : ViewModel() {

    // hält alles zusammen, was der Testsceen gerade anzeigen soll
    var uiState by mutableStateOf(DataTestUiState())
        private set

    init {
        // beim Start holen wir direkt die lokal gemerkten Bücher rein
        loadSeenBooks()
    }

    // aktualisiert nur den Suchtext
    fun updateSearchQuery(searchQuery: String) {
        uiState = uiState.copy(searchQuery = searchQuery)
    }

    // holt Suchergebnisse aus dem Repository
    fun searchBooks() {
        val query = uiState.searchQuery.trim()
        if (query.isBlank()) {
            uiState = uiState.copy(
                searchResults = emptyList(),
                statusText = "Bitte Suchbegriff eingeben.",
            )
            return
        }

        viewModelScope.launch {
            // loading hilft im Testscreen direkt zu sehen, dass gerade wirklich etwas passiert
            uiState = uiState.copy(
                isLoading = true,
                statusText = "Lade Suchergebnisse...",
            )

            // fehler landen absichtlich im Statusfeld, damit man im Testscreen sofort sieht was los ist
            runCatching {
                repository.searchBooks(query)
            }.onSuccess { books ->
                uiState = uiState.copy(
                    isLoading = false,
                    searchResults = books,
                    statusText = if (books.isEmpty()) {
                        "Keine Bücher gefunden."
                    } else {
                        "${books.size} Bücher geladen."
                    },
                )
            }.onFailure {
                uiState = uiState.copy(
                    isLoading = false,
                    statusText = "Fehler beim Laden von API Daten.",
                )
            }
        }
    }

    // merkt ein Buch lokal als gesehen
    fun markBookAsSeen(book: Book) {
        viewModelScope.launch {
            runCatching {
                repository.markBookAsSeen(book)
                repository.getSeenBooks()
            }.onSuccess { seenBooks ->
                // der Treffer oben wird direkt aktualisiert, damit Room und Suchliste gleich aussehen
                val updatedResults = uiState.searchResults.map { currentBook ->
                    if (currentBook.id == book.id) {
                        currentBook.copy(isSeen = true)
                    } else {
                        currentBook
                    }
                }

                uiState = uiState.copy(
                    seenBooks = seenBooks,
                    searchResults = updatedResults,
                    statusText = "Buch als gesehen gespeichert.",
                )
            }.onFailure {
                uiState = uiState.copy(
                    statusText = "Fehler beim Speichern in Room.",
                )
            }
        }
    }

    // holt alle lokal gespeicherten Bücher
    fun loadSeenBooks() {
        viewModelScope.launch {
            runCatching {
                repository.getSeenBooks()
            }.onSuccess { books ->
                // Room Daten landen separat im unteren Bereich der Testansicht
                uiState = uiState.copy(seenBooks = books)
            }.onFailure {
                uiState = uiState.copy(
                    statusText = "Fehler beim Laden von Room Daten.",
                )
            }
        }
    }
}

data class DataTestUiState(
    // Startwert macht den Testsceen direkt benutzbar, ohne dass erst Text eingegeben werden muss
    val searchQuery: String = "harry potter",
    val searchResults: List<Book> = emptyList(),
    val seenBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val statusText: String = "Datenansicht bereit.",
)
