package com.example.buchbrowser.viewmodels

import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.example.buchbrowser.data.Book
import com.example.buchbrowser.repository.ApiBookRepository

import com.example.buchbrowser.repository.BookRepository

import com.example.buchbrowser.repository.MockBookRepository

import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch

class HomeViewModel(

    private val repository: BookRepository = ApiBookRepository()

) : ViewModel() {
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    private var allBooks: List<Book> = emptyList()

    private val _books = MutableStateFlow<List<Book>>(emptyList())

    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {

        loadInitialBooks()

    }
    fun loadOfflineBooks() {
        viewModelScope.launch {
            val result = MockBookRepository().searchBooks("")
            allBooks = result
    }
    //Aktualisieren des Suchbegriffs
    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
    //Die eigentliche Suche auslösen
    fun onSearch() {
        viewModelScope.launch {
            val queryToSearch = if (_searchQuery.value.isBlank()) "a" else _searchQuery.value
            val result = repository.searchBooks(query = queryToSearch)
            _books.value = result
            _errorMessage.value = null
        }

    }
    private fun loadInitialBooks() {
        viewModelScope.launch {
            try {
                val result = repository.searchBooks("bestseller")
                allBooks = result
                _books.value = result
            } catch (e: Exception) {
                e.printStackTrace()
                _books.value = emptyList()
                _errorMessage.value =
                    "Die OpenLibrary ist momentan nicht erreichbar.\nBitte versuche es später erneut."
            }
        }
    }

    fun updateSearchQuery(query: String) {

        _searchQuery.value = query

        _books.value = if (query.isBlank()) {

            allBooks

        } else {

            allBooks.filter { book ->

                book.title.contains(query, ignoreCase = true) ||
                        book.authorNames.any { author ->
                            author.contains(query, ignoreCase = true)
                        }

            }
        }
    }

    fun searchBooks() {

        val query = searchQuery.value.trim()

        if (query.length < 3) {
            _books.value = emptyList()
            return
        }

        viewModelScope.launch {
            try {
                val result = repository.searchBooks(query)

                _books.value = result
                _errorMessage.value = null

            } catch (e: Exception) {
                _books.value = emptyList()
            }
        }
    }

    private fun loadInitialBooks(){
        onQueryChange(newQuery = "a")
        onSearch()
    }
}