package com.example.buchbrowser.ui.theme.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.ui.theme.components.BookGrid
import com.example.buchbrowser.ui.theme.components.SearchBar
import com.example.buchbrowser.viewmodels.FavoritesViewModel
import com.example.buchbrowser.viewmodels.HomeViewModel
import com.example.buchbrowser.viewmodels.ReadingListViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    favoritesViewModel: FavoritesViewModel,
    readingListViewModel: ReadingListViewModel,
    onBookClick: (Book) -> Unit,
) {
    val favorites by favoritesViewModel.favorites.collectAsState()
    val readingList by readingListViewModel.books.collectAsState()
    val books by homeViewModel.books.collectAsState()
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val errorMessage by homeViewModel.errorMessage.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val isRandomMode by homeViewModel.isRandomMode.collectAsState()
    val gridState = rememberLazyGridState()
    var isSearchVisible by remember { mutableStateOf(true) }
    var previousScrollPosition by remember { mutableIntStateOf(0) }

    LaunchedEffect(gridState) {
        snapshotFlow {
            (gridState.firstVisibleItemIndex * 10_000) + gridState.firstVisibleItemScrollOffset
        }.collect { currentScrollPosition ->
            isSearchVisible = currentScrollPosition <= previousScrollPosition
            previousScrollPosition = currentScrollPosition
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            AnimatedVisibility(
                visible = isSearchVisible,
                enter = slideInVertically(initialOffsetY = { -it / 2 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it / 2 }) + fadeOut(),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { homeViewModel.updateSearchQuery(it) },
                        onSearch = { homeViewModel.searchBooks() },
                        modifier = Modifier.weight(1f),
                    )

                    FilledTonalIconButton(
                        onClick = { homeViewModel.toggleRandomBooks() },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = if (isRandomMode) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.secondaryContainer
                            },
                            contentColor = if (isRandomMode) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSecondaryContainer
                            },
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = if (isRandomMode) {
                                "Zufallsmodus deaktivieren"
                            } else {
                                "Zufallsbuch anzeigen"
                            },
                        )
                    }
                }
            }

            if (errorMessage != null) {
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Button(onClick = { homeViewModel.loadOfflineBooks() }) {
                            Text(
                                text = "Offline-Bibliothek anzeigen",
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                }
            }

            when {
                books.isNotEmpty() -> {
                    BookGrid(
                        books = books,
                        favorites = favorites,
                        readingList = readingList,
                        gridState = gridState,
                        modifier = Modifier.weight(1f),
                        onBookClick = onBookClick,
                        onFavoriteClick = { book ->
                            favoritesViewModel.toggleFavorite(book)
                        },
                        onBookmarkClick = { book ->
                            readingListViewModel.toggleBookmark(book)
                        }
                    )
                }

                !isLoading && errorMessage == null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            )
                            Text(
                                text = "Search library",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Text(
                                text = "Type title, author, or use random mode.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }

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
                            text = "Loading shelves…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
