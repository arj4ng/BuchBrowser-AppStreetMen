package com.example.buchbrowser.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.repository.BookRepository
import com.example.buchbrowser.repository.MockBookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: BookRepository = MockBookRepository()

) : ViewModel() {

    private val _books = MutableStateFlow<List<Book>>(emptyList())
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    init {
        loadInitialBooks()
    }

    private fun loadInitialBooks(){
        viewModelScope.launch {
            val result = repository.searchBooks(query = "a")
            _books.value = result
        }
    }
}