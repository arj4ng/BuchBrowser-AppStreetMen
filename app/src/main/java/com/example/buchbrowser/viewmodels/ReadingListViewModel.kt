package com.example.buchbrowser.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReadingListViewModel(private val repository: BookRepository) : ViewModel() {

    val books: StateFlow<List<Book>> = repository.getReadingListBooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadBooks() {}

    fun isBookmarked(bookId: String): Boolean {
        return books.value.any { book -> book.id == bookId }
    }

    fun toggleBookmark(book: Book) {
        viewModelScope.launch {
            repository.toggleReadingList(book)
        }
    }
}
