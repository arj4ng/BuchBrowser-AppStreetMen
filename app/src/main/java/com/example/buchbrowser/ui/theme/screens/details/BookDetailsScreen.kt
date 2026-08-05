package com.example.buchbrowser.ui.theme.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.ui.theme.BuchBrowserTheme
import com.example.buchbrowser.viewmodels.BookDetailsUiState
import com.example.buchbrowser.viewmodels.BookDetailsViewModel
import com.example.buchbrowser.viewmodels.FavoritesViewModel
import com.example.buchbrowser.viewmodels.ReadingListViewModel

// app/src/main/java/com/example/buchbrowser/ui/theme/screens/details/BookDetailsScreen.kt
// BookDetailsScreen
//
// Created by Arjang Khademi on 04.08.2026
/*
  ╔════════════════════════════════════════════════════════╗
  ║  █████╗ ██████╗      ██╗ ██╗  ██╗ ███╗   ██╗  ██████╗  ║
  ║ ██╔══██╗██╔══██╗     ██║ ██║  ██║ ████╗  ██║ ██╔════╝  ║
  ║ ███████║██████╔╝     ██║ ███████║ ██╔██╗ ██║ ██║  ███╗ ║
  ║ ██╔══██║██╔══██╗██   ██║ ╚════██║ ██║╚██╗██║ ██║   ██║ ║
  ║ ██║  ██║██║  ██║╚█████╔╝      ██║ ██║ ╚████║ ╚██████╔╝ ║
  ║ ╚═╝  ╚═╝╚═╝  ╚═╝ ╚════╝       ╚═╝ ╚═╝  ╚═══╝  ╚═════╝  ║
  ╚═════════════════════════════════════════ [ DEV TAG ] ══╝
*/
// TODO: ╔══ [ BuchBrowser ] ══╗
// TODO: ║BookDetailsScreen.kt ║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | DETAILS
// TODO: ┗━ Buch schön im Fokus zeigen

/**
 * Detailansicht eines Buchs inklusive Zurück-, Favoriten- und Merken-Button.
 *
 * @param viewModel          Detail-ViewModel (lädt u. a. die Beschreibung nach).
 * @param favoritesViewModel Geteiltes Favoriten-ViewModel (dieselbe Instanz wie im Favoriten-Screen!).
 * @param book               Das anzuzeigende Buch (kommt aus der Navigation).
 * @param innerPadding       Padding vom umgebenden Scaffold.
 * @param onBackClick        Callback für den Zurück-Button.
 */
@Composable
fun BookDetailsScreen(
    viewModel: BookDetailsViewModel,
    favoritesViewModel: FavoritesViewModel,
    readingListViewModel: ReadingListViewModel,
    book: Book,
    innerPadding: PaddingValues,
    onBackClick: () -> Unit
) {
    // beim Öffnen landet erst das Buch im State, dann wird die Beschreibung gezogen
    LaunchedEffect(book.id) {
        viewModel.setBook(book)
    }

    // Favoriten beobachten -> Herz-Zustand aktualisiert sich sofort beim Tippen
    val favorites by favoritesViewModel.favorites.collectAsState()
    val currentBook = viewModel.uiState.book
    val isFavorite = currentBook != null && favorites.any { it.id == currentBook.id }

    // Merkliste beobachten -> Bookmark-Zustand aktualisiert sich sofort beim Tippen
    val readingList by readingListViewModel.books.collectAsState()
    val isBookmarked = currentBook != null && readingList.any { it.id == currentBook.id }

    BookDetailsContent(
        uiState = viewModel.uiState,
        innerPadding = innerPadding,
        isFavorite = isFavorite,
        isBookmarked = isBookmarked,
        onBackClick = onBackClick,
        // Merken-Klick: aktuelles Buch in der Merkliste toggeln
        onBookmarkClick = {
            viewModel.uiState.book?.let { readingListViewModel.toggleBookmark(it) }
        },
        // Herz-Klick: aktuelles Buch in die Favoriten toggeln
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BookHeroCover(
            title = book.title,
            coverUrl = book.coverUrl,
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = book.title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
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
                    containerColor = if (isFavorite) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = if (isFavorite) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
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
                    containerColor = if (isBookmarked) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = if (isBookmarked) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
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

        Spacer(modifier = Modifier.height(20.dp))

        DetailsMetaBlock(
            publishYear = book.firstPublishYear,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Beschreibung",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )

                when {
                    uiState.isDescriptionLoading -> {
                        CircularProgressIndicator()
                        Text(
                            text = "Beschreibung lädt gerade nach.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                    uiState.descriptionError != null -> {
                        Text(
                            text = uiState.descriptionError,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    !uiState.description.isNullOrBlank() -> {
                        Text(
                            text = uiState.description,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight,
                        )
                    }

                    else -> {
                        Text(
                            text = "Zu diesem Buch gibt es gerade noch keine Beschreibung.",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "OpenLibrary ID: ${book.id}",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
        )
    }
}

@Composable
private fun BookHeroCover(
    title: String,
    coverUrl: String?,
) {
    if (coverUrl != null) {
        AsyncImage(
            model = coverUrl,
            contentDescription = "Cover von $title",
            modifier = Modifier
                .size(width = 230.dp, height = 340.dp)
                .clip(RoundedCornerShape(28.dp)),
            contentScale = ContentScale.Crop,
        )
    } else {
        Box(
            modifier = Modifier
                .size(width = 230.dp, height = 340.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Kein\nCover",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun DetailsMetaBlock(
    publishYear: Int?,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "Erscheinungsjahr: ${publishYear ?: "Unbekannt"}",
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

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
                description = "Bilbo stolpert in ein Abenteuer, das erst klein anfängt und dann immer größer wird. Genau daraus zieht der Screen seinen ruhigen, buchigen Mittelpunkt.",
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
