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
    private companion object {
        const val INITIAL_QUERY = "bestseller"
    }

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    private var allBooks: List<Book> = emptyList()

    private val _books = MutableStateFlow<List<Book>>(emptyList())

    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRandomMode = MutableStateFlow(false)
    val isRandomMode: StateFlow<Boolean> = _isRandomMode.asStateFlow()

    init {

        loadInitialBooks()

    }
    fun loadOfflineBooks() {
        viewModelScope.launch {
            val result = MockBookRepository().searchBooks("")
            allBooks = result
        }
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
            _isLoading.value = true
            try {
                val result = repository.searchBooks(INITIAL_QUERY)
                allBooks = result
                _books.value = result
                _isRandomMode.value = false
                _errorMessage.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _books.value = emptyList()
                _errorMessage.value =
                    "Die OpenLibrary ist momentan nicht erreichbar.\nBitte versuche es später erneut."
            } finally {
                _isLoading.value = false
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
                _isRandomMode.value = false
                _errorMessage.value = null

            } catch (e: Exception) {
                _books.value = emptyList()
            }
        }
    }

    fun toggleRandomBooks() {
        if (_isRandomMode.value) {
            loadInitialBooks()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val randomBooks = buildList {
                    repeat(12) {
                        repository.getRandomBook()?.let(::add)
                    }
                }.distinctBy { book -> book.id }

                _books.value = randomBooks
                _isRandomMode.value = randomBooks.isNotEmpty()
                _errorMessage.value = if (randomBooks.isEmpty()) {
                    "Zufallsbuch gerade nicht verfügbar.\nBitte versuche es später erneut."
                } else null
            } catch (e: Exception) {
                _books.value = emptyList()
                _isRandomMode.value = false
                _errorMessage.value =
                    "Zufallsbuch gerade nicht verfügbar.\nBitte versuche es später erneut."
            } finally {
                _isLoading.value = false
            }
        }
    }

}
