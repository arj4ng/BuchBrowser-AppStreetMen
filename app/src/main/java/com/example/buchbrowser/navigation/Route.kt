package com.example.buchbrowser.navigation

import kotlinx.serialization.Serializable

/**
 * Repräsentiert alle Navigationsziele (Routen) innerhalb der Anwendung.
 *
 * Jede Route wird als typsicheres, serialisierbares Objekt definiert, um Fehler
 * bei der Argumentenübergabe und Tippfehler in Pfad-Strings zur Kompilierzeit zu vermeiden.
 *
 * @see Route.Home
 * @see Route.Search
 */
sealed interface Route {

    @Serializable
    data object Home: Route
    @Serializable
    data object Favorite : Route
    @Serializable
    data object Settings : Route

    @Serializable
    data class BookDetail(val id: String) : Route
}