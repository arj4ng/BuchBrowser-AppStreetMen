package com.example.buchbrowser.ui.theme.screens.readingList

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.repository.MockBookRepository
import com.example.buchbrowser.ui.theme.components.BookGrid
import com.example.buchbrowser.viewmodels.ReadingListViewModel

@Composable
fun ReadingListScreen(
    viewModel: ReadingListViewModel,
    onBookClick: (Book) -> Unit
) {
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBooks()
    }
    ReadingListContent(
        books = books,
        isLoading = isLoading,
        onBookClick = onBookClick
    )
}

@Preview(showSystemUi = true)
@Composable
private fun ReadingListScreenPreview() {
    val testBooks = listOf(
        Book("1", "Der kleine Hobbit", listOf("J.R.R. Tolkien"), "https://covers.openlibrary.org/b/id/14625727-M.jpg", 1937, listOf("de"), true),
        Book("2", "Momo", listOf("Michael Ende"), "https://covers.openlibrary.org/b/id/10749658-M.jpg", 1973, listOf("de"), true)
    )
    com.example.buchbrowser.ui.theme.BuchBrowserTheme { // Dein Theme für richtige Farben
        ReadingListContent(
            books = testBooks,
            isLoading = false,
            onBookClick = {}
        )
    }
}