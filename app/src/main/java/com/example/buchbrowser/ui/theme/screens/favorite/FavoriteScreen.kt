package com.example.buchbrowser.ui.theme.screens.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.viewmodels.FavoritesViewModel

// app/src/main/java/com/example/buchbrowser/ui/theme/screens/favorite/FavoriteScreen.kt
// FavoriteScreen – zeigt alle als Favorit markierten Bücher

/**
 * Listet alle Favoriten aus dem geteilten [FavoritesViewModel] auf.
 *
 * Der Screen beobachtet den Favoriten-Flow und rendert entweder einen
 * freundlichen Leer-Hinweis oder die Trefferliste.
 *
 * @param favoritesViewModel Das geteilte Favoriten-ViewModel (dieselbe Instanz
 *                           wie im Detail-Screen!).
 * @param onBookClick        Callback beim Antippen eines Buchs (Navigation zum Detail).
 */
@Composable
fun FavoriteScreen(
    favoritesViewModel: FavoritesViewModel,
    onBookClick: (Book) -> Unit,
) {
    // StateFlow beobachten -> bei jeder Favoriten-Änderung neu rendern
    val favorites by favoritesViewModel.favorites.collectAsState()

    if (favorites.isEmpty()) {
        // noch keine Favoriten -> Hinweis statt leerer Fläche
        FavoritesEmptyState()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // id als key: stabile Wiederverwendung beim Entfernen
            items(favorites, key = { book -> book.id }) { book ->
                FavoriteBookItem(
                    book = book,
                    onClick = { onBookClick(book) },
                    // Herz erneut tippen entfernt das Buch wieder
                    onRemoveClick = { favoritesViewModel.toggleFavorite(book) },
                )
            }
        }
    }
}

/**
 * Freundlicher Platzhalter, solange noch nichts favorisiert wurde.
 */
@Composable
private fun FavoritesEmptyState() {
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
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Tippe auf das Herz in der Detailansicht, um ein Buch hier zu speichern.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Eine Favoriten-Zeile: Cover links, Infos in der Mitte, Herz-Button rechts zum Entfernen.
 *
 * @param book         Das anzuzeigende Buch.
 * @param onClick      Antippen der Karte (Navigation zum Detail).
 * @param onRemoveClick Antippen des Herzens (aus Favoriten entfernen).
 */
@Composable
private fun FavoriteBookItem(
    book: Book,
    onClick: () -> Unit,
    onRemoveClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, // ganze Karte klickbar
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // --- Cover-Box (fester Platz, auch ohne Bild) ---
            Box(
                modifier = Modifier
                    .size(width = 56.dp, height = 80.dp)
                    .clip(RoundedCornerShape(6.dp))
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
                    // Platzhalter: erster Buchstabe des Titels
                    Text(
                        text = book.title.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // --- Textblock (nimmt den mittleren Platz ein) ---
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
                // Jahr nur zeigen, wenn vorhanden
                book.firstPublishYear?.let { year ->
                    Text(
                        text = "Erschienen: $year",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            // --- Entfernen-Button (gefülltes Herz = ist Favorit) ---
            IconButton(onClick = onRemoveClick) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Aus Favoriten entfernen",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}