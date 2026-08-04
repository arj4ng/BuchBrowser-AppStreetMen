package com.example.buchbrowser.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.repository.BookRepository
import kotlinx.coroutines.launch

// app/src/main/java/com/example/buchbrowser/viewmodels/BookDetailsViewModel.kt
// BookDetailsViewModel
//
// Created by Arjang Khademi on 04.08.2026
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
// TODO: ║BookDetailsViewModel ║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | DETAILS
// TODO: ┗━ Detaildaten nachladen

class BookDetailsViewModel(
    private val repository: BookRepository,
) : ViewModel() {

    // hält den kompletten Stand vom Detailscreen
    var uiState by mutableStateOf(BookDetailsUiState())
        private set

    // setzt die Basisdaten sofort, dann kommt Beschreibung extra nach
    fun setBook(book: Book) {
        val sameBookAlreadyLoaded = uiState.book?.id == book.id
        uiState = uiState.copy(
            book = book,
            isDescriptionLoading = !sameBookAlreadyLoaded,
            description = if (sameBookAlreadyLoaded) uiState.description else null,
            descriptionError = null,
        )

        if (!sameBookAlreadyLoaded) {
            loadDescription(book.id)
        }
    }

    // lädt die Beschreibung getrennt, damit das Layout sofort da sein kann
    private fun loadDescription(bookId: String) {
        viewModelScope.launch {
            runCatching {
                repository.getBookDescription(bookId)
            }.onSuccess { description ->
                uiState = uiState.copy(
                    isDescriptionLoading = false,
                    description = description,
                    descriptionError = null,
                )
            }.onFailure {
                uiState = uiState.copy(
                    isDescriptionLoading = false,
                    description = null,
                    descriptionError = "Beschreibung konnte gerade nicht geladen werden.",
                )
            }
        }
    }
}

data class BookDetailsUiState(
    val book: Book? = null,
    val isDescriptionLoading: Boolean = false,
    val description: String? = null,
    val descriptionError: String? = null,
)
