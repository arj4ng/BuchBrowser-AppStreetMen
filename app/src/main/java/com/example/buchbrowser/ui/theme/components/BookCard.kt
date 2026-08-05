package com.example.buchbrowser.ui.theme.components

import android.R.attr.padding
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.runtime.Composable
import com.example.buchbrowser.data.Book
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import com.example.buchbrowser.ui.theme.BuchBrowserTheme
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.scale
import androidx.compose.animation.AnimatedVisibility
@Composable
fun BookCard(
    book: Book,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onFavoriteClick: () -> Unit = {}
) {
    val heartScale by animateFloatAsState(

        targetValue = if (isFavorite) 1f else 0f,

        label = ""

    )

    val heartAlpha by animateFloatAsState(

        targetValue = if (isFavorite) 1f else 0f,

        label = ""

    )
    val rotation by animateFloatAsState(

        targetValue = if (isFavorite) -8f else -25f,

        label = ""

    )
    val elevation by animateFloatAsState(

        targetValue = if (isFavorite) 30f else 12f,

        label = ""

    )

    val translation by animateFloatAsState(

        targetValue = if (isFavorite) -20f else 0f,

        label = ""

    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onFavoriteClick
            )
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.height(180.dp)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(elevation.dp),
                modifier = Modifier.graphicsLayer {
                    rotationY = rotation
                    translationX = translation
                }
            ) {
                Box {
                    AsyncImage(
                        model = book.coverUrl,
                        contentDescription = book.title,
                        modifier = Modifier
                            .width(120.dp)
                            .height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    Icon(

                        imageVector = Icons.Default.Favorite,

                        contentDescription = null,

                        tint = Color.Red,

                        modifier = Modifier

                            .align(Alignment.TopEnd)

                            .padding(8.dp)

                            .scale(heartScale)

                            .graphicsLayer {

                                alpha = heartAlpha

                            }

                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(8.dp)
                            .background(Color(0xFF5C4033))
                            .align(Alignment.CenterStart)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

    }
}

