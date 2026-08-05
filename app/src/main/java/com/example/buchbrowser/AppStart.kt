package com.example.buchbrowser

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.buchbrowser.api.ApiClient
import com.example.buchbrowser.data.Book
import com.example.buchbrowser.data.BookDatabase
import com.example.buchbrowser.navigation.Route
import com.example.buchbrowser.repository.BookRepositoryImpl
import com.example.buchbrowser.ui.theme.components.BookCard
import com.example.buchbrowser.viewmodels.BookDetailsViewModel
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import androidx.navigation.toRoute
import com.example.buchbrowser.ui.theme.screens.details.BookDetailsScreen
import com.example.buchbrowser.ui.theme.screens.home.HomeScreen
import com.example.buchbrowser.ui.theme.screens.favorite.FavoriteScreen
import com.example.buchbrowser.ui.theme.screens.settings.SettingsScreen
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.example.buchbrowser.ui.theme.screens.readingList.ReadingListScreen
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.buchbrowser.viewmodels.HomeViewModel
import com.example.buchbrowser.ui.theme.components.SearchBar
import com.example.buchbrowser.viewmodels.ReadingListViewModel
import com.example.buchbrowser.viewmodels.FavoritesViewModel

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppStart() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val homeViewModel: HomeViewModel = viewModel()
    val searchQuery by homeViewModel.searchQuery.collectAsState()
    var isSearchActive by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val repository = remember {
        BookRepositoryImpl(
            api = ApiClient.openLibraryApi,
            bookDao = BookDatabase.getInstance(context).bookDao(),
        )
    }
    val detailsViewModel = remember {
        BookDetailsViewModel(repository = repository)
    }
    val readingListViewModel = remember {
        ReadingListViewModel()
    }
// EINE geteilte Instanz für Detail- UND Favoriten-Screen
    val favoritesViewModel = remember {
        FavoritesViewModel()
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

    //dynamischen Title ermittlen
    val title = when {
        currentDestination?.hasRoute<Route.Home>() == true -> "BuchBrowser"
        currentDestination?.hasRoute<Route.Favorite>() == true -> "Favoriten"
        currentDestination?.hasRoute<Route.Settings>() == true -> "Einstellungen"
        currentDestination?.hasRoute<Route.Details>() == true -> "Details"
        currentDestination?.hasRoute<Route.ReadingList>() == true -> "Leseliste"
        else -> "BuchBrowser"
    }

    //Prüfen ob wir einen Zurück-Button brauchen
    val canNavigateBack = navController.previousBackStackEntry != null &&
            currentDestination?.hasRoute<Route.Home>() == false &&
            currentDestination?.hasRoute<Route.Favorite>() == false &&
            currentDestination?.hasRoute<Route.Settings>() == false

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    if (isSearchActive && currentDestination?.hasRoute<Route.Home>() == true) {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { homeViewModel.onQueryChange(newQuery = it) },
                            onSearch = { homeViewModel.onSearch() },
                            placeholder = "Suchen..."
                        )
                    } else {
                        Text(title)
                    }
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    if (isSearchActive && currentDestination?.hasRoute<Route.Home>() == true) {
                        IconButton(onClick = {
                            isSearchActive = false
                            homeViewModel.onQueryChange(newQuery = "")
                            homeViewModel.onSearch()
                        }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Suche beenden")
                        }
                    } else if (canNavigateBack) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Zurück")
                        }
                    }
                },
                actions = {
                    if (!isSearchActive && currentDestination?.hasRoute<Route.Home>() == true) {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Suche öffnen")
                        }
                    }
                    IconButton(onClick = { navController.navigate(Route.ReadingList) }) {
                        Icon(imageVector = Icons.Default.Bookmark, contentDescription = "Leseliste")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Home>() } == true,
                    onClick = {
                        isSearchActive = false
                        navigateToTab(
                            navController,
                            targetRoute = Route.Home
                        )
                    },
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Favorite>() } == true,
                    onClick = { navigateToTab(navController, targetRoute = Route.Favorite) },
                    icon = { Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favoriten") },
                    label = { Text("Favoriten") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Settings>() } == true,
                    onClick = { navigateToTab(navController, targetRoute = Route.Settings) },
                    icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Home,
            modifier = Modifier.padding(innerPadding)
        ){
            composable<Route.Home> {
                HomeScreen(
                    homeViewModel = homeViewModel,
                    onBookClick = { book ->
                        navController.navigate(
                            Route.Details(
                                id = book.id,
                                title = book.title,
                                author = book.authorNames.firstOrNull() ?: "",
                                coverUrl = book.coverUrl,
                                firstPublishYear = book.firstPublishYear
                            )

                        )

                    }
                )
            }

            composable<Route.Favorite> {
                FavoriteScreen(
                    favoritesViewModel = favoritesViewModel,
                    onBookClick = { book ->
                        navController.navigate(
                            Route.Details(
                                id = book.id,
                                title = book.title,
                                author = book.authorNames.firstOrNull() ?: "",
                                coverUrl = book.coverUrl,
                                firstPublishYear = book.firstPublishYear
                            )
                        )
                    }
                )
            }
            composable<Route.Settings> {
                SettingsScreen()
            }
            composable<Route.Details> { entry ->
                val route = entry.toRoute<Route.Details>()

                BookDetailsScreen(
                    viewModel = detailsViewModel,
                    favoritesViewModel = favoritesViewModel,
                    readingListViewModel = readingListViewModel,
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
            composable<Route.ReadingList> {
                ReadingListScreen(
                    viewModel = readingListViewModel,
                    onBookClick = { book ->
                        navController.navigate(
                            Route.Details(
                                id = book.id,
                                title = book.title,
                                author = book.authorNames.firstOrNull() ?: "",
                                coverUrl = book.coverUrl,
                                firstPublishYear = book.firstPublishYear
                            )
                        )
                    }
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
