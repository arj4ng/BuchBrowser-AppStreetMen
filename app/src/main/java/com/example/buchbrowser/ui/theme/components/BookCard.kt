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
import com.example.buchbrowser.data.Book
import androidx.compose.foundation.combinedClickable
@Composable
fun BookCard(
    book: Book,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {}
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

            .combinedClickable(

                onClick = {

                    onClick()

                },

                onDoubleClick = {

                    selected = !selected

                    onDoubleClick()

                }

            )
    ){
        Row(
            modifier = Modifier.height(230.dp)
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
                            .width(150.dp)
                            .height(230.dp),
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = book.title,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 2
        )

    }
}
