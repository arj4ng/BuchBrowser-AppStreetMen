package com.example.buchbrowser.ui.theme.screens.readingList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.ui.theme.components.BookGrid

@Composable
fun ReadingListContent(
    books: List<Book>,
    favorites: List<Book>,
    isLoading: Boolean,
    onBookClick: (Book) -> Unit,
    onFavoriteClick: (Book) -> Unit,
) {
    val gridState = rememberLazyGridState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Leseliste wird geladen…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        } else if (books.isEmpty()) {
            EmptyReadingList()
        } else {
            BookGrid(
                books = books,
                favorites = favorites,
                gridState = gridState,
                onBookClick = onBookClick,
                onFavoriteClick = onFavoriteClick,
            )
        }
    }
}
