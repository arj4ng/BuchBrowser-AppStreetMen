package com.example.buchbrowser.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.repository.BookRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: BookRepository) : ViewModel() {

    val favorites: StateFlow<List<Book>> = repository.getFavoriteBooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun isFavorite(bookId: String): Boolean {
        return favorites.value.any { favorite -> favorite.id == bookId }
    }

    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            repository.toggleFavorite(book)
        }
    }
}
