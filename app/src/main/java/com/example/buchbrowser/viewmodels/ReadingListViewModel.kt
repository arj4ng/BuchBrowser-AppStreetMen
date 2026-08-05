package com.example.buchbrowser.viewmodels

import androidx.lifecycle.ViewModel
import com.example.buchbrowser.data.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// app/src/main/java/com/example/buchbrowser/viewmodels/ReadingListViewModel.kt
// ReadingListViewModel – zentrale, geteilte Quelle für die Merkliste (In-Memory)

/**
 * Zentrales ViewModel für die Merkliste ("gemerkte" Bücher).
 *
 * Baugleich zum [FavoritesViewModel]: Es darf nur EINE Instanz geben, die sich
 * Detail-Screen und Leselisten-Screen teilen. Nur so sieht die Leseliste sofort,
 * wenn im Detail-Screen der Merken-Button getippt wurde.
 *
 * Der Zustand liegt nur im Speicher (In-Memory) und überlebt keinen App-Neustart.
 * Die Namen [books], [isLoading] und [loadBooks] bleiben erhalten, damit der
 * bestehende ReadingListScreen unverändert weiterläuft.
 */
class ReadingListViewModel : ViewModel() {

    // die gemerkten Bücher – gleicher Name wie vorher, damit der Screen passt
    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    // bleibt aus Kompatibilität erhalten; In-Memory lädt nie wirklich -> immer false
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * Früher wurde hier aus Room geladen. In-Memory gibt es nichts nachzuladen,
     * daher ein bewusster No-Op – der Screen darf die Methode aber weiter aufrufen.
     */
    fun loadBooks() {
        // absichtlich leer: die Liste lebt komplett im Speicher
    }

    /**
     * Prüft, ob ein Buch aktuell auf der Merkliste liegt.
     * @param bookId Die OpenLibrary-ID des Buchs.
     */
    fun isBookmarked(bookId: String): Boolean {
        return _books.value.any { book -> book.id == bookId }
    }

    /**
     * Schaltet den Merken-Status um: liegt das Buch schon drin, fliegt es raus –
     * sonst kommt es rein. Das steckt hinter dem Merken-Button im Detail-Screen.
     * @param book Das betroffene Buch.
     */
    fun toggleBookmark(book: Book) {
        _books.update { current ->
            if (current.any { it.id == book.id }) {
                // schon gemerkt -> entfernen
                current.filterNot { it.id == book.id }
            } else {
                // noch nicht gemerkt -> hinten anhängen
                current + book
            }
        }
    }
}