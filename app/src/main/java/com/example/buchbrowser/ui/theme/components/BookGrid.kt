package com.example.buchbrowser.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.buchbrowser.data.Book

@Composable
fun BookGrid(
    books: List<Book>,
    favorites: List<Book>,
    readingList: List<Book> = emptyList(),
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
    onBookClick: (Book) -> Unit,
    onFavoriteClick: (Book) -> Unit,
    onBookmarkClick: (Book) -> Unit = {},
) {
    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(count = 2),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
        modifier = modifier,
    ) {
        items(books) { book ->
            BookCard(
                book = book,
                isFavorite = favorites.any { it.id == book.id },
                isBookmarked = readingList.any { it.id == book.id },
                modifier = Modifier,
                onClick = {
                    onBookClick(book)
                },
                onFavoriteClick = {
                    onFavoriteClick(book)
                },
                onBookmarkClick = {
                    onBookmarkClick(book)
                }
            )
        }
    }
}
