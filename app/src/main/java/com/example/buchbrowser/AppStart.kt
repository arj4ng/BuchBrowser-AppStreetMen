package com.example.buchbrowser

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.buchbrowser.api.ApiClient
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.data.BookDatabase
import com.example.buchbrowser.navigation.Route
import com.example.buchbrowser.repository.BookRepositoryImpl
import com.example.buchbrowser.ui.theme.components.BookCard
import com.example.buchbrowser.ui.theme.screens.details.BookDetailsScreen
import com.example.buchbrowser.viewmodels.BookDetailsViewModel

// app/src/main/java/com/example/buchbrowser/AppStart.kt
// AppStart
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
// TODO: ║     AppStart.kt     ║
// TODO: ╚═════════════════════╝

// TODO: ┏━ PROJEKTWOCHE | DETAILS
// TODO: ┗━ Details Navigation einhängen

@Composable
fun AppStart() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val repository = remember {
        BookRepositoryImpl(
            api = ApiClient.openLibraryApi,
            bookDao = BookDatabase.getInstance(context).bookDao(),
        )
    }
    val detailsViewModel = remember {
        BookDetailsViewModel(repository = repository)
    }
    val homePreviewBook = remember {
        Book(
            id = "/works/OL21177W",
            title = "Their Eyes Were Watching God",
            authorNames = listOf("Zora Neale Hurston"),
            coverUrl = "https://covers.openlibrary.org/b/id/8231856-M.jpg",
            firstPublishYear = 1937,
            languageCodes = listOf("eng"),
            isSeen = false,
        )
    }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("AppNavigation", "Aktueller Screen geändert zu: ${destination.route}")
        }
    }

    Scaffold(
        bottomBar = {
            val hideBottomBar = currentDestination?.hierarchy?.any { destination ->
                destination.hasRoute<Route.Details>()
            } == true

            if (!hideBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Home>() } == true,
                        onClick = { navigateToTab(navController, targetRoute = Route.Home) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                            )
                        },
                        label = { Text("Home") },
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Search>() } == true,
                        onClick = { navigateToTab(navController, targetRoute = Route.Search) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Suche",
                            )
                        },
                        label = { Text("Suche") },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<Route.Home> {
                HomeRouteContent(
                    book = homePreviewBook,
                    innerPadding = innerPadding,
                    onBookClick = { selectedBook ->
                        navController.navigate(
                            Route.Details(
                                id = selectedBook.id,
                                title = selectedBook.title,
                                author = selectedBook.authorNames.joinToString().ifBlank { "Autor unbekannt" },
                                coverUrl = selectedBook.coverUrl,
                                firstPublishYear = selectedBook.firstPublishYear,
                            ),
                        )
                    },
                )
            }

            composable<Route.Search> {
                Text("Search Screen (Work in Progress)")
            }

            composable<Route.Details> { entry ->
                val route = entry.toRoute<Route.Details>()

                BookDetailsScreen(
                    viewModel = detailsViewModel,
                    book = Book(
                        id = route.id,
                        title = route.title,
                        authorNames = listOf(route.author),
                        coverUrl = route.coverUrl,
                        firstPublishYear = route.firstPublishYear,
                        languageCodes = emptyList(),
                        isSeen = false,
                    ),
                    innerPadding = PaddingValues(),
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }
}

@Composable
private fun HomeRouteContent(
    book: Book,
    innerPadding: PaddingValues,
    onBookClick: (Book) -> Unit,
) {
    BookCard(
        book = book,
        modifier = Modifier.padding(innerPadding),
        onClick = {
            onBookClick(book)
        },
        onDoubleClick = {
            //später dafür da um das Buch zu drehen
        }
    )
}

private fun navigateToTab(
    navController: NavController,
    targetRoute: Route,
) {
    navController.navigate(targetRoute) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
