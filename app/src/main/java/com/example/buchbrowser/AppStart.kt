package com.example.buchbrowser

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.buchbrowser.navigation.Route
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.example.buchbrowser.ui.theme.components.BookCard
import com.example.buchbrowser.ui.theme.screens.home.HomeScreen
import com.example.buchbrowser.ui.theme.screens.favorite.FavoriteScreen
import com.example.buchbrowser.ui.theme.screens.settings.SettingsScreen

/**
 * Der zentrale Einstiegspunkt für die Benutzeroberfläche und Navigation der App.
 *
 * Diese Composable-Funktion kapselt das globale [Scaffold]-Layout und verwaltet
 * die Hauptnavigation über eine [NavigationBar] am unteren Bildschirmrand sowie
 * den zugrunde liegenden [NavHost].
 *
 * **Hauptaufgaben:**
 * - Initialisierung und Verwaltung des [NavHostController].
 * - Bereitstellung der globalen Bottom-Navigation zum Wechseln zwischen Haupt-Screens.
 * - Sichere Weitergabe von `innerPadding` an den [NavHost], um Überlappungen mit System-Leisten zu verhindern.
 *
 */
@Composable
fun AppStart() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("AppNavigation","Aktueller Screen geändert zu: ${destination.route} ")
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Home>() } == true,
                    onClick = { navigateToTab(navController, targetRoute = Route.Home) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Favorite>() } == true,
                    onClick = { navigateToTab(navController, targetRoute = Route.Favorite) },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoriten") },
                    label = { Text("Favoriten") }
                )
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.hasRoute<Route.Settings>() } == true,
                    onClick = { navigateToTab(navController, targetRoute = Route.Settings) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = Route.Home,
            modifier = Modifier.padding(innerPadding)
        ){
            composable<Route.Home> {
                HomeScreen()
            }

            composable<Route.Favorite> {
                FavoriteScreen()
            }
            composable<Route.Settings> {
                SettingsScreen()
            }
            composable<Route.BookDetail> {
                Text("Hier werden bald die BookDetail angezeigt.")
            }
        }
    }
}

/**
 * Navigiert sicher zu einer Ziel-Route [targetRoute] unter Einhaltung der Android UX-Best-Practices
 * für die Bottom-Navigation.
 *
 * Diese Konfiguration stellt folgendes Verhalten sicher:
 * - **Keine Duplikate:** [launchSingleTop] verhindert das erneute Erstellen des Screens, wenn dieser bereits aktiv ist.
 * - **Sauberer Backstack:** [popUpTo] räumt die Navigationshistorie bis zur Start-Destination auf.
 *   Verhindert das Ansammlen unendlich vieler Screens beim Tab-Wechsel.
 * - **Zustandserhaltung:** [saveState] und [restoreState] sorgen dafür, dass Scroll-Positionen
 *   und Benutzereingaben beim Wechseln zwischen den Tabs erhalten bleiben.
 *
 * @param targetRoute Die Ziel-Route (z. B. [Route.Home] oder [Route.Search]), zu der navigiert werden soll.
 */
private fun navigateToTab(
    navController: NavController,
    targetRoute: Route
){
    navController.navigate(targetRoute) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
