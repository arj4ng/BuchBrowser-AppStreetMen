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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.ui.theme.BuchBrowserTheme
import com.example.buchbrowser.viewmodels.BookDetailsUiState
import com.example.buchbrowser.viewmodels.BookDetailsViewModel

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

@Composable
fun BookDetailsScreen(
    viewModel: BookDetailsViewModel,
    book: Book,
    innerPadding: PaddingValues,
    onBackClick: () -> Unit
) {
    // beim Öffnen landet erst das Buch im State, dann wird die Beschreibung gezogen
    LaunchedEffect(book.id) {
        viewModel.setBook(book)
    }

    BookDetailsContent(
        uiState = viewModel.uiState,
        innerPadding = innerPadding,
        onBackClick = onBackClick
    )
}

@Composable
fun BookDetailsContent(
    uiState: BookDetailsUiState,
    innerPadding: PaddingValues,
    onBackClick: () -> Unit
) {
    val book = uiState.book ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Zurück"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        BookHeroCover(
            title = book.title,
            coverUrl = book.coverUrl,
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = book.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = book.authorNames.joinToString().ifBlank { "Autor unbekannt" },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(20.dp))

        DetailsMetaBlock(
            bookId = book.id,
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
    bookId: String,
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
        Text(
            text = "OpenLibrary ID: $bookId",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
            onBackClick = TODO(),
        )
    }
}
