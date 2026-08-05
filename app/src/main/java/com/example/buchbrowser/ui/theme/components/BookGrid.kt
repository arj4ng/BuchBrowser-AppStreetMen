package com.example.buchbrowser.ui.theme.components

import android.app.ProgressDialog.show
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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

    favorites: List<Book>,

    modifier: Modifier = Modifier,

    onBookClick: (Book) -> Unit,

    onFavoriteClick: (Book) -> Unit

) {LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement =  Arrangement.spacedBy(24.dp),
        modifier = modifier
    ) {
        items(books) { book ->
            BookCard(
                book = book,
                isFavorite = favorites.any { it.id == book.id },
                modifier = Modifier,
                onClick = {
                    onBookClick(book)
                },
                onFavoriteClick = {
                    onFavoriteClick(book)
                }
            )
        }
    }
}

