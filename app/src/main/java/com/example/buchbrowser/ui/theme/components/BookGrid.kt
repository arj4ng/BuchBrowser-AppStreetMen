package com.example.buchbrowser.ui.theme.components

import android.app.ProgressDialog.show
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.ui.theme.BuchBrowserTheme

@Composable
fun BookGrid(
    books: List<Book>,
    modifier: Modifier = Modifier,
    onBookClick: (Book) -> Unit //vorbereitung für die Detailansicht
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement =  Arrangement.spacedBy(24.dp),
        modifier = modifier
    ) {
        items(books) { book ->
            BookCard(
                title = book.title,
                author = book.authorNames.joinToString(", "),
                coverUrl = book.coverUrl ?: "",
                modifier = Modifier
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun BookGridPreview(){
    val previewBooks = listOf(
        Book("1", "Zora Neale", listOf("Hurston"), "https://covers.openlibrary.org/b/id/8231856-M.jpg", null, listOf("en"), false),
        Book("2", "Der alte Mann", listOf("Hemingway"), "https://covers.openlibrary.org/b/id/8231856-M.jpg", null, listOf("de"), false),
        Book("3", "1984", listOf("Orwell"), "https://covers.openlibrary.org/b/id/8231856-M.jpg", null, listOf("en"), false),
        Book("4", "Moby Dick", listOf("Melville"), "https://covers.openlibrary.org/b/id/8231856-M.jpg", null, listOf("en"), false)
    )

    BuchBrowserTheme {
        BookGrid(
            books = previewBooks,
            onBookClick = {}
        )
    }
}