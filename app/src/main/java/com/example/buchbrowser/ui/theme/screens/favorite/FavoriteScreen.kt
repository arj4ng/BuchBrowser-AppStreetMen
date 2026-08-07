package com.example.buchbrowser.ui.theme.screens.favorite

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.viewmodels.FavoritesViewModel
import com.example.buchbrowser.viewmodels.ReadingListViewModel

@Composable
fun FavoriteScreen(
    favoritesViewModel: FavoritesViewModel,
    readingListViewModel: ReadingListViewModel,
    onBookClick: (Book) -> Unit,
) {
    val favorites by favoritesViewModel.favorites.collectAsState()
    val readingList by readingListViewModel.books.collectAsState()

    if (favorites.isEmpty()) {
        FavoritesEmptyState()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            items(favorites, key = { book -> book.id }) { book ->
                FavoriteBookItem(
                    book = book,
                    isBookmarked = readingList.any { it.id == book.id },
                    onClick = { onBookClick(book) },
                    onToggleFavorite = { favoritesViewModel.toggleFavorite(book) },
                    onToggleBookmark = { readingListViewModel.toggleBookmark(book) }
                )
            }
        }
    }
}

@Composable
private fun FavoritesEmptyState() {
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 20.dp,
                top = 20.dp,
                end = 20.dp,
                bottom = bottomInset + 108.dp,
            ),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Noch keine Favoriten",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Tippe auf Herz in Detailansicht. Bücher sammeln sich hier.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun FavoriteBookItem(
    book: Book,
    isBookmarked: Boolean,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleBookmark: () -> Unit,
) {
    val bookmarkScale = animateFloatAsState(targetValue = if (isBookmarked) 1f else 0f, label = "bookmarkScale")
    val bookmarkAlpha = animateFloatAsState(targetValue = if (isBookmarked) 1f else 0f, label = "bookmarkAlpha")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onToggleFavorite,
                onLongClick = onToggleBookmark
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(width = 60.dp, height = 90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                if (book.coverUrl != null) {
                    AsyncImage(
                        model = book.coverUrl,
                        contentDescription = "Cover von ${book.title}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Text(
                        text = book.title.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .size(16.dp)
                        .scale(bookmarkScale.value)
                        .graphicsLayer { alpha = bookmarkAlpha.value }
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = book.authorNames.joinToString().ifBlank { "Autor unbekannt" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                
                if (isBookmarked) {
                    Text(
                        text = "Auch in Leseliste",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Aus Favoriten entfernen",
                    tint = Color.Red,
                )
            }
        }
    }
}
