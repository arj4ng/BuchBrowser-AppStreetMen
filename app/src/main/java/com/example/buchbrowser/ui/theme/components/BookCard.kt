package com.example.buchbrowser.ui.theme.components

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

@Composable
fun BookCard(
    title: String,
    author: String,
    coverUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {

    var selected by remember {
        mutableStateOf(false)
    }
    val rotation by animateFloatAsState(
        targetValue = if (selected) -8f else -25f,
        label = ""
    )
    val elevation by animateFloatAsState(
        targetValue = if (selected) 30f else 12f,
        label = ""
    )
    val translation by animateFloatAsState(
        targetValue = if (selected) -20f else 0f,
        label = ""
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable {
                selected = !selected
                onClick()
            }
    ) {
        Row(
            modifier = Modifier.height(250.dp)
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
                        model = coverUrl,
                        contentDescription = title,
                        modifier = Modifier
                            .width(170.dp)
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(10.dp)
                            .background(Color(0xFF5C4033))
                            .align(Alignment.CenterStart)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 2
        )

        Text(
            text = author,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
