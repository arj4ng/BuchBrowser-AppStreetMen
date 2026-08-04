package com.example.buchbrowser.ui.theme.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.ui.theme.components.BookGrid
import com.example.buchbrowser.viewmodels.HomeViewModel

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    onBookClick: (Book) -> Unit
) {
    val books by homeViewModel.books.collectAsState()

    BookGrid(
        books = books,
        onBookClick = onBookClick
    )
}

