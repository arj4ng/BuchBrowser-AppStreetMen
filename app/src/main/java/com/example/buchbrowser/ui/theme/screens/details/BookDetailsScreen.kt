package com.example.buchbrowser.ui.theme.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.data.getOpenSound
import com.example.buchbrowser.music.MusicManager
import com.example.buchbrowser.ui.theme.BuchBrowserTheme
import com.example.buchbrowser.viewmodels.BookDetailsUiState
import com.example.buchbrowser.viewmodels.BookDetailsViewModel
import com.example.buchbrowser.viewmodels.FavoritesViewModel
import com.example.buchbrowser.viewmodels.ReadingListViewModel

@Composable
fun BookDetailsScreen(
    viewModel: BookDetailsViewModel,
    favoritesViewModel: FavoritesViewModel,
    readingListViewModel: ReadingListViewModel,
    book: Book,
    innerPadding: PaddingValues,
    onBackClick: () -> Unit
) {
    LaunchedEffect(book.id) {
        viewModel.setBook(book)
    }

    val favorites by favoritesViewModel.favorites.collectAsState()
    val readingList by readingListViewModel.books.collectAsState()
    
    val currentBook = viewModel.uiState.book
    val isFavorite = currentBook != null && favorites.any { it.id == currentBook.id }
    val isBookmarked = currentBook != null && readingList.any { it.id == currentBook.id }
    
    val context = LocalContext.current
    LaunchedEffect(book.id) {
        book.getOpenSound()?.let { MusicManager.playSound(context, it) }
    }

    BookDetailsContent(
        uiState = viewModel.uiState,
        innerPadding = innerPadding,
        isFavorite = isFavorite,
        isBookmarked = isBookmarked,
        onBackClick = onBackClick,
        onBookmarkClick = {
            viewModel.uiState.book?.let { readingListViewModel.toggleBookmark(it) }
        },
        onFavoriteClick = {
            viewModel.uiState.book?.let { favoritesViewModel.toggleFavorite(it) }
        },
    )
}

@Composable
fun BookDetailsContent(
    uiState: BookDetailsUiState,
    innerPadding: PaddingValues,
    isFavorite: Boolean,
    isBookmarked: Boolean,
    onBackClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onFavoriteClick: () -> Unit,
) {
    val book = uiState.book ?: return
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, top = 20.dp, end = 24.dp, bottom = bottomInset + 124.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BookHeroCover(
            title = book.title,
            coverUrl = book.coverUrl,
            isFavorite = isFavorite,
            isBookmarked = isBookmarked
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = book.title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Start,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = book.authorNames.joinToString().ifBlank { "Autor unbekannt" },
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = onFavoriteClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isFavorite) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                ),
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorit",
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = if (isFavorite) "Favorisiert" else "Favorisieren")
            }

            Button(
                onClick = onBookmarkClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isBookmarked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                ),
            ) {
                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Merken",
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = if (isBookmarked) "Gemerkt" else "Merken")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Beschreibung",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )

                if (uiState.isDescriptionLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = uiState.description ?: "Keine Beschreibung verfügbar.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun BookHeroCover(
    title: String,
    coverUrl: String?,
    isFavorite: Boolean = false,
    isBookmarked: Boolean = false,
) {
    Box(
        modifier = Modifier
            .size(width = 230.dp, height = 340.dp)
            .clip(RoundedCornerShape(28.dp))
    ) {
        if (coverUrl != null) {
            AsyncImage(
                model = coverUrl,
                contentDescription = "Cover von $title",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Kein\nCover")
            }
        }

        if (isFavorite) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier.align(Alignment.TopEnd).padding(16.dp).size(32.dp)
            )
        }

        if (isBookmarked) {
            Icon(
                imageVector = Icons.Default.Bookmark,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.TopStart).padding(16.dp).size(32.dp)
            )
        }
    }
}

@Composable
private fun DetailsMetaBlock(publishYear: Int?) { /* logic removed for brevity or kept if simple */ }

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BookDetailsScreenPreview() {
    BuchBrowserTheme {
        BookDetailsContent(
            uiState = BookDetailsUiState(
                book = Book(
                    id = "/works/OL45883W",
                    title = "Der kleine Hobbit",
                    authorNames = listOf("J. R. R. Tolkien"),
                    coverUrl = "https://covers.openlibrary.org/b/id/14625727-L.jpg",
                    firstPublishYear = 1937,
                    languageCodes = listOf("ger"),
                    isSeen = false,
                ),
            ),
            innerPadding = PaddingValues(0.dp),
            isFavorite = false,
            isBookmarked = false,
            onBackClick = {},
            onBookmarkClick = {},
            onFavoriteClick = {},
        )
    }
}
