package com.example.buchbrowser.ui.theme.screens.readingList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.viewmodels.FavoritesViewModel
import com.example.buchbrowser.viewmodels.ReadingListViewModel

@Composable
fun ReadingListScreen(
    viewModel: ReadingListViewModel,
    favoritesViewModel: FavoritesViewModel,
    onBookClick: (Book) -> Unit
) {
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val favorites by favoritesViewModel.favorites.collectAsState()
    
    ReadingListContent(
        books = books,
        favorites = favorites,
        isLoading = isLoading,
        onBookClick = onBookClick,
        onFavoriteClick = { book ->
            favoritesViewModel.toggleFavorite(book)
        },
        onBookmarkClick = { book ->
            viewModel.toggleBookmark(book)
        }
    )
}
