package com.example.buchbrowser.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.repository.BookRepository
import kotlinx.coroutines.launch

// app/src/main/java/com/example/buchbrowser/viewmodels/ApiRoomDebugViewModel.kt
// ApiRoomDebugViewModel
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
// TODO: ║ApiRoomDebugViewModel║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | DATENTEST
// TODO: ┗━ Datenzugriff sichtbar machen

class ApiRoomDebugViewModel(
    private val repository: BookRepository,
) : ViewModel() {

    // hält alles, was der Screen gerade zeigen soll
    var uiState by mutableStateOf(ApiRoomDebugUiState())
        private set

    init {
        // beim Start holen wir direkt die lokal gemerkten Bücher
        loadSeenBooks()
    }

    // ändert nur den Suchtext
    fun updateSearchQuery(searchQuery: String) {
        uiState = uiState.copy(searchQuery = searchQuery)
    }

    // holt Suchtreffer aus dem Repo
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
            // loading zeigt im Screen direkt, dass gerade was läuft
            uiState = uiState.copy(
                isLoading = true,
                statusText = "Lade Suchergebnisse...",
            )

            // Fehler landen extra im Statusfeld, damit man sofort sieht was schiefläuft
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
                // der Treffer oben wird direkt mitgezogen, damit unten und oben gleich aussehen
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
                // Room Daten landen extra im unteren Bereich
                uiState = uiState.copy(seenBooks = books)
            }.onFailure {
                uiState = uiState.copy(
                    statusText = "Fehler beim Laden von Room Daten.",
                )
            }
        }
    }
}

data class ApiRoomDebugUiState(
    // Startwert macht den Screen direkt nutzbar
    val searchQuery: String = "harry potter",
    val searchResults: List<Book> = emptyList(),
    val seenBooks: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val statusText: String = "Datenansicht bereit.",
)
