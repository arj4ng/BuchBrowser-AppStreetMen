package com.example.buchbrowser.viewmodels

import androidx.lifecycle.ViewModel
import com.example.buchbrowser.data.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// app/src/main/java/com/example/buchbrowser/viewmodels/FavoritesViewModel.kt
// FavoritesViewModel – zentrale, geteilte Quelle für Favoriten

/**
 * Zentrales ViewModel für die Favoriten der App.
 *
 * WICHTIG: Es darf nur EINE Instanz geben, die sich Detail-Screen und
 * Favoriten-Screen teilen. Nur so sieht der Favoriten-Screen sofort, wenn im
 * Detail-Screen das Herz getippt wurde. Doppelter Favoriten-State an zwei
 * Stellen führt garantiert zu Bugs.
 *
 * Der Zustand liegt aktuell nur im Speicher (In-Memory) und überlebt keinen
 * App-Neustart. Für dauerhafte Speicherung müsste später Room ergänzt werden.
 */
class FavoritesViewModel : ViewModel() {

    // privat veränderbar, öffentlich nur lesbar.
    // Wir speichern die kompletten Book-Objekte, damit der Favoriten-Screen
    // Titel/Cover/Autor direkt anzeigen kann, ohne neu nachzuladen.
    private val _favorites = MutableStateFlow<List<Book>>(emptyList())
    val favorites: StateFlow<List<Book>> = _favorites.asStateFlow()

    /**
     * Prüft, ob ein Buch aktuell in den Favoriten liegt.
     * @param bookId Die OpenLibrary-ID des Buchs.
     */
    fun isFavorite(bookId: String): Boolean {
        return _favorites.value.any { favorite -> favorite.id == bookId }
    }

    /**
     * Schaltet den Favoriten-Status um: liegt das Buch schon drin, fliegt es
     * raus – sonst kommt es rein. Genau das steckt hinter dem Herz-Button.
     * @param book Das betroffene Buch.
     */
    fun toggleFavorite(book: Book) {
        _favorites.update { current ->
            if (current.any { it.id == book.id }) {
                // schon Favorit -> entfernen
                current.filterNot { it.id == book.id }
            } else {
                // noch kein Favorit -> hinten anhängen
                current + book
            }
        }
    }
}