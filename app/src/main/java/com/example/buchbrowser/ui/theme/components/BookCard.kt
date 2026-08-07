package com.example.buchbrowser.ui.theme.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.ui.theme.BookSpineBrown
import com.example.buchbrowser.ui.theme.FavoriteGlow

@Composable
fun BookCard(
    book: Book,
    isFavorite: Boolean,
    isBookmarked: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {},
    onBookmarkClick: () -> Unit = {},
) {
    val heartScale = animateFloatAsState(targetValue = if (isFavorite) 1f else 0f, label = "heartScale")
    val heartAlpha = animateFloatAsState(targetValue = if (isFavorite) 1f else 0f, label = "heartAlpha")
    val bookmarkScale = animateFloatAsState(targetValue = if (isBookmarked) 1f else 0f, label = "bookmarkScale")
    val bookmarkAlpha = animateFloatAsState(targetValue = if (isBookmarked) 1f else 0f, label = "bookmarkAlpha")
    
    val rotation = animateFloatAsState(targetValue = if (isFavorite) -4f else -10f, label = "rotation")
    val translation = animateFloatAsState(targetValue = if (isFavorite) -10f else 0f, label = "translation")

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onFavoriteClick,
                onLongClick = onBookmarkClick
            )
            .fillMaxWidth(),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(238.dp)
                .graphicsLayer {
                    rotationY = rotation.value
                    translationX = translation.value
                },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp,
                pressedElevation = 12.dp,
            ),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = book.coverUrl,
                    contentDescription = book.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.08f)),
                )

                Box(
                    modifier = Modifier
                        .width(10.dp)
                        .fillMaxSize()
                        .background(BookSpineBrown.copy(alpha = 0.85f))
                        .align(Alignment.CenterStart),
                )

                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = FavoriteGlow,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .scale(heartScale.value)
                        .graphicsLayer { alpha = heartAlpha.value },
                )

                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 12.dp, start = 14.dp)
                        .scale(bookmarkScale.value)
                        .graphicsLayer { alpha = bookmarkAlpha.value },
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = book.title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )

        val author = book.authorNames.firstOrNull().orEmpty()
        if (author.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = author,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
