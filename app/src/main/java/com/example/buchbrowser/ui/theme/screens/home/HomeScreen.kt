package com.example.buchbrowser.ui.theme.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.ui.theme.components.BookGrid
import com.example.buchbrowser.ui.theme.components.SearchBar
import com.example.buchbrowser.viewmodels.HomeViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(),
    onBookClick: (Book) -> Unit
) {

    val books by homeViewModel.books.collectAsState()
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val errorMessage by homeViewModel.errorMessage.collectAsState()

    Column {

        SearchBar(
            query = searchQuery,
            onQueryChange = {
                homeViewModel.updateSearchQuery(it)
            },
            onSearch = {
                homeViewModel.searchBooks()
            },
            modifier = Modifier.padding(16.dp)
        )

        if (errorMessage != null) {

            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Button(
                onClick = {
                    homeViewModel.loadOfflineBooks()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Offline-Bibliothek anzeigen")
            }
        }
        if (books.isNotEmpty()) {
            BookGrid(
                books = books,
                onBookClick = onBookClick
            )
        }
    }
}

