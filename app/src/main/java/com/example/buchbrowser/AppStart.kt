package com.example.buchbrowser

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.buchbrowser.music.MusicManager
import com.example.buchbrowser.navigation.Route
import com.example.buchbrowser.repository.BookRepositoryImpl
import com.example.buchbrowser.ui.theme.AppTheme
import com.example.buchbrowser.ui.theme.BuchBrowserTheme
import com.example.buchbrowser.ui.theme.screens.details.BookDetailsScreen
import com.example.buchbrowser.ui.theme.screens.favorite.FavoriteScreen
import com.example.buchbrowser.ui.theme.screens.home.HomeScreen
import com.example.buchbrowser.ui.theme.screens.readingList.ReadingListScreen
import com.example.buchbrowser.ui.theme.screens.settings.SettingsScreen
import com.example.buchbrowser.viewmodels.BookDetailsViewModel
import com.example.buchbrowser.viewmodels.FavoritesViewModel
import com.example.buchbrowser.viewmodels.HomeViewModel
import com.example.buchbrowser.viewmodels.MusicViewModel
import com.example.buchbrowser.viewmodels.ReadingListViewModel
import com.example.buchbrowser.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppStart() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()
    val appThemeMode by settingsViewModel.appThemeMode.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val musicViewModel = remember { MusicViewModel() }
    val repository = remember {
        BookRepositoryImpl(
            api = ApiClient.openLibraryApi,
            bookDao = BookDatabase.getInstance(context).bookDao(),
        )
    }
    val detailsViewModel = remember { BookDetailsViewModel(repository = repository) }
    val readingListViewModel = remember { ReadingListViewModel() }
    val favoritesViewModel = remember { FavoritesViewModel() }

    LaunchedEffect(musicViewModel.selectedTheme) {
        MusicManager.playTheme(context, musicViewModel.selectedTheme)
    }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("AppNavigation", "Aktueller Screen geändert zu: ${destination.route}")
        }
    }

    val title = when {
        currentDestination?.hasRoute<Route.Home>() == true -> "BuchBrowser"
        currentDestination?.hasRoute<Route.Favorite>() == true -> "Favoriten"
        currentDestination?.hasRoute<Route.Settings>() == true -> "Einstellungen"
        currentDestination?.hasRoute<Route.Details>() == true -> "Details"
        currentDestination?.hasRoute<Route.ReadingList>() == true -> "Leseliste"
        else -> "BuchBrowser"
    }

    val canNavigateBack = navController.previousBackStackEntry != null &&
        currentDestination?.hasRoute<Route.Home>() == false &&
        currentDestination?.hasRoute<Route.Favorite>() == false &&
        currentDestination?.hasRoute<Route.Settings>() == false

    BuchBrowserTheme(appThemeMode = appThemeMode) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                containerColor = Color.Transparent,
                topBar = {
                    Surface(
                        color = MaterialTheme.colorScheme.background,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp,
                    ) {
                        TopAppBar(
                            title = { Text(text = title, style = MaterialTheme.typography.headlineSmall) },
                            navigationIcon = {
                                if (canNavigateBack) {
                                    IconButton(onClick = { navController.popBackStack() }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Zurück",
                                        )
                                    }
                                }
                            },
                            actions = {
                                IconButton(onClick = { navController.navigate(Route.ReadingList) }) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = "Leseliste",
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior,
                            colors = topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                                titleContentColor = MaterialTheme.colorScheme.onBackground,
                            ),
                        )
                    }
                },
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Route.Home,
                    modifier = Modifier.padding(innerPadding),
                ) {
                composable<Route.Home> {
                    HomeScreen(
                        homeViewModel = homeViewModel,
                        favoritesViewModel = favoritesViewModel,
                        onBookClick = { book ->
                            navController.navigate(
                                Route.Details(
                                    id = book.id,
                                    title = book.title,
                                    author = book.authorNames.firstOrNull().orEmpty(),
                                    coverUrl = book.coverUrl,
                                    firstPublishYear = book.firstPublishYear,
                                ),
                            )
                        },
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
                                    author = book.authorNames.firstOrNull().orEmpty(),
                                    coverUrl = book.coverUrl,
                                    firstPublishYear = book.firstPublishYear,
                                ),
                            )
                        },
                    )
                }

                composable<Route.Settings> {
                    SettingsScreen(
                        musicViewModel = musicViewModel,
                        settingsViewModel = settingsViewModel,
                    )
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
                        favoritesViewModel = favoritesViewModel,
                        onBookClick = { book ->
                            navController.navigate(
                                Route.Details(
                                    id = book.id,
                                    title = book.title,
                                    author = book.authorNames.firstOrNull().orEmpty(),
                                    coverUrl = book.coverUrl,
                                    firstPublishYear = book.firstPublishYear,
                                ),
                            )
                        },
                    )
                }
            }
            }

            FloatingBottomBar(
                boxScope = this,
                navController = navController,
                currentDestination = currentDestination,
                bottomInset = bottomInset,
            )
        }
    }
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

@Composable
private fun bottomBarItemColors(): NavigationBarItemColors {
    return NavigationBarItemDefaults.colors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        selectedTextColor = MaterialTheme.colorScheme.onSurface,
        indicatorColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun FloatingBottomBar(
    boxScope: androidx.compose.foundation.layout.BoxScope,
    navController: NavController,
    currentDestination: androidx.navigation.NavDestination?,
    bottomInset: androidx.compose.ui.unit.Dp,
) {
    with(boxScope) {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = bottomInset + 8.dp,
                ),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f),
            ),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.14f),
                                MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.78f),
                                MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.84f),
                            ),
                        ),
                    ),
            ) {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    windowInsets = WindowInsets(0.dp),
                ) {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Home>() } == true,
                        onClick = { navigateToTab(navController, Route.Home) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        colors = bottomBarItemColors(),
                        alwaysShowLabel = true,
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Favorite>() } == true,
                        onClick = { navigateToTab(navController, Route.Favorite) },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoriten") },
                        label = { Text("Favoriten") },
                        colors = bottomBarItemColors(),
                        alwaysShowLabel = true,
                    )
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Settings>() } == true,
                        onClick = { navigateToTab(navController, Route.Settings) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") },
                        colors = bottomBarItemColors(),
                        alwaysShowLabel = true,
                    )
                }
            }
        }
    }
}
