package com.example.buchbrowser.ui.theme.screens.readingList

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.ui.theme.components.BookGrid
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment

@Composable
fun ReadingListContent(
    books: List<Book>,
    isLoading: Boolean,
    onBookClick: (Book) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (books.isEmpty()) {
            EmptyReadingList()
        } else {
            BookGrid(
                books = books,
                onBookClick = onBookClick
            )
        }
    }
}